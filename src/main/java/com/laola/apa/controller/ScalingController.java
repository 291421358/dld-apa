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
    /**
     * @apiNote 根据条件获得定标
     * @author tzhh
     * @date 2021/5/27 15:17
     * @param id
	 * @param type
	 * @param time
     * @return {@link Map< String, List>}
     **/
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
     * @apiNote 获得某个项目的所有定标时间
     * @author tzhh
     * @date 2021/5/27 15:18
     * @param projectParamId
     * @return {@link null}
     **/
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
     * @apiNote 获得某个项目最后一个定标 的所有项目
     * @author tzhh
     * @date 2021/5/27 15:23
     * @param projectParamId
     * @return {@link java.util.List<java.util.Map<java.lang.String,java.lang.Object>>}
     **/
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
     * @apiNote 修改某个项目的定标
     * @author tzhh
     * @date 2021/5/27 15:23
     * @param projectParamId
	 * @param projectId
     * @return {@link java.lang.String}
     **/
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
     * @apiNote 修改某个定标项目数值
     * @author tzhh
     * @date 2021/5/27 15:23
     * @param project
     * @return {@link java.lang.String}
     **/
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


    /***
     * @apiNote  添加定标项目计算方法
     * @author tzhh
     * @date 2021/5/27 15:22
     * @param scaling
     * @return {@link int}
     **/
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
     * @apiNote 修改某个定标曲线算法
     * @author tzhh
     * @date 2021/5/27 15:22
     * @param scaling
     * @return {@link int}
     **/
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
     * @apiNote 删除某个定标
     * @author tzhh
     * @date 2021/5/27 15:22
     * @param dateId
     * @param paramId
     * @return {@link int}
     **/
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
    /***
     * @apiNote 删除选中定标中的选中点
     * @author tzhh
     * @date 2021/5/27 15:22
     * @param projectId
     * @return {@link int}
     **/
    @RequestMapping("delOne")
    public int delOne(int projectId){
        System.out.println("11111111111111111");
        projectTest.deleteProjectById(projectId);
        return 1;
    }
}
