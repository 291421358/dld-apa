package com.laola.apa.server.impl.PortDataDeal;

import com.laola.apa.entity.Project;
import com.laola.apa.entity.RegentPlace;
import com.laola.apa.entity.UsedCode;
import com.laola.apa.server.PortDataDealService;
import com.laola.apa.server.ProjectTest;
import com.laola.apa.server.ReagentPlaceIntf;
import com.laola.apa.server.UsedCodeServer;
import com.laola.apa.utils.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("p94")
public class P94 implements PortDataDealService<String,String> {
    @Autowired
    UsedCodeServer usedCodeServer;
    @Autowired
    ReagentPlaceIntf reagentPlaceIntf;
    @Autowired
    ProjectTest projectTest;

    /**
     * 处理二维码
     * @param data
     * @return
     */
    @Override
    public String deal(String... data) {
        String string = data[0];
        String[] split = string.substring(6).split(",");
        //唯一码
        String code = split[0];
        //试剂总量
        int total = Integer.parseInt(split[2]);
        //查询或插入
        UsedCode b = usedCodeServer.queryOrInsert(code,total);
        if(b == null){
            return "";
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
        return "";
    }
}
