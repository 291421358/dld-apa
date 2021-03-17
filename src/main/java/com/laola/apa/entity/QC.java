package com.laola.apa.entity;

import java.io.Serializable;

/**
 * (QC)实体类
 *
 * @author tzhh
 * @since 2020-06-16 13:41:45
 */
public class QC implements Serializable {
    private static final long serialVersionUID = 824034935575984500L;
    
    private Integer id;
    /**
    * 项目号
    */
    private String projectParamNum;
    /**
    * 标准值
    */
    private String staQuality;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectParamNum() {
        return projectParamNum;
    }

    public void setProjectParamNum(String projectParamNum) {
        this.projectParamNum = projectParamNum;
    }

    public String getStaQuality() {
        return staQuality;
    }

    public void setStaQuality(String staQuality) {
        this.staQuality = staQuality;
    }

}