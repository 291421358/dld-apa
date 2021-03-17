package com.laola.apa.entity;

import java.io.Serializable;

/**
 * (RegentPlace)实体类
 *
 * @author tzhh
 * @since 2021-02-23 11:10:56
 */
public class RegentPlace implements Serializable {
    private static final long serialVersionUID = -87848662514191173L;
    
    private Integer id;
    
    private Integer projectParamId;
    
    private Integer place;
    
    private String code;


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

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}