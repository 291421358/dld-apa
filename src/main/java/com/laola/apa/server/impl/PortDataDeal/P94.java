package com.laola.apa.server.impl.PortDataDeal;

import com.laola.apa.costant.AlgorithmConstant;
import com.laola.apa.entity.*;
import com.laola.apa.mapper.ProjectParamMapper;
import com.laola.apa.server.*;
import com.laola.apa.utils.DataUtil;
import com.laola.apa.utils.DateUtils;
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
    @Autowired
    EquipmentStateserver equipmentStateserver;
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
        //试剂位置
        String place = string.substring(4, 6);
        String substring6 = string.substring(6);
        String string1 = String2Hex.convertHexToString(substring6);
        string1 = DateUtils.unicodeDecode(string1);
        String[] split = string1.split(",");
        //唯一码
        String code = split[0];
        //试剂总量
        int total = Integer.parseInt(split[1]);
        //试剂项目
        String paramStr = split[2];
        paramStr = paramStr.replace("null", "");
        paramStr += "/ed";
        String[] param = paramStr.split("/");
        String paramid = param[0];

        ProjectParam projectParam = null;
        try {
            projectParam = new ProjectParam(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String dilution_sample_size = projectParam.getDilution_sample_size();
        String diluent_size = projectParam.getDiluent_size();
        if (null !=dilution_sample_size && null != diluent_size && !dilution_sample_size.equals("") && !diluent_size.equals("")&& !dilution_sample_size.equals("0") && !diluent_size.equals("0")){
            projectParam.setDiluent_place(String.valueOf(Integer.parseInt(place)*2-1));
        }else {
            projectParam.setDiluent_place(null);
        }



        String diluent_place = projectParam.getDiluent_place();
       //取得当前时间  作为定标时间
        String   thistime = DataUtil.now();

        if (null != diluent_place && !diluent_place.equals("")){
            UsedCode b = usedCodeServer.queryOrInsert(code,total);
            setRegentPlace(code, paramid, place,1);

        }else
        //计算方法
        if (split.length >=4){
            String algorithm = split[3];
            //根据算法代码获得计算方法全称
            if (algorithm == null || algorithm.equals("") || algorithm.equals(" "));
            algorithm = "1";
            algorithm = AlgorithmConstant.algorithm.get(algorithm);
            thistime = DataUtil.changeType(thistime);
            //查询或插入
            UsedCode b = usedCodeServer.queryOrInsert(code,total);
            setRegentPlace(code, paramid, place,0);
            if(b == null){
                return "";
            }
            List<Map<String, Object>> latestOne = scalingIntf.getLatestOne(Integer.valueOf(paramid));

            if (latestOne != null && latestOne.size() >0) {
                Map<String, Object> map = latestOne.get(0);
                Object starttime = map.get("starttime");
                long dateGap2Now = DataUtil.getDateGap2Now(String.valueOf(starttime));
                if (dateGap2Now < 6000){
                    thistime = DataUtil.getPreDateByUnit(String.valueOf(starttime), 1, Calendar.MINUTE);
                }
            }
            logger.info("thistime="+thistime);
            Scaling scaling = new Scaling(thistime,algorithm);
            scalingIntf.insertScalingAlgorithm(scaling);
            //添加定标项目
            int i = insertScalingProject(split, paramid, thistime);
        }else {
              usedCodeServer.queryOrInsert(code,total);
            setRegentPlace(code, paramid, place,0);
        }

        projectParam.setFactor(thistime);
        logger.info("thistime="+thistime);
        int i = projectParamMapper.updateByPrimaryKeySelective(projectParam);
//        scalingIntf.updateProjectsScaling(i,paramid);
        logger.info("update"+i);
        if (i == 0){
            projectParamMapper.insertSelective(projectParam);
        }
        if (place.equals("6") || place.equals("06")){
            EquipmentState equipmentState = new EquipmentState();
            equipmentState.setTemp("1");
            equipmentStateserver.update(equipmentState);
        }
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
    private int insertScalingProject(String[] split, String paramid, String thistime) {
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
            boolean add = projectList.add(p);
        }
        int i = projectTest.insertProjectList(projectList);
        return projectTest.getLastSca();
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
    private void setRegentPlace(String code, String paramid, String place ,int a) {
        //设置试剂位置
        RegentPlace regentPlace = new RegentPlace();
        //设置位置
        regentPlace.setId(Integer.parseInt(place));
        //设置项目id
        regentPlace.setProject_param_id(Integer.valueOf(paramid));
        regentPlace.setPlace(Integer.parseInt(place));
        //设置试剂code
        regentPlace.setCode(code);
        regentPlace.setA(a);
        int i1 = reagentPlaceIntf.updateRegentPlace(regentPlace);
    }

}
