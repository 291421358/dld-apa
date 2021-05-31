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
    private static final Logger logger = LoggerFactory.getLogger(GetProjectResultImp.class);
    static String SPLITCONDITION = "(?=eb9c)|(?=eb86)|(?=eb90)|(?=eb92)|(?=eb93)|(?=eb94)|(?=eb95)";//(?=eb91)|
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
        if (string.substring(0,4).equals("eb91") || string.substring(0,4).equals("eb9c"))
            SPLITCONDITION = " ";
        String[] split = string.split(SPLITCONDITION);
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            String s34 = s.substring(2, 4);
            logger.info("THE LENGTH OF GET DATA" + s.length());
            PortDataDealService<String,Object> beanByName =  SpringBeanUtil.getBeanByTypeAndName(PortDataDealService.class,"p" + s34);
            beanByName.deal(s,serialPort);
        }
    }

//    public static void main(String[] args) {
//        String a = "1111";
//        SPLITCONDITION = " ";
//        String[] split = a.split(SPLITCONDITION);
//        System.out.println(split[0]);
//    }
}
