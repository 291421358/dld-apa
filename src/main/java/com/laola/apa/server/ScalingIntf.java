package com.laola.apa.server;

import com.laola.apa.entity.Project;
import com.laola.apa.entity.Scaling;

import java.util.List;
import java.util.Map;

public interface ScalingIntf {
    /**
     * 根据项目id取得项目曲线
     * @param id
     * @return
     */
    List<Map<String, Object>> selectOneCurve(int id);

    String getFactor(int id);

    List<Map<String, Object>> getLatestOne(Integer projectParamId);

    //根据条件获得定标项目值
    Map<String, List> getOneProjectsAllScalingByCon(int id, int type, String time);
    /**
     * 修改定标项目
     */
    int updateProjectsScaling(int projectParamId, String projectId);

    List<Map<String, Object>> getOneProjectsScalingTime(int projectParamId, int i);

    /**
     *修改某个定标项目数值 updateProjectParam
     */
    int updateProject(Project project);

    int insertScalingAlgorithm(Scaling scaling);

    /**
     *修改曲线 updateProjectParam
     */
    int updateScalingAlgorithm(Scaling scaling);

    int deleteOneScaling(String dateId);
}
