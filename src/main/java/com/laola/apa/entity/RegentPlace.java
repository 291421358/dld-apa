package com.laola.apa.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * (RegentPlace)实体类
 *
 * @author tzhh
 * @since 2021-02-23 11:10:56
 */
public class RegentPlace implements Serializable {
    private static final long serialVersionUID = -87848662514191173L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 项目参数id
     */
    private Integer project_param_id;

    /**
     * 位置
     */
    private Integer place;

    /**
     * 试剂编码
     */
    private String code;

    /**
     * a  标记
     * @return
     */
    private Integer a;

    public Integer getA() {
        return a;
    }

    public RegentPlace setA(Integer a) {
        this.a = a;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProject_param_id() {
        return project_param_id;
    }

    public RegentPlace setProject_param_id(Integer project_param_id) {
        this.project_param_id = project_param_id;
        return this;
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

    public RegentPlace() {
    }

    public RegentPlace(Integer id,Integer projectParamId, Integer place, String code) {
        this.project_param_id = projectParamId;
        this.id = id;
        this.place = place;
        this.code = code;
    }

}