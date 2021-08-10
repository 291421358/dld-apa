package com.laola.apa;

import com.laola.apa.entity.*;
import com.laola.apa.mapper.ProjectParamMapper;
import com.laola.apa.mapper.RegentPlaceMapper;
import com.laola.apa.mapper.UsedCodeMapper;
import com.laola.apa.server.*;
import com.laola.apa.utils.DataUtil;
import com.laola.apa.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DlaApaApplication.class , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LaolaTest {
    @Autowired
    private UsedCodeServer usedCodeServer;
    @Autowired
    private ParamIntf paramIntf;
    @Autowired
    private ReagentPlaceIntf reagentPlaceIntf;

    @Autowired
    private ScalingIntf scalingIntf;

    @Autowired
    private ProjectParamMapper projectParamMapper;

    @Autowired
    private ProjectTest projectTest;
    @Test
    public void fTest() {
        projectTest.getIdList(13216);
        DateUtils.DEC2HEX(String.valueOf(Integer.parseInt("10")+160));
        ProjectParam projectParam = new ProjectParam();
        projectParam.setId(1);
        projectParam = projectParamMapper.selectOne(projectParam);
        String string = projectParam.toLitString();
        String replace = string.replace("null", "");
        String[] split = replace.split("-");
        ProjectParam projectParam1 = new ProjectParam(split);
        projectParam1.setId(99);
        int i = projectParamMapper.updateByPrimaryKeySelective(projectParam1);
        projectParamMapper.insertSelective(projectParam1);

    }

    @Test
    public void projectListTest(){


        UsedCode usedCode = new UsedCode();
        usedCode.setCode("oo");
        usedCode.setCount(80);
        usedCode.setTotal(80);
        usedCode.setCount2(80);
        usedCode.setTotal2(80);
        usedCodeServer.getCopies(7673);

    }

    @Test
    public void onePojectTest() throws Exception {
        List<Map<String, Object>> latestOne = scalingIntf.getLatestOne(6);
        Map<String, Object> map = latestOne.get(0);
        System.out.println(map);
    }
//    @Test
    public void update(){
        ProjectParam projectParam = new ProjectParam();
        projectParam.setId(3);
        projectParam.setNormalHigh("10");
        projectParam.setNormalLow("0");
        projectParam.setMinAbsorbance("-2");
        projectParam.setMaxAbsorbance("10");
        paramIntf.update(projectParam);
    }
}
