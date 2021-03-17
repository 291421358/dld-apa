package com.laola.apa.entity;

import java.io.Serializable;

/**
 * (EquipmentState)实体类
 *
 * @author tzhh
 * @since 2020-06-02 13:37:39
 */
public class EquipmentState implements Serializable {
    private static final long serialVersionUID = 726379449152597495L;

    private Integer id;

    private String temp;

    private Integer rackNo;

    private Integer placeNo;

    private Integer pureWater;

    private Integer wasteWater;

    private Integer firingPin;

    private String reactTemp;

    private String regentTemp;

    private Integer numSent;

    private Integer numUnderTest;

    private Integer numAll;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
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

    public Integer getPureWater() {
        return pureWater;
    }

    public void setPureWater(Integer pureWater) {
        this.pureWater = pureWater;
    }

    public Integer getWasteWater() {
        return wasteWater;
    }

    public void setWasteWater(Integer wasteWater) {
        this.wasteWater = wasteWater;
    }

    public Integer getFiringPin() {
        return firingPin;
    }

    public void setFiringPin(Integer firingPin) {
        this.firingPin = firingPin;
    }

    public String getReactTemp() {
        return reactTemp;
    }

    public void setReactTemp(String reactTemp) {
        this.reactTemp = reactTemp;
    }

    public String getRegentTemp() {
        return regentTemp;
    }

    public void setRegentTemp(String regentTemp) {
        this.regentTemp = regentTemp;
    }

    public Integer getNumSent() {
        return numSent;
    }

    public void setNumSent(Integer numSent) {
        this.numSent = numSent;
    }

    public Integer getNumUnderTest() {
        return numUnderTest;
    }

    public void setNumUnderTest(Integer numUnderTest) {
        this.numUnderTest = numUnderTest;
    }

    public Integer getNumAll() {
        return numAll;
    }

    public void setNumAll(Integer numAll) {
        this.numAll = numAll;
    }
    public EquipmentState() {
    }

    public EquipmentState(Integer id, Integer pureWater, Integer wasteWater, Integer firingPin, String reactTemp, String regentTemp, Integer numSent, Integer numUnderTest, Integer numAll) {
        this.id = id;
        this.pureWater = pureWater;
        this.wasteWater = wasteWater;
        this.firingPin = firingPin;
        this.reactTemp = reactTemp;
        this.regentTemp = regentTemp;
        this.numSent = numSent;
        this.numUnderTest = numUnderTest;
        this.numAll = numAll;
    }

    public EquipmentState(Integer id, Integer rackNo, Integer placeNo) {
        this.id = id;
        this.rackNo = rackNo;
        this.placeNo = placeNo;
    }

    @Override
    public String toString() {
        return "EquipmentState{" +
                "id=" + id +
                ", temp='" + temp + '\'' +
                ", rackNo=" + rackNo +
                ", placeNo=" + placeNo +
                ", pureWater=" + pureWater +
                ", wasteWater=" + wasteWater +
                ", firingPin=" + firingPin +
                ", reactTemp='" + reactTemp + '\'' +
                ", regentTemp='" + regentTemp + '\'' +
                ", numSent=" + numSent +
                ", numUnderTest=" + numUnderTest +
                ", numAll=" + numAll +
                '}';
    }
}