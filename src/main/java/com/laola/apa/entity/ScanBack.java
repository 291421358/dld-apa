package com.laola.apa.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class ScanBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 项目名称
     */
    @Column(name = "rack_no")
    private Integer rackNo;

    /**
     * 中文名称
     */
    @Column(name = "place_no")
    private Integer placeNo;

    /**
     * 计算方法
     */
    @Column(name = "bar_code")
    private String barCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRackNo() {
        return rackNo;
    }

    public void setRackNo(Integer rackNo) {
        this.rackNo = rackNo;
    }

    public Integer getPlaceNo() {
        return placeNo;
    }

    public void setPlaceNo(Integer placeNo) {
        this.placeNo = placeNo;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    @Override
    public String toString() {
        return "ScanBack{" +
                "paramid=" + id +
                ", rackNo=" + rackNo +
                ", placeNo=" + placeNo +
                ", barCode='" + barCode + '\'' +
                '}';
    }
}
