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
            PortDataDealService<String,String> beanByName =  SpringBeanUtil.getBeanByTypeAndName(PortDataDealService.class,"p" + s34);
            beanByName.deal(string);
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
            PortDataDealService<String,String> beanByName =  SpringBeanUtil.getBeanByTypeAndName(PortDataDealService.class,"p" + s34);
            beanByName.deal(string);
            return;
        }           //101-130       //20-40       //50-200
        //e5 90 128 (空杯子从哪拿) （抽标准液位子） （量）
        if (string.length() == 12) {
            logger.info("接收到辛普瑞拉" + string);
            testRate(string);
        }
        String temp = "";

        if ("94".equals(s34)) {
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
            PortDataDealService<String,Object> beanByName =  SpringBeanUtil.getBeanByTypeAndName(PortDataDealService.class,"p" + s34);
            beanByName.deal(string,serialPort);
//            deal9cCommand(string, serialPort);
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

    }

}
