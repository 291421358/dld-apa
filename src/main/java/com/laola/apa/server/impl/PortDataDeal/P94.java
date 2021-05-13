package com.laola.apa.server.impl.PortDataDeal;

import com.laola.apa.entity.Project;
import com.laola.apa.entity.RegentPlace;
import com.laola.apa.entity.UsedCode;
import com.laola.apa.server.PortDataDealService;
import com.laola.apa.server.ProjectTest;
import com.laola.apa.server.ReagentPlaceIntf;
import com.laola.apa.server.UsedCodeServer;
import com.laola.apa.utils.DataUtil;
import com.laola.apa.utils.String2Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("p94")
public class P94 implements PortDataDealService<String,Object> {
    @Autowired
    UsedCodeServer usedCodeServer;
    @Autowired
    ReagentPlaceIntf reagentPlaceIntf;
    @Autowired
    ProjectTest projectTest;
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
        //试剂项目id
        String paramid = split[2];
        //查询或插入
        UsedCode b = usedCodeServer.queryOrInsert(code,total);
        if(b == null){
            return "";
        }

        //试剂位置
        String place = string.substring(4, 6);


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
            Map<String, Object> p = new HashMap<>();
            p.put("density",split[i]);
            p.put("absorbance",split[i+1]);
            p.put("type","2");
            p.put("projectParamId",Integer.valueOf(paramid));
            p.put("starttime",DataUtil.now());
            p.put("endtime",DataUtil.now());
            Integer integer = projectTest.selectNextProjectNum();
            p.put("projectNum",integer);
            projectList.add(p);
        }
        projectTest.insertProjectList(projectList);
        return "";
    }
}