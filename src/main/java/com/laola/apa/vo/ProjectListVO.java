package com.laola.apa.vo;

import java.io.Serializable;

public class ProjectListVO implements Serializable {
    private Integer id;

    /**
     * 项目名称
     */
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
