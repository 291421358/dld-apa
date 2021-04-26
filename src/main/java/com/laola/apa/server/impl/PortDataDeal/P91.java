package com.laola.apa.server.impl.PortDataDeal;

import com.google.gson.Gson;
import com.laola.apa.entity.Project;
import com.laola.apa.entity.ProjectCurve;
import com.laola.apa.entity.ProjectParam;
import com.laola.apa.entity.Scaling;
import com.laola.apa.mapper.ProjectCurveMapper;
import com.laola.apa.mapper.ProjectMapper;
import com.laola.apa.mapper.ScalingMapper;
import com.laola.apa.server.*;
import com.laola.apa.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service("p91")
public class P91 implements PortDataDealService<String,String> {

    @Autowired
    private ProjectTest projectTest;
    @Autowired
    UsedCodeServer usedCodeServer;
    @Autowired
    private ParamIntf projectParameters;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ProjectCurveMapper projectCurveMapper;
    @Autowired
    private ScalingIntf scalingIntf;
    @Autowired
    ScalingMapper scalingMapper;
    @Autowired
    WebSocket webSocket;
    private static final Logger logger = LoggerFactory.getLogger(PortDataDealService.class);

    /**
     * 处理9c开头的结果。  在做项目时，出一个结果时
     *
     * @param strings
     * @throws Exception
     */
    @Override
    public String deal(String ... strings) {
            String  string= strings[0];
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
                        break;
                    }

                }
                //如果该项目存在。不需要再插入项目
                if (should) {
                    System.err.println("出结果时项目不存在");
                    return "出结果时项目不存在";
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
                        return "无定标";
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
            return "";
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
}
