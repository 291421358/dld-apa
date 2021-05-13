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

    /**
     * @apiNote
     * @author tzhh
     * @date 2021/5/13 11:05
     * @param id
     * @return {@link String}
     **/
    String getFactor(int id);

    /**
     * @apiNote
     * @author tzhh
     * @date 2021/5/13 11:05
     * @param projectParamId
     * @return {@link List< Map< String, Object>>}
     **/
    List<Map<String, Object>> getLatestOne(Integer projectParamId);

    /**
     * @apiNote
     * @author tzhh
     * @date 2021/5/13 11:07
     * @param id
	 * @param type
	 * @param time
     * @return {@link Map< String, List>}
     **/
    Map<String, List> getOneProjectsAllScalingByCon(int id, int type, String time);
    /***
     * @apiNote
     * @author tzhh
     * @date 2021/5/13 11:05
     * @param projectParamId
     * @param projectId
     * @return {@link int}
     **/
    int updateProjectsScaling(int projectParamId, String projectId);

    /***
     * @apiNote 
     * @author tzhh 
     * @date 2021/5/13 11:04
     * @param projectParamId
     * @param i
     * @return {@link List< Map< String, Object>>}
     **/
    List<Map<String, Object>> getOneProjectsScalingTime(int projectParamId, int i);

    /**
     *修改某个定标项目数值 updateProjectParam
     */
    int updateProject(Project project);

    /***
     * @apiNote 添加定标
     * @author tzhh 
     * @date 2021/5/13 11:04
     * @param scaling
     * @return {@link int}
     **/
    int insertScalingAlgorithm(Scaling scaling);

    /**
     *修改曲线 updateProjectParam
     */
    int updateScalingAlgorithm(Scaling scaling);
    /**
     * @apiNote
     * @author tzhh
     * @date 2021/5/13 11:07
     * @param dateId
     * @return {@link int}
     **/
    int deleteOneScaling(String dateId);
}
