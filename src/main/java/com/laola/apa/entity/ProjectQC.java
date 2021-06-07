package com.laola.apa.entity;

import javax.persistence.*;

@Table(name = "laola.project")
public class ProjectQC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 项目开始时间
     */
    @Column(name = "starttime")
    private String starttime;

    /**
     * 项目结束时间
     */
    @Column(name = "endtime")
    private String endtime;

    /**
     * 杯号
     */
    @Column(name = "cup_number")
    private String cup_number;

    /**
     * 项目序号
     */
    @Column(name = "project_num")
    private Integer project_num;

    /**
     * 项目参数id
     */
    @Column(name = "project_param_id")
    private Integer project_param_id;

    /**
     * 病人code
     *
     * @return
     */
    @Column(name = "human_code")
    private Integer human_code;

    /**
     * 位号place_no
     *
     * @return
     */
    @Column(name = "place_no")
    private Integer place_no;

    /**
     * 架号rack_no
     *
     * @return
     */
    @Column(name = "rack_no")
    private Integer rack_no;

    /**
     * 条形码 bar_code
     *
     * @return
     */
    @Column(name = "bar_code")
    private String bar_code;

    /**
     * 类型 1普通，2定标，3质控
     * @return
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 定标因子 bar_code
     *
     * @return
     */
    @Column(name = "factor")
    private String factor;

    /**
     * 浓度 density
     *
     * @return
     */
    @Column(name = "density")
    private String density;


    /**
     * 浓度 吸光度
     *
     * @return
     */
    @Column(name = "absorbance")
    private String absorbance;

    /**
     * 异常
     *
     * @return
     */
    @Column(name = "abnormal")
    private Integer abnormal;


    /**
     * 浓度 吸光度
     *
     * @return
     */
    @Column(name = "absorbance_low")
    private Integer absorbance_low;


    /**
     * 浓度 吸光度
     *
     * @return
     */
    @Column(name = "absorbance_height")
    private Integer absorbance_height;

    public Integer getAbsorbance_low() {
        return absorbance_low;
    }

    public void setAbsorbance_low(Integer absorbance_low) {
        this.absorbance_low = absorbance_low;
    }

    public Integer getAbsorbance_height() {
        return absorbance_height;
    }

    public void setAbsorbance_height(Integer absorbance_height) {
        this.absorbance_height = absorbance_height;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
    }

    public String getDensity() {
        return density;
    }

    public void setDensity(String density) {
        this.density = density;
    }

    public Integer getPlace_no() {
        return place_no;
    }

    public void setPlace_no(Integer place_no) {
        this.place_no = place_no;
    }

    public Integer getRack_no() {
        return rack_no;
    }

    public void setRack_no(Integer rack_no) {
        this.rack_no = rack_no;
    }

    public String getBar_code() {
        return bar_code;
    }

    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
    }

    public Integer getHuman_code() {
        return human_code;
    }

    public void setHuman_code(Integer human_code) {
        this.human_code = human_code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getCup_number() {
        return cup_number;
    }

    public void setCup_number(String cup_number) {
        this.cup_number = cup_number;
    }

    public Integer getProject_num() {
        return project_num;
    }

    public void setProject_num(Integer project_num) {
        this.project_num = project_num;
    }

    public Integer getProject_param_id() {
        return project_param_id;
    }

    public void setProject_param_id(Integer project_param_id) {
        this.project_param_id = project_param_id;
    }

    public String getAbsorbance() {
        return absorbance;
    }

    public void setAbsorbance(String absorbance) {
        this.absorbance = absorbance;
    }

    public Integer getAbnormal() {
        return abnormal;
    }

    public void setAbnormal(Integer abnormal) {
        this.abnormal = abnormal;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", starttime='" + starttime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", cup_number='" + cup_number + '\'' +
                ", project_num=" + project_num +
                ", project_param_id=" + project_param_id +
                ", human_code=" + human_code +
                ", place_no=" + place_no +
                ", rack_no=" + rack_no +
                ", bar_code=" + bar_code +
                ", type=" + type +
                ", factor='" + factor + '\'' +
                ", density='" + density + '\'' +
                ", absorbance='" + absorbance + '\'' +
                ", abnormal=" + abnormal +
                ", absorbance_low=" + absorbance_low +
                ", absorbance_height=" + absorbance_height +
                '}';
    }
}