package com.laola.apa.server.impl;

import com.google.gson.Gson;
import com.laola.apa.costant.TestConstant;
import com.laola.apa.entity.*;
import com.laola.apa.mapper.ProjectCurveMapper;
import com.laola.apa.mapper.ProjectMapper;
import com.laola.apa.mapper.ScalingMapper;
import com.laola.apa.server.*;
import com.laola.apa.utils.*;
import gnu.io.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Demo class
 *
 * @author tzhh
 * @date 2016/10/31
 */
@SuppressWarnings("ALL")
@Service
public class GetProjectResultImp implements GetProjectResult {
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ProjectCurveMapper projectCurveMapper;
    @Autowired
    private ProjectTest projectTest;
    @Autowired
    private ScalingIntf scalingIntf;
    @Autowired
    private PatientService patientService;
    @Autowired
    private ParamIntf projectParameters;
    @Autowired
    ScalingMapper scalingMapper;
    @Autowired
    EquipmentStateserver equipmentStateSever;
    @Autowired
    ReagentPlaceIntf reagentPlaceIntf;
    @Autowired
    UsedCodeServer usedCodeServer;
    @Autowired
    WebSocket webSocket;

    private static final Logger logger = LoggerFactory.getLogger(GetProjectResultImp.class);

    /**
     * 处理项目结果
     *
     * @param string
     * @param serialPort
     * @throws RuntimeException
     */
    @Override
    public void dealProjectResult(String string, SerialPort serialPort) {
        if (null == string) {
            return;
        }
        logger.info("收到数据长度为" + string.length());
        if (string.length() == 104) {
            logger.info("接收到ad数据" + string);
            try {
                readAD(serialPort.getInputStream(), "user1", WebSocket.getClients().get("user1"), string);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        String s34 = string.substring(2, 4);
        if ("90".equals(s34)){
            logger.info("接收到仪器温度数据" + string);
            PortDataDealService<String,String> beanByName =  SpringBeanUtil.getBeanByTypeAndName(PortDataDealService.class,"p" + s34);
            beanByName.deal(string);
            return;
        }
        if ("86".equals(s34)) {
            logger.info("接收到仪器状态数据" + string);
            PortDataDealService<String,String> beanByName =  SpringBeanUtil.getBeanByTypeAndName(PortDataDealService.class,"p" + s34);
            beanByName.deal(string);
            return;
        }

        if ("92".equals(s34) || string.length() > 2000) {
            logger.info("接收到光准数据" + string);
            try {
                readLightQuasi(serialPort.getInputStream(), "user1", WebSocket.getClients().get("user1"), string);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }           //101-130       //20-40       //50-200
        //e5 90 128 (空杯子从哪拿) （抽标准液位子） （量）
        if (string.length() == 12) {
            logger.info("接收到辛普瑞拉" + string);
            testRate(string);
        }
        String temp = "";

        if ("9f".equals(s34)) {
            logger.info("项目二维码信息" + string);
            dealQR(string);
        }
        if (string.indexOf("eb91") > 4) {
            //项目数据在后
            logger.info("项目数据在后 " + string);
            //temp == 架号位号
            temp = string.substring(0, string.indexOf("eb91"));
            //string == 项目数据
            string = string.substring(string.indexOf("eb91"));
        }
        if (string.indexOf("eb9c") > 4) {
            //架号位号数据在后
            logger.info("架号位号在后"+string);
            //temp == 架号位号
            temp = string.substring(string.indexOf("eb9c"));
            //string == 项目数据
            string = string.substring(0,string.indexOf("eb9c"));
        }
        s34 = string.substring(2, 4);
        if (DateUtils.decodeHEX(string.substring(4, 6)) <= 80 && string.length() > 64 && "91".equals(s34)) {
            logger.info("接收到结果数据" + string);
            dealResult(string);
        }
        if (!"".equals(temp)) {
            string = temp;
        }
        s34 = string.substring(2, 4);
        if ("9c".equals(s34)) {
            logger.info("接收到位号和二维码信息" + string);
            deal9cCommand(string, serialPort);
        }


    }

    private void dealQR(String string) {
        String[] split = string.substring(6).split(",");
        //唯一码
        String code = split[0];
        //试剂总量
        int total = Integer.parseInt(split[2]);
        //查询或插入
        UsedCode b = usedCodeServer.queryOrInsert(code,total);
        if(b == null){
            return;
        }

        //试剂位置
        String place = string.substring(4, 6);
        //试剂项目id
        String paramid = split[1];

        //设置试剂位置
        RegentPlace regentPlace = new RegentPlace();
            //设置位置
            regentPlace.setId(Integer.parseInt(place));
            //设置项目id
            regentPlace.setProjectParamId(Integer.valueOf(paramid));
            //设置试剂code
            regentPlace.setCode(code);
        reagentPlaceIntf.updateRegentPlace(regentPlace);

        //定标参数
        List<Map<String, Object>> projectList = new ArrayList<>();
        for (int i = 3; i <split.length ; i += 2) {
            Project project = new Project();
            project.setDensity(split[i]);
            project.setAbsorbance(split[i+1]);
            project.setType(2);
            project.setProjectParamId(Integer.valueOf(paramid));
            project.setStarttime(DataUtil.now());
            project.setEndtime(DataUtil.now());
            projectList.add((Map<String, Object>) project);
        }
        projectTest.insertProjectList(projectList);
    }

    /**
     * 辛普拉曼
     *
     * @param string
     */
    private void testRate(String string) {
        String rate = string.substring(6, 8);
        if (rate == null || "".equals(rate)) {
            System.err.println("进度为空");
        }
        logger.info(String.valueOf(DateUtils.decodeHEX(rate)));
        if (DateUtils.decodeHEX(rate) == 100) {
            // 初始化设备
            USBContral.init();

            // 开激光
            DeviceContral.setLaserPower(500);
            DeviceContral.setLaserPowerOn();
            DeviceContral.setLaserOn();

            // 设置积分时间和功率
            DeviceContral.setIntegrationTime(200);
            DeviceContral.setLaserPower(300);

            String sn = DeviceContral.getSN();
            logger.info(sn);
            // 采谱
            double[] spec = DeviceContral.requestSampleSpectrum();
            logger.info(ArrayUtils.doubleArray2String(spec));

            // 关激光
            DeviceContral.setLaserOff();
            DeviceContral.setLaserPowerOff();

            // 断开设备
            USBContral.clear();
        }

    }

    /**
     * 处理9c开头的命令。   来自于每一次样品架位移动时下位机发送到上位机
     * 收到架号位号信息
     *
     * @param string
     * @param serialPort
     * @throws IOException
     */
    private void deal9cCommand(String string, SerialPort serialPort) {
        //架号
        Integer rackNo = DateUtils.decodeHEX(string.substring(4, 6));
        //位号
        Integer placeNo = DateUtils.decodeHEX(string.substring(6, 8));
        //条形码scan_back
        String barCode = DateUtils.hexAscii2Str(string.substring(10, string.length() - 2));
        //
        EquipmentState equipmentState = new EquipmentState(1, rackNo, placeNo);
        equipmentStateSever.update(equipmentState);

        //查询有效的项目
        List<Map<String, Object>> ableList = projectTest.selectNeverDo();
        for (Map<String, Object> ablemap : ableList) {
            //循环有效项目
            if ((null != ablemap.get("rackNo") && Integer.parseInt(String.valueOf(ablemap.get("placeNo"))) == placeNo &&
                    rackNo == Integer.parseInt(String.valueOf(ablemap.get("rackNo")))) && !"2".equals(String.valueOf(ablemap.get("type")))) {
                //架号位号相同，进入发送功能，取得命令 根据项目id
                String commond = projectTest.getIdList(Integer.parseInt(String.valueOf(ablemap.get("id"))));
                //病员信息
                if ("1".equals(String.valueOf(ablemap.get("type")))){
                    Patient patient = new Patient(Integer.parseInt(String.valueOf(ablemap.get("human_code"))), "É".equals(barCode)?"":barCode);
                    //修改病员信息
                    patientService.update(patient);
                }
                //位号为一位，加上0 变成两个字符
                String strPlaceNo = String.valueOf(placeNo);
                if (strPlaceNo.length() == 1) {
                    strPlaceNo = "0" + strPlaceNo;
                }
                //
                Integer regentPlace = usedCodeServer.getCopies(Integer.parseInt(String.valueOf(ablemap.get("id"))));

                Project project = new Project();
                project.setId(Integer.parseInt(String.valueOf(ablemap.get("id"))));
                if (regentPlace<=0){
                    project.setAbnormal(1);
                }else if (regentPlace<= 5){
                    project.setAbnormal(2);
                }
                projectMapper.updateByPrimaryKeySelective(project);
                project = null;
                commond = TestConstant.TestHead + strPlaceNo + " " + commond + " 00 00";
                logger.info("发出的命令 123：" + commond);
                OutputStream outputStream = null;
                try {
                    outputStream = serialPort.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] binaryStr = DateUtils.hexStrToBinaryStr(commond);
                assert binaryStr != null;
                try {
                    outputStream.write(binaryStr, 0, binaryStr.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if ("2".equals(String.valueOf(ablemap.get("type"))) && (null != ablemap.get("rackNo") && Integer.parseInt(String.valueOf(ablemap.get("placeNo"))) == placeNo &&
                    rackNo == Integer.parseInt(String.valueOf(ablemap.get("rackNo"))))) {
                String commond = projectTest.getIdList(Integer.parseInt(String.valueOf(ablemap.get("id"))));
                String strPlaceNo = String.valueOf(placeNo);
                if (strPlaceNo.length() == 1) {
                    strPlaceNo = "0" + strPlaceNo;
                }
                commond = TestConstant.TestHead + strPlaceNo + " " + commond + " 00 00";
                logger.info("发出的命令；" + commond);
                OutputStream outputStream = null;
                try {
                    outputStream = serialPort.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] binaryStr = DateUtils.hexStrToBinaryStr(commond);
                assert binaryStr != null;
                try {
                    outputStream.write(binaryStr, 0, binaryStr.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * 处理9c开头的结果。  在做项目时，出一个结果时
     *
     * @param string
     * @throws Exception
     */
    private void dealResult(String string) {
        //判断是结果
        //截取前32 * 2位
        String result = string.substring(64);
        //查询没结束的项目
        List<Map<String, Object>> ableList = projectTest.selectAbleProject();
        //遍历32位后的七*2 位；每七*2 位遍历一次
        for (int i = 0; i < result.length() / 14; i++) {
            //x项目id
            int id = 0;
            int length = 0;
            int type = 0;
            float density = 0;
            int oldDensity = 0;
            int ppi = 0;
            boolean should = true;
            for (Map<String, Object> ablemap : ableList) {
                String projectNum = String.valueOf(DateUtils.decodeHEX(result.substring(i * 14, i * 14 + 2)));
                String projectNum1 = String.valueOf(ablemap.get("project_num"));
                if (projectNum1.equals(projectNum)) {
                    logger.info(projectNum1 + " project_num & project_num " + projectNum);
                    //串口传上来的前两位项目号和  数据库中的项目号相同
//                    logger.info(projectNum);
                    id = Integer.parseInt(String.valueOf(ablemap.get("id")));
                    length = Integer.parseInt(String.valueOf(ablemap.get("length")));
                    type = ablemap.get("type") == null ? 1 : Integer.parseInt(String.valueOf(ablemap.get("type")));
                    density = ablemap.get("density") == null ? 1 : Float.parseFloat(String.valueOf(ablemap.get("density")));
                    oldDensity = ablemap.get("cup_number") == null ? 1 : Integer.parseInt(String.valueOf(ablemap.get("cup_number")));
                    ppi = ablemap.get("ppi") == null ? 1 : Integer.parseInt(String.valueOf(ablemap.get("ppi")));
                    //如果该项目存在。不需要再插入项目
                    should = false;
                }

            }
            //如果该项目存在。不需要再插入项目
            if (should) {
                System.err.println("出结果时项目不存在");
                return;
            }
            if (DateUtils.decodeHEX(result.substring(i * 14 + 4, i * 14 + 6)) == 1) {
                //试剂减一
                logger.info("在收到第一个数据时001试剂减一");
                int i1 = usedCodeServer.minusOneCopyReagent(Integer.parseInt(String.valueOf(id)));
            }

            ProjectCurve projectCurve = new ProjectCurve();
            //设置曲线参数
            projectCurve.setProjectId(id);
            //数据号
            projectCurve.setX(DateUtils.decodeHEX(result.substring(i * 14 + 4, i * 14 + 6)));
            //eb91250000140000000000000000000001000000010000e16d0000000500050518052915bf15bf1a071f151615161c021515d015d01d04108897359813010b17841784
            //辅波高位，辅波低位 //取一组数据，第10-11位 *256 + 12-13位 为ad数值
            int auxiliary = DateUtils.decodeHEX(result.substring(i * 14 + 10, i * 14 + 12)) * 256 + DateUtils.decodeHEX(result.substring(i * 14 + 12, i * 14 + 14));
            //主波高位，主波低位 //取一组数据，第10-11位 *256 + 12-13位 为ad数值
            int major = DateUtils.decodeHEX(result.substring(i * 14 + 6, i * 14 + 8)) * 256 + DateUtils.decodeHEX(result.substring(i * 14 + 8, i * 14 + 10));
            //40000/ad，取对数获得吸光度
            PortDataDealService<String,String> beanByName =  SpringBeanUtil.getBeanByTypeAndName(PortDataDealService.class,"p86");
            beanByName.deal(string);
//            setBeindState(string);
            ProjectParam projectParam = projectParameters.onePoject(ppi);
            String mainWavelength = projectParam.getMainWavelength();
            if (auxiliary == 0) {
                auxiliary = 1;
            }
            if (major == 0) {
                major = 1;
            }
            double log = 0;
            //波长不为750时使用透射算法
            if (!"8".equals(mainWavelength)){
                 log = Math.log10(40000F / major);
            }else {
                //波长为750时使用散射算法
                 log = major/10000F;
            }
            //格式化吸光度 小数点后三位
            String formatAbs = new DecimalFormat("0.0000").format(log*2);
            //取得项目参数值

            //最大吸光度
            String maxAbsorbance = projectParam.getMaxAbsorbance();
            //最小吸光度
            String minAbsorbance = projectParam.getMinAbsorbance();
            Project projectSetAbnormal = new Project();
            projectSetAbnormal.setId(id);
            if (Float.parseFloat(formatAbs) > Float.parseFloat(maxAbsorbance)){
                projectSetAbnormal.setAbsorbanceHeight(1);
            }
            if (Float.parseFloat(formatAbs) < Float.parseFloat(minAbsorbance)){
                projectSetAbnormal.setAbsorbanceLow(1);
            }
            projectMapper.updateByPrimaryKeySelective(projectSetAbnormal);
            projectSetAbnormal = null;
            //吸光度*1000
            projectCurve.setY(String.valueOf(new DecimalFormat("0.0").format(Float.parseFloat(formatAbs) * 1000)));
            //插入曲线上的一个点
            projectCurveMapper.insert(projectCurve);
            //数据长度是否等读点数终点 代表最后和一个点读数结束
            if (DateUtils.decodeHEX(result.substring(i * 14 + 4, i * 14 + 6)) == length) {
                Project project = new Project();
                //设置结束时间为当前时间
                project.setEndtime(new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date()));
                //new Date().getTime() + 6000000
                project.setId(id);
                // 取得曲线
                List<Map<String, Object>> selectOneCurve = scalingIntf.selectOneCurve(id);
                //取得定标因子
                String factor = projectParam.getFactor();

                if (null == factor || "".equals(factor) || factor.length()<10){
                    project.setDensity("无定标");
                    projectMapper.updateByPrimaryKeySelective(project);
                    return;
                }
                //取得读数点
                String mainBegin = projectParam.getMainIndicationBegin();
                String mainEnd = projectParam.getMainIndicationEnd();

                String auxBegin = projectParam.getAuxiliaryIndicationBegin();
                String auxEnd = projectParam.getAuxiliaryIndicationEnd();
                auxEnd = auxEnd.equals("") ? "0" : auxEnd;
                auxBegin = auxBegin.equals("") ? "0" : auxBegin;
                //如果 主/辅终点 为空或者0 这使其赋值为主/辅 始点
                if ("".equals(auxBegin) || "0".equals(auxBegin)) {
                    auxBegin = mainBegin;
                }
                if ("".equals(auxEnd) || "0".equals(auxEnd)) {
                    auxEnd = mainEnd;
                }
                //取得数差
                float absorbanceGap = 0;
                //处理 读点数法
                //取得项目读数法
                String computeMethod = projectParam.getComputeMethod();

//                logger.info("计算结果得出absorbanceGap"+absorbanceGap);
                if (absorbanceGap < 0.1F) {
                    absorbanceGap = 0.1F;
                }
//                logger.info("计算结果得出absorbanceGap"+absorbanceGap);
                if ("终点法".equals(computeMethod)) {
                    //速率法 取 终点-起点
                    if (mainBegin == "0") {
                        absorbanceGap = DateUtils.getAbsorbanceGap(selectOneCurve, mainEnd) / 1000;
                    }else {
                        absorbanceGap = DateUtils.getAbsorbanceGap(selectOneCurve, mainBegin, mainEnd) / 1000;
                    }
                }
                logger.info("读数法：" + computeMethod);
                if ("速率法".equals(computeMethod)) {
                    //速率法 取 absorbanceGap/两数时间差*60
                    absorbanceGap = DateUtils.getAbsorbanceGap(selectOneCurve, mainBegin, mainEnd) / 1000;
                    logger.info("光准差：" + absorbanceGap);
                    absorbanceGap = absorbanceGap / (((Integer.parseInt(auxBegin) + Integer.parseInt(mainBegin)) / 2 - (Integer.parseInt(auxEnd.equals("") ? "0" : auxEnd) + Integer.parseInt(mainEnd.equals("") ? "0" : mainEnd)) / 2) * 11) * 60;
                }

                if ("两点法".equals(computeMethod)) {
                    //两点法
                    absorbanceGap = DateUtils.getAbsorbanceGap(selectOneCurve, mainBegin, mainEnd, auxBegin, auxEnd) / 1000;
                    logger.info(String.valueOf(absorbanceGap));
                }
//                logger.info("计算结果得出absorbanceGap"+absorbanceGap);

                if (type == 2) {
                    // 定标项目
                    project.setFactor(String.valueOf(new DecimalFormat("0.00").format(density / (absorbanceGap))));
                    project.setAbsorbance(String.valueOf(absorbanceGap));
                }
//                logger.info("计算结果得出absorbanceGap"+absorbanceGap);

                if (type == 1 || type == 3) {
                    //普通项目或者质控项目

                    setDensity(density, ppi, projectParam, project, factor, absorbanceGap);
                    if(type == 3){
                        //qc项目发送id和结果到页面
                        sendQC(oldDensity,density,ppi);
                    }
//                    logger.info(project);
                }
                projectMapper.updateByPrimaryKeySelective(project);

            }
        }
    }

    /**
     *
     * @param density
     * @param ppi
     */
    private void sendQC(float densityOld,float density, int ppi) {
        Map<String, Object> lightQuasiMap = new HashMap<>(11);
        lightQuasiMap.put("densityOld",densityOld);
        lightQuasiMap.put("density",density);
        lightQuasiMap.put("ppi",ppi);
//        lightQuasiMap.put("msg", lightQuasiMap);
        lightQuasiMap.put("id", "qc");
        Gson gson = new Gson();
        try {
            webSocket.sendMessageTo(gson.toJson(lightQuasiMap), "user1");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setDensity(float density, int ppi, ProjectParam projectParam, Project project, String factor, float absorbanceGap) {
        Example example = new Example(Project.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectParamId", ppi);
        criteria.andEqualTo("type", 2);
        String preDate = DataUtil.getPreDateByUnit(factor, 1, 12);
//                    logger.info("factor:"+factor);
        criteria.andBetween("starttime", factor, preDate);
        List<Project> projects = projectMapper.selectByExample(example);

//                    logger.info(projects);
        //创建浓度list
        StringBuilder den = new StringBuilder();
        //创建吸光度list
        StringBuilder abs = new StringBuilder();
        //取出浓度和吸光度
        for (Project project0 : projects) {
            den.append(project0.getDensity()).append(",");
            abs.append(project0.getAbsorbance()).append(",");
        }
        // xy 装换成 double类型的数组
        double[] y = DataUtil.string2Double(abs.toString());
        double[] x = DataUtil.string2Double(den.toString());
        DataUtil.DealXY dealXY = new DataUtil.DealXY(x, y).invoke();
        if (dealXY.is()){
            project.setDensity("无定标");
            projectMapper.updateByPrimaryKeySelective(project);
            return;
        };
        x = dealXY.getX();
        double[] xX = dealXY.getxX();
        double[] yY = dealXY.getyY();
        // 曲线list
        //根据时间取得定标参数
        logger.info(factor);
        Scaling scaling = scalingMapper.queryById(factor);
        if (xX.length < 1 || (xX.length>1 && xX[0] == xX[1] ) || null == scaling) {
            project.setDensity("定标异常");
            projectMapper.updateByPrimaryKeySelective(project);
            return;
        }

        String algorithm;
        if (scaling == null) {
            algorithm = "一次曲线";
        } else {
            algorithm = scaling.getAlgorithm();
        }
        //根据项目定标参数  获得项目定标算法
        algorithm = scaling.getAlgorithm();
        if (xX.length < 3 && "三次样条函数".equals(algorithm)) {
            algorithm = "一次曲线";
        }
        if ("三次样条函数".equals(algorithm)) {
            logger.info("三次样条");
            density = Formula.getSplineDensity(absorbanceGap, xX, yY);

        }
        if ("一次曲线".equals(algorithm)) {
            logger.info("一次曲线");
            density = Formula.getDensityByLinear(absorbanceGap, xX, yY);
//                        logger.info(density);
        }
        if ("二次曲线拟合".equals(algorithm)) {
            logger.info("二次曲线拟合");
            //二次曲线拟合
            density = Formula.quadratic(xX, yY, absorbanceGap);
        }

        if ("RodBard".equals(algorithm)) {
            density = Formula.RodBard(xX, yY, absorbanceGap);
            logger.info(String.valueOf(density));
        }
        if (density == -501){
            project.setDensity("<0.05");
            projectMapper.updateByPrimaryKeySelective(project);
            return;
        }
        if (density == -502){
            project.setDensity("ABS过大");
            projectMapper.updateByPrimaryKeySelective(project);
            return;
        }
        if (density == -503){
            project.setDensity("ABS越界");
            projectMapper.updateByPrimaryKeySelective(project);
            return;
        }
        // 普通项目
        String decimalDigit = projectParam.getDecimalDigit();
        String formatDigit = "0.00";
        switch (decimalDigit){
            case "0位":
                formatDigit = "0";
                break;
            case "1位":
                formatDigit = "0.0";
                break;
            case "2位":
                formatDigit = "0.00";
                break;
            default:

        }
        project.setDensity(new DecimalFormat(formatDigit).format(density));
    }



    /**
     * 设置State
     *
     * @param string
     */
    private void setBeindState(String string) {

    }

    /**
     * 读取ad
     *
     * @param inputStream
     * @param username
     * @param webSocket
     * @param hexStr
     * @throws InterruptedException
     * @throws IOException
     */
    private void readAD(InputStream inputStream, String username, WebSocket webSocket, String hexStr) throws InterruptedException, IOException {

        logger.info("转码后的数据" + hexStr);

        Map<String, String> ADMap = new HashMap<>(11);
        assert hexStr != null;
        if (!hexStr.substring(6, 8).equals("00")) {
            ADMap.put("tem",
                    String.valueOf((
                            new BigInteger(hexStr.substring(4, 6), 16).intValue() * 256
                                    + new BigInteger(hexStr.substring(6, 8), 16).intValue()) / 1351
                    )
            );
        }
        //取前72位数
        hexStr = hexStr.substring(64);
        for (int i = 0; i < hexStr.length() / 4; i++) {
            ADMap.put("ad" + String.valueOf(i + 1),
                    String.valueOf(
                            new BigInteger(hexStr.substring(i * 4, i * 4 + 2), 16).intValue() * 256
                                    + new BigInteger(hexStr.substring(i * 4 + 2, i * 4 + 4), 16).intValue()
                    )
            );
        }
//
        ADMap.put("id", "readAD");
        Gson gson = new Gson();
        webSocket.sendMessageTo(gson.toJson(ADMap), username);
    }

    /**
     * 读光准
     *
     * @param inputStream
     * @param username
     * @param webSocket
     */
    private void readLightQuasi(InputStream inputStream, String username, WebSocket webSocket, String hexStr) throws IOException, InterruptedException {
        Map<String, Object> lightQuasiMap = new HashMap<>(11);
        assert hexStr != null;
        List<String> LightQuasiStr = new ArrayList<>();
        for (int i = 0; i < hexStr.length() / 2; i++) {
            BigInteger bigInteger = new BigInteger(hexStr.substring(i * 2, i * 2 + 2), 16);
            if (i % 5 != 0 || (Integer.parseInt(String.valueOf(bigInteger)) > 250)) {
                LightQuasiStr.add(String.valueOf(bigInteger));
            }
        }
        lightQuasiMap.put("msg", LightQuasiStr);
        lightQuasiMap.put("id", "readLightQuasi");
        Gson gson = new Gson();
        webSocket.sendMessageTo(gson.toJson(lightQuasiMap), username);
    }

    public static void main(String[] args) {
        System.out.println(4000/10000F);
    }
}
