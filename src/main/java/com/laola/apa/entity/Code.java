package com.laola.apa.entity;

import java.io.Serializable;

/**
 * (Code)实体类
 *
 * @author tzhh
 * @since 2021-02-20 14:45:10
 */
public class Code implements Serializable {
    private static final long serialVersionUID = 475729231233134116L;
    
    private Integer id;
    /**
     * 试剂唯一码
     */
    private String code;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}