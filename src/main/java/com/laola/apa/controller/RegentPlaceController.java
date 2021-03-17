package com.laola.apa.controller;

import com.laola.apa.entity.RegentPlace;
import com.laola.apa.server.ReagentPlaceIntf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return regentPlaceIntf.getRegentPlace();
    }
    /**
     * 修改试剂位置
     */
    @RequestMapping(value = "updateRegentPlace")
    public int updateRegentPlace(RegentPlace regentPlace){
        System.out.println(regentPlace.getProjectParamId()+""+regentPlace.getPlace()+regentPlace.getId()+regentPlace.getCode());
        return regentPlaceIntf.updateRegentPlace(regentPlace);
    }
}
