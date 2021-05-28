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
    /**
     * 用户名
     */
    private String u;
    /**
     * 密码
     */
    private String p;
    /**
     * 用户类型
     */
    private String t;

    public String getT() {
        return t;
    }

    public Loginer setT(String t) {
        this.t = t;
        return this;
    }

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

    @Override
    public String toString() {
        return "Loginer{" +
                "id=" + id +
                ", u='" + u + '\'' +
                ", p='" + p + '\'' +
                ", t='" + t + '\'' +
                '}';
    }
}