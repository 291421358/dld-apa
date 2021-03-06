package com.laola.apa.server;

import com.laola.apa.entity.Project;
import com.laola.apa.entity.ProjectCurve;
import com.laola.apa.entity.ProjectQC;

import java.util.List;
import java.util.Map;


public interface ProjectTest {
    //根据项目id找曲线
    List<ProjectCurve> selectCurveById(Integer id);

    //保存项目
    int insertProject(Project project);

    //保存项目
    int insertProjectList(List<Map<String,Object>> projectList);

    //  * 查询有效的项目(在发送项目开始指令时)
    List<Map<String, Object>> selectAble();

    //查询没结束的项目
    List<Map<String, Object>> selectAbleProject();

    //查询没做过的项目
    List<Map<String, Object>> selectNeverDo(int i);

    //查询下一个projectNum
    Integer selectNextProjectNum();

    //下一个病人code
    Integer selectNextHumanCode();

    //取得要测的后13位字节集合
//    List<String> get(int code);

    //取得16个字节 List
    String getIdList(int id);

    //是否有项目在做
    int isProjectDoing();

    //获取某一天的数据
    Map<String, Object> getProjectListByData(Project project);

    Map<String, Object> getProjectListByDataTenToEnd(Project project);

    //根据条件查询项目
    List<Map<String, Object>> getProjectsByCon(String startTime, int humanCode);

    //根据条件查询定标项目
    List<Map<String, Object>> getQcProjects(int projectParamId, String beginDate, String EndDate, String type);

    void deleteProjects();

    void deleteProjects(String dateId, Integer paramId);

    void deleteProjectById(int id);

    //查询每天最后一个质控
    List<ProjectQC> getQcLastOneByDataAndType();

    void addby37();

    int l(String density);

    int getLastSca();
}
