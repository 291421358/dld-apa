package com.laola.apa.controller;

import com.google.gson.Gson;
import com.laola.apa.entity.Project;
import com.laola.apa.entity.Scaling;
import com.laola.apa.server.ProjectTest;
import com.laola.apa.server.ScalingIntf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("scaling")
public class ScalingController {
    @Autowired
    ScalingIntf scalingIntf;
    @Autowired
    ProjectTest projectTest;
    @RequestMapping("getOneProjectsAllScalingByCon")
    public Map<String, List> getOneProjectsAllScalingByCon(Integer id, Integer type, String time){
        Map<String,List> resultMap = scalingIntf.getOneProjectsAllScalingByCon(id,type,time);
//        resultMap.put("code",0);
//        resultMap.put("msg","");
//        resultMap.put("count",300);
//        resultMap.put( "data",projectListList);
//        Gson json = new Gson();
        return  resultMap;
    }



    /**
     * 获得某个项目的所有定标时间
     * @return
     */
    @RequestMapping("getOneProjectsScalingTime")
    public List<Map<String, Object>> getOneProjectsScalingTime(Integer projectParamId){
        Map<String,Object> resultMap = new HashMap<>(16);
        List<Map<String, Object>> projectListList = scalingIntf.getOneProjectsScalingTime(projectParamId,2);
        resultMap.put("code",0);
        resultMap.put("msg","");
        resultMap.put("count",300);
        resultMap.put( "data",projectListList);
        Gson json = new Gson();
        return projectListList;
    }

    /**
     * 获得某个项目最后一个定标
     * @return
     */
    @RequestMapping("getLatestOne")
    public List<Map<String, Object>> getLatestOne(Integer projectParamId){
        Map<String,Object> resultMap = new HashMap<>(16);
        List<Map<String, Object>> projectListList = scalingIntf.getLatestOne(projectParamId);
        resultMap.put("code",0);
        resultMap.put("msg","");
        resultMap.put("count",300);
        resultMap.put( "data",projectListList);
        Gson json = new Gson();
        return projectListList;
    }

    /**
     * 修改某个项目的定标
     * @return
     */
    @RequestMapping("updateProjectsScaling")
    public String updateProjectsScaling(int projectParamId,String projectId){
        Map<String,Object> resultMap = new HashMap<>(16);
        int projectListList = scalingIntf.updateProjectsScaling(projectParamId,projectId);
        resultMap.put("code",0);
        resultMap.put("msg","");
        resultMap.put("count",300);
        resultMap.put( "data",projectListList);
        Gson json = new Gson();
        return json.toJson(projectListList);
    }


    /**
     * 修改某个定标项目数值
     * @return
     */
    @RequestMapping("updateProject")
    public String updateProject(Project project){
        Map<String,Object> resultMap = new HashMap<>(16);
        int projectListList = scalingIntf.updateProject(project);
        resultMap.put("code",0);
        resultMap.put("msg","");
        resultMap.put("count",300);
        resultMap.put( "data",projectListList);
        Gson json = new Gson();
        return json.toJson(projectListList);
    }


    /**
     * 添加定标项目计算方法
     * @return
     */
    @RequestMapping("insertScalingAlgorithm")
    public int insertScalingAlgorithm(Scaling scaling){
        String dateid = scaling.getDateid();
        dateid = dateid.replace("年","-");
        dateid = dateid.replace("月","-");
        dateid = dateid.replace("日"," ");
        dateid = dateid.replace("时",":");
        dateid = dateid.replace("分","");
        scaling.setDateid(dateid);
        return scalingIntf.insertScalingAlgorithm(scaling);
    }

    /**
     * 修改某个定标曲线算法
     * @return
     */
    @RequestMapping("updateScalingAlgorithm")
    public int updateScalingAlgorithm(Scaling scaling){
        String dateid = scaling.getDateid();
        dateid = dateid.replace("年","-");
        dateid = dateid.replace("月","-");
        dateid = dateid.replace("日"," ");
        dateid = dateid.replace("时",":");
        dateid = dateid.replace("分","");
        scaling.setDateid(dateid);
        return scalingIntf.updateScalingAlgorithm(scaling);
    }

    /**
     * 删除某个定标
     * @return
     */
    @RequestMapping("deleteOneScaling")
    public int deleteOneScaling(String dateId,Integer paramId){
        dateId = dateId.replace("年","-");
        dateId = dateId.replace("月","-");
        dateId = dateId.replace("日"," ");
        dateId = dateId.replace("时",":");
        dateId = dateId.replace("分","");
        projectTest.deleteProjects(dateId,paramId);
        return scalingIntf.deleteOneScaling(dateId);

    }
}
