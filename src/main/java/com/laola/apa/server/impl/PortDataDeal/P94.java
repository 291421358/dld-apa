package com.laola.apa.server.impl.PortDataDeal;

import com.laola.apa.costant.AlgorithmConstant;
import com.laola.apa.entity.ProjectParam;
import com.laola.apa.entity.RegentPlace;
import com.laola.apa.entity.Scaling;
import com.laola.apa.entity.UsedCode;
import com.laola.apa.mapper.ProjectParamMapper;
import com.laola.apa.server.*;
import com.laola.apa.utils.DataUtil;
import com.laola.apa.utils.String2Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("p94")
public class P94 implements PortDataDealService<String,Object> {
    @Autowired
    UsedCodeServer usedCodeServer;
    @Autowired
    ReagentPlaceIntf reagentPlaceIntf;
    @Autowired
    ProjectTest projectTest;
    @Autowired
    ScalingIntf scalingIntf;
    @Autowired
    private ProjectParamMapper projectParamMapper;
    private static final Logger logger = LoggerFactory.getLogger(P94.class);

    /**
     * 处理二维码
     * @param data
     * @return
     */
    @Override
    public String deal(Object... data) {

        String string = String.valueOf(data[0]);
        logger.info("PROJECT QR INFORMATION" + string);

        String substring6 = string.substring(6);
        String string1 = String2Hex.convertHexToString(substring6);
        String[] split = string1.split(",");
        //唯一码
        String code = split[0];
        //试剂总量
        int total = Integer.parseInt(split[1]);
        //试剂项目
        String paramStr = split[2];
        String[] param = paramStr.split("-");
        String paramid = param[0];
        ProjectParam projectParam = new ProjectParam(param);
        int i = projectParamMapper.updateByPrimaryKeySelective(projectParam);
        logger.info("update"+i);
        if (i == 0){
            projectParamMapper.insertSelective(projectParam);
        }
        //试剂位置
        String place = string.substring(4, 6);
        //计算方法
        String algorithm = split[3];
        //根据算法代码获得计算方法全称
        algorithm = AlgorithmConstant.algorithm.get(algorithm);
        //查询或插入
        UsedCode b = usedCodeServer.queryOrInsert(code,total);
        setRegentPlace(code, paramid, place);
        if(b == null){
            return "";
        }


        List<Map<String, Object>> latestOne = scalingIntf.getLatestOne(Integer.valueOf(paramid));

        String    thistime = DataUtil.now();;
        if (latestOne != null && latestOne.size() >0) {
            Map<String, Object> map = latestOne.get(0);
            Object starttime = map.get("starttime");
            long dateGap2Now = DataUtil.getDateGap2Now(String.valueOf(starttime));
            if (dateGap2Now < 6000){
                thistime = DataUtil.getPreDateByUnit(String.valueOf(starttime), 1, Calendar.MINUTE);
            }
        }

        Scaling scaling = new Scaling(thistime,algorithm);
        scalingIntf.insertScalingAlgorithm(scaling);
        //添加定标项目
        insertScalingProject(split, paramid, thistime);
        return "";
    }
    /**
     * @apiNote 添加定标项目
     * @author tzhh
     * @date 2021/5/17 14:34
     * @param split
	 * @param paramid
	 * @param thistime
     * @return
     **/
    private void insertScalingProject(String[] split, String paramid, String thistime) {
        List<Map<String, Object>> projectList = new ArrayList<>();
        for (int i = 4; i <split.length ; i += 2) {
            Map<String, Object> p = new HashMap<>();
            p.put("density",split[i]);
            p.put("absorbance",split[i+1]);
            p.put("type","2");
            p.put("projectParamId",paramid);
            p.put("starttime",thistime);
            p.put("endtime",thistime);
            Integer integer = projectTest.selectNextProjectNum();
            p.put("projectNum",integer);
            projectList.add(p);
        }
        projectTest.insertProjectList(projectList);
    }
    /**
     * @apiNote 设置试剂位置
     * @author tzhh 
     * @date 2021/5/17 14:34
     * @param code
	 * @param paramid
	 * @param place
     * @return
     **/
    private void setRegentPlace(String code, String paramid, String place) {
        //设置试剂位置
        RegentPlace regentPlace = new RegentPlace();
        //设置位置
        regentPlace.setId(Integer.parseInt(place));
        //设置项目id
        regentPlace.setProject_param_id(Integer.valueOf(paramid));
        regentPlace.setPlace(Integer.parseInt(place));
        //设置试剂code
        regentPlace.setCode(code);
        int i1 = reagentPlaceIntf.updateRegentPlace(regentPlace);
    }

}
