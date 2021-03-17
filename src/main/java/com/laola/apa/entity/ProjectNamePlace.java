package com.laola.apa.entity;

import java.io.Serializable;

/**
 * (ProjectNamePlace)实体类
 *
 * @author tzhh
 * @since 2020-05-13 11:03:28
 */
public class ProjectNamePlace implements Serializable {
    private static final long serialVersionUID = -74391756033758484L;
    
    private Integer id;
    
    private Integer projectParamId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectParamId() {
        return projectParamId;
    }

    public void setProjectParamId(Integer projectParamId) {
        this.projectParamId = projectParamId;
    }

}