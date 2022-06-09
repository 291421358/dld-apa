package com.laola.apa.controller;

import com.laola.apa.entity.RegentPlace;
import com.laola.apa.server.ReagentPlaceIntf;
import com.laola.apa.server.ScalingIntf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "regentPlace")
public class RegentPlaceController {
    @Autowired
    private ReagentPlaceIntf regentPlaceIntf;
    /**
     *  获得试剂位置
     * @return
     */
    @RequestMapping(value = "getRegentPlace")
    public List getRegentPlace(){
        List<List> li = new ArrayList<>();
        List<Map<String, Object>> boolScal = regentPlaceIntf.getBoolScal();
        li.add(boolScal);
        List<Map<String, Object>> regentPlace = regentPlaceIntf.getRegentPlace();
        li.add(regentPlace);
        return li;
    }
    /**
     * 修改试剂位置
     */
    @RequestMapping(value = "updateRegentPlace")
    public int updateRegentPlace(RegentPlace regentPlace){
        System.out.println(regentPlace.getProject_param_id()+""+regentPlace.getPlace()+regentPlace.getId()+regentPlace.getCode());
        return regentPlaceIntf.updateRegentPlace(regentPlace);
    }
}
