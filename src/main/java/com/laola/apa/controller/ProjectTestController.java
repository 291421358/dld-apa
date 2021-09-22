package com.laola.apa.controller;

import com.google.gson.Gson;
import com.laola.apa.entity.Project;
import com.laola.apa.entity.ProjectCurve;
import com.laola.apa.entity.ProjectQC;
import com.laola.apa.server.ProjectTest;
import com.laola.apa.utils.DataUtil;
import com.laola.apa.utils.SerialUtil;
import gnu.io.SerialPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value = "productTest")
public class ProjectTestController {
    @Autowired
    private ProjectTest projectTest;

    /**
     *
     */
    @RequestMapping(value = "l")
    public int l(String density){
         return projectTest.l(density);
    }

    /**
     *
     */
@RequestMapping(value = "getQcLastOneByDataAndType")
    public List<ProjectQC> getQcLastOneByDataAndType(){
    List<ProjectQC> qcLastOneByDataAndType = projectTest.getQcLastOneByDataAndType();
    return qcLastOneByDataAndType;
    }
    /**
     * 保存项目
     *
     * @param project
     * @return
     */
    @RequestMapping(value = "saveProject")
    public synchronized int saveProject(Project project) {
        if (project.getStarttime() == null || project.getStarttime().equals("")){
            project.setStarttime(DataUtil.now());
        }else {
            String dateid = project.getStarttime();
            dateid = dateid.replace("年","-");
            dateid = dateid.replace("月","-");
            dateid = dateid.replace("日"," ");
            dateid = dateid.replace("时",":");
            dateid = dateid.replace("分",":00");
            project.setStarttime(dateid);
        }
        Integer integer = projectTest.selectNextProjectNum();
        project.setProjectNum(integer);
//        System.out.println(integer);
        int i = projectTest.insertProject(project);
        return i;
    }

    /**
     * 保存项目list
     *
     * @param projectListStr
     * @return
     */
    @RequestMapping(value = "saveProjectList")
    public int saveProjectList(String projectListStr) {
        Gson gson = new Gson();
        List<Map<String, Object>> list = (List<Map<String, Object>>) gson.fromJson(projectListStr, List.class);
        Integer integer = projectTest.selectNextProjectNum();
//        int[] paramArray = new int[9];
        for (Map<String, Object> project : list) {
//            paramArray[Integer.parseInt(String.valueOf(project.get("projectParamId")).replace(".0",""))] = Integer.parseInt(String.valueOf(project.get("projectParamId")).replace(".0",""));
            project.put("starttime", DataUtil.now());
            project.put("projectNum", integer);
            project.putIfAbsent("type", 1);
            Object barCode = project.get("barCode");
            if (null != barCode && !String.valueOf(barCode).replaceAll(" ","").equals("") && !String.valueOf(barCode).equals(" ")){
                project.put("rackId","-1");
            }
            integer++;
            if (integer > 80) {
                integer = 1;
            }
        }
//        if (reagentPlaceIntf.getParamIds(paramArray)){
//            System.out.println(Arrays.toString(paramArray));
//            return -2;
//        }
        System.out.println(list);
        return projectTest.insertProjectList(list);
    }

    /**
     * 取下一个用户code
     *
     * @return
     */
    @RequestMapping(value = "selectNextHumanCode")
    public int selectNextHumanCode() {
        int i = projectTest.selectNextHumanCode();
        return i;
    }

    /**
     * 获取某一天的数据
     *
     * @param project
     * @return
     */
    @RequestMapping(value = "getProjectListByData")
    public String getProjectListByData(Project project) {
        Map<String, Object> resultMap = new HashMap<>(16);
        Map<String, Object> projectListList = projectTest.getProjectListByData(project);
        resultMap.put("code", 0);
        resultMap.put("msg", "");
        resultMap.put("count", 300);
        resultMap.put("data", projectListList);
        Gson json = new Gson();
        return json.toJson(projectListList);
    }


    /**
     * 获取某一天的结果
     *
     * @param project
     * @return
     */
    @RequestMapping(value = "getProjectDensityByData")
    public String getProjectDensityByData(Project project) {
        Map<String, Object> projectListList = projectTest.getProjectListByData(project);
        String densitys = "";
        for (Map.Entry<String, Object> entry : projectListList.entrySet()) {

            String key = entry.getKey().toString();
            if (key.equals("maxCode") || key.equals("minCode")){
                break;
            }
            ArrayList value = (ArrayList)entry.getValue();
            System.out.println(value);
            for (int j = 0; j < value.size(); j++) {
                Map<String, String> stringStringMap = (Map<String, String>) value.get(j);
                densitys += stringStringMap.get("progress")+" ";
            }
            densitys += "<br> ";
        }
        return densitys;
    }
    /**
     * 获取某一天的数据 后十条
     *
     * @param project
     * @return
     */
    @RequestMapping(value = "getProjectListByDataTenToEnd")
    public String getProjectListByDataTenToEnd(Project project) {
        Map<String, Object> resultMap = new HashMap<>(16);
        System.out.println();
        Map<String, Object> projectListList = projectTest.getProjectListByDataTenToEnd(project);
        resultMap.put("code", 0);
        resultMap.put("msg", "");
        resultMap.put("count", 300);
        resultMap.put("data", projectListList);
        Gson json = new Gson();
        return json.toJson(projectListList);
    }


    /**
     * 获取曲线通过id
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "selectCurveById")
    public List<Map<String, Float>> s(int id) {
        List<ProjectCurve> projectListList = projectTest.selectCurveById(id);
        float max = 0;
        float min = 0;
        float maxX = 0;
        float minX = 0;
        //取得最大值 和最小值
        for (ProjectCurve projectCurve : projectListList) {
            //取最大的 y
            if (Float.parseFloat(projectCurve.getY()) > max) {
                max = Float.parseFloat(projectCurve.getY());
            }
            //取最小的Y
            if (Float.parseFloat(projectCurve.getY()) < min || min == 0) {
                min = Float.parseFloat(projectCurve.getY());
            }
            //取最大的x
            if (projectCurve.getX() > maxX) {
                maxX = projectCurve.getX();
            }
            //取最小的x
            if (projectCurve.getX() < minX || minX == 0) {
                minX = projectCurve.getX();
            }
        }
        System.out.println(max + "::::::::" + min);
//        if ()
        float y = 180;
        if ((Float.parseFloat(String.valueOf(max)) - Float.parseFloat(String.valueOf(min))) < 100) {
            y = 50;
        }
        float gap = (Float.parseFloat(String.valueOf(max)) - Float.parseFloat(String.valueOf(min))) / y;
        float gapX = (Float.parseFloat(String.valueOf(maxX)) - Float.parseFloat(String.valueOf(minX))) / 180;
        List<Map<String, Float>> resultList = new ArrayList<>();
        for (ProjectCurve projectCurve : projectListList) {
            Map<String, Float> map = new HashMap<>();
            map.put("y", (Float.parseFloat(projectCurve.getY()) - min) / gap);
            map.put("x", (projectCurve.getX() - minX) / gapX);
            map.put("abs", Float.valueOf(projectCurve.getY())/1000);
            map.put("t",Float.valueOf(projectCurve.getT()));
            resultList.add(map);
        }
        Map<String, Float> maxminMap = new HashMap<>();
        maxminMap.put("max", max);
        maxminMap.put("maxY", y);
        maxminMap.put("min", min);
        resultList.add(maxminMap);
        return resultList;
    }


    /**
     * 删除所有未做的数据
     *
     * @return
     */
    @RequestMapping(value = "deleteProjects")
    public String deleteProjects() {
        projectTest.deleteProjects();
        return "200";
    }


    /**
     * 37添加结果的数据
     *
     * @return
     */
    @RequestMapping(value = "addby37")
    public String addby37() {
        projectTest.addby37();
        return "200";
    }

    @RequestMapping(value = "lis")
    public void lis(){
        SerialUtil serialUtil = new SerialUtil();
        serialUtil.init("51 43 52 50 2F 61 61 61 61 61 61 61 60 61 60 60 60 61 61 61 55","COM2");


    }
}
