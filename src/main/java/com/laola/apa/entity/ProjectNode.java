package com.laola.apa.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.io.Serializable;

/**
 * (ProjectNode)实体类
 *
 * @author tzhh
 * @since 2021-08-20 10:09:05
 */
public class ProjectNode implements Serializable {
    private static final long serialVersionUID = -52467440830355666L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 项目id
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 标志
     */
    @Column(name = "si")
    private Integer si;
    /**
     * 下位机time
     */
    @Column(name = "t")
    private int t;
    /**
     * rel 创建时间
     */
    private Date ct;
    public ProjectNode() {
    }

    public ProjectNode(Integer projectId, Integer si, Integer t) {
        this.projectId = projectId;
        this.si = si;
        this.t = t;
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

    public Integer getSi() {
        return si;
    }

    public void setSi(Integer si) {
        this.si = si;
    }

    public Integer getT() {
        return t;
    }

    public void setT(Integer t) {
        this.t = t;
    }

    public Date getCt() {
        return ct;
    }

    public void setCt(Date ct) {
        this.ct = ct;
    }

}