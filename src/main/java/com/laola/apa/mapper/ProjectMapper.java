package com.laola.apa.mapper;

import com.laola.apa.entity.Project;
import com.laola.apa.entity.ProjectQC;
import com.laola.apa.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectMapper extends MyMapper<Project> {
    int insertProjectList(@Param("projects") List projects);

    int deleteProjectByParamId(int paramid);

    void deleteProjects();
    /**
     * 通过dateId paramId删除数据
     *
     */
    void deleteProjectsByDateIdParamId(String dateId, Integer paramId);

    List<ProjectQC> getQcLastOneByDataAndType();

    void addby37();

    void l(String s, Integer s1);

    void clean(int i);

    int getLastSca();
}