package com.laola.apa.controller;


import com.laola.apa.entity.ProjectNamePlace;
import com.laola.apa.entity.ProjectParam;
import com.laola.apa.server.ParamIntf;
import com.laola.apa.server.ProjectNamePlaceServer;
import com.laola.apa.vo.ProjectListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "parameter")
public class ParameterController {
    @Autowired
    private ParamIntf paramIntf;
    @Autowired
    private ProjectNamePlaceServer projectNamePlaceServer;




    /**
     * 项目列表
     * @return
     */
    @RequestMapping(value = "projectList" , method = RequestMethod.GET)
    public Map<String, Object> projectList(){
        List<ProjectListVO> projectList = paramIntf.projectList();
        List<ProjectNamePlace> namePlaceList = projectNamePlaceServer.queryAll(null);
        Map<String,Object> projectListMap = new HashMap<>();
        projectListMap.put("nameMap",projectList);
        projectListMap.put("namePlaceList",namePlaceList);

        return projectListMap;
    }

    /**
     * 项目列表
     * @return
     */
    @RequestMapping(value = "projectMap" , method = RequestMethod.GET)
    public Map<String, Object> projectMap(){
        Map<String, String> projectMap = paramIntf.projectMap();
        List<ProjectNamePlace> namePlaceList = projectNamePlaceServer.queryAll(null);
        Map<String,Object> projectListMap = new HashMap<>();
        projectListMap.put("nameMap",projectMap);
        projectListMap.put("namePlaceList",namePlaceList);

        return projectListMap;
    }

    /**
     * 单个项目查询
     * @param id
     * @return
     */
    @RequestMapping(value = "oneProject" , method = RequestMethod.GET)
    public ProjectParam onePoject(Integer id){
        ProjectParam projectParam = paramIntf.onePoject(id);
        return projectParam;
    }

    /**
     * 更新
     * @param projectParam
     * @return
     * @deprecated updateByPrimaryKeySelective 方法 ，传入的值为空则不修改
     */
    @RequestMapping(value = "update" , method = RequestMethod.GET)
    public String update(ProjectParam projectParam){
        System.out.println(projectParam.getId());

        paramIntf.update(projectParam);
        return "200";
    }

    /**
     * 增加
     * @param projectParam
     * @return 200
     */
    @RequestMapping(value = "insert" , method = RequestMethod.GET)
    public String insert(ProjectParam projectParam){
        System.out.println(projectParam.getId());

        paramIntf.insert(projectParam);
        return "200";
    }
    /**
     * 生成二维码
     * @param id
     * @return
     * @deprecated updateByPrimaryKeySelective 方法 ，传入的值为空则不修改
     */
    @RequestMapping(value = "createQRCode" , method = RequestMethod.GET)
    public String createQRCode(int id,int total){
        System.out.println(id);

        paramIntf.createQRCode(id,total);
        return "200";
    }

}
