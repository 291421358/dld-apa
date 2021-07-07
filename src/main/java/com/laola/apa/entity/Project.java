package com.laola.apa.entity;

import javax.persistence.*;

@Table(name = "laola.project")
public class Project {
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
    private String cupNumber;

    /**
     * 项目序号
     */
    @Column(name = "project_num")
    private Integer projectNum;

    /**
     * 项目参数id
     */
    @Column(name = "project_param_id")
    private Integer projectParamId;

    /**
     * 病人code
     *
     * @return
     */
    @Column(name = "human_code")
    private Integer humanCode;

    /**
     * 位号place_no
     *
     * @return
     */
    @Column(name = "place_no")
    private Integer placeNo;

    /**
     * 架号rack_no
     *
     * @return
     */
    @Column(name = "rack_no")
    private Integer rackNo;

    /**
     * 条形码 bar_code
     *
     * @return
     */
    @Column(name = "bar_code")
    private String barCode;

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
    private Integer absorbanceLow;




    /**
     * 浓度 吸光度
     *
     * @return
     */
    @Column(name = "absorbance_height")
    private Integer absorbanceHeight;


    /**
     *  a 急诊
     *
     * @return
     */
    @Column(name = "a")
    private Integer a;

    /**
     * b
     *
     * @return
     */
    @Column(name = "b")
    private Integer b;


    public Integer getA() {
        return a;
    }

    public Project setA(Integer a) {
        this.a = a;
        return this;
    }

    public Integer getB() {
        return b;
    }

    public Project setB(Integer b) {
        this.b = b;
        return this;
    }

    public Integer getAbsorbanceLow() {
        return absorbanceLow;
    }

    public void setAbsorbanceLow(Integer absorbanceLow) {
        this.absorbanceLow = absorbanceLow;
    }

    public Integer getAbsorbanceHeight() {
        return absorbanceHeight;
    }

    public void setAbsorbanceHeight(Integer absorbanceHeight) {
        this.absorbanceHeight = absorbanceHeight;
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

    public Integer getPlaceNo() {
        return placeNo;
    }

    public void setPlaceNo(Integer placeNo) {
        this.placeNo = placeNo;
    }

    public Integer getRackNo() {
        return rackNo;
    }

    public void setRackNo(Integer rackNo) {
        this.rackNo = rackNo;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Integer getHumanCode() {
        return humanCode;
    }

    public void setHumanCode(Integer humanCode) {
        this.humanCode = humanCode;
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

    public String getCupNumber() {
        return cupNumber;
    }

    public void setCupNumber(String cupNumber) {
        this.cupNumber = cupNumber;
    }

    public Integer getProjectNum() {
        return projectNum;
    }

    public void setProjectNum(Integer projectNum) {
        this.projectNum = projectNum;
    }

    public Integer getProjectParamId() {
        return projectParamId;
    }

    public void setProjectParamId(Integer projectParamId) {
        this.projectParamId = projectParamId;
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
                ", cupNumber='" + cupNumber + '\'' +
                ", projectNum=" + projectNum +
                ", projectParamId=" + projectParamId +
                ", humanCode=" + humanCode +
                ", placeNo=" + placeNo +
                ", rackNo=" + rackNo +
                ", barCode='" + barCode + '\'' +
                ", type=" + type +
                ", factor='" + factor + '\'' +
                ", density='" + density + '\'' +
                ", absorbance='" + absorbance + '\'' +
                ", abnormal=" + abnormal +
                ", absorbanceLow=" + absorbanceLow +
                ", absorbanceHeight=" + absorbanceHeight +
                ", a=" + a +
                ", b=" + b +
                '}';
    }
}