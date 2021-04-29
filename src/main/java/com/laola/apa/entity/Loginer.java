package com.laola.apa.entity;

import java.io.Serializable;

/**
 * (Loginer)实体类
 *
 * @author tzhh
 * @since 2021-04-29 16:09:11
 */
public class Loginer implements Serializable {
    private static final long serialVersionUID = -23819997828211659L;
    
    private Integer id;
    
    private String u;
    
    private String p;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

}