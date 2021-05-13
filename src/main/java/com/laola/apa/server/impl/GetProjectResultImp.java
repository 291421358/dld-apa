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
    static String SPLITCONDITION = "(?=eb9c)|(?=eb86)|(?=eb90)|(?=eb91)|(?=eb92)|(?=eb93)|(?=eb94)";
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
        String s34 = string.substring(2, 4);

        String[] split = string.split(SPLITCONDITION);
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            logger.info("THE LENGTH OF GET DATA" + s.length());
            PortDataDealService<String,Object> beanByName =  SpringBeanUtil.getBeanByTypeAndName(PortDataDealService.class,"p" + s34);
            beanByName.deal(s,serialPort);
        }
    }

}
