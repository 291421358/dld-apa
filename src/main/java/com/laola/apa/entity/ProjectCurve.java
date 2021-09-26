package com.laola.apa.entity;

import javax.persistence.*;

/**
 * 项目曲线
 */
@Table(name = "laola.project_curve")
public class ProjectCurve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 项目id
     */
    @Column(name = "project_id")
    private Integer projectId;


    /**
     * 项目数据号
     */
    @Column(name = "x")
    private Integer x;

    /**
     * 项目数据
     */
    @Column(name = "y")
    private String y;

    /**
     * 项目数据
     */
    @Column(name = "y_a")
    private String ya;

    /**
     * 项目数据
     */
    @Column(name = "t")
    private Integer t;

    /**
     * 项目数据
     */
    @Column(name = "creattime")
    private String creattime;


    public String getYa() {
        return ya;
    }

    public ProjectCurve setYa(String ya) {
        this.ya = ya;
        return this;
    }

    public Integer getT() {
        return t;
    }

    public ProjectCurve setT(Integer t) {
        this.t = t;
        return this;
    }

    public String getCreattime() {
        return creattime;
    }

    public ProjectCurve setCreattime(String creattime) {
        this.creattime = creattime;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "ProjectCurve{" +
                "paramid=" + id +
                ", projectId=" + projectId +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}