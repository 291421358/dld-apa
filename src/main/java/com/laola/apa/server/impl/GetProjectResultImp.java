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
    private ProjectTest projectTest;
    @Autowired
    private PatientService patientService;
    @Autowired
    EquipmentStateserver equipmentStateSever;
    @Autowired
    ReagentPlaceIntf reagentPlaceIntf;
    @Autowired
    UsedCodeServer usedCodeServer;


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
        String s34 = string.substring(2, 4);
        if ("92".equals(s34)) {
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

        if ("93".equals(s34) || string.length() > 2000) {
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
            PortDataDealService<String,String> beanByName =  SpringBeanUtil.getBeanByTypeAndName(PortDataDealService.class,"p" + s34);

            beanByName.deal(string);
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

}
