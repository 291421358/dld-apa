package com.laola.apa.entity;

import java.io.Serializable;

/**
 * (Doctor)实体类
 *
 * @author tzhh
 * @since 2020-05-06 16:42:39
 */
public class Doctor implements Serializable {
    private static final long serialVersionUID = -73446904719969362L;
    
    private Integer id;
    /**
     * 类型
     */
    private String type;
    /**
     * 名称
     */
    private String name;
    /**
     * 名称
     */
    private String a;

    /**
     * 名称
     */
    private String b;

    public String getA() {
        return a;
    }

    public Doctor setA(String a) {
        this.a = a;
        return this;
    }

    public String getB() {
        return b;
    }

    public Doctor setB(String b) {
        this.b = b;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}