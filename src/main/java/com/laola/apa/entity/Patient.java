package com.laola.apa.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.Serializable;

/**
 * (Patient)实体类
 *
 * @author tzhh
 * @since 2020-05-06 16:24:01
 */
public class Patient implements Serializable {
    private static final long serialVersionUID = 356771050812376341L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pid;

    private Integer id;
    
    private String code;

    private String name;
    private String sex;

    private String inpatientArea;
    
    private Integer bedNum;
    
    private Integer sampleNum;
    
    private String inspectionDept;
    
    private String inspectionDoc;
    
    private String inspectionDate;
    
    private String testDoc;
    
    private String testDate;
    
    private String examineDoc;

    private String remark;
    private String sampleType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getInpatientArea() {
        return inpatientArea;
    }

    public void setInpatientArea(String inpatientArea) {
        this.inpatientArea = inpatientArea;
    }

    public Integer getBedNum() {
        return bedNum;
    }

    public void setBedNum(Integer bedNum) {
        this.bedNum = bedNum;
    }

    public Integer getSampleNum() {
        return sampleNum;
    }

    public void setSampleNum(Integer sampleNum) {
        this.sampleNum = sampleNum;
    }

    public String getInspectionDept() {
        return inspectionDept;
    }

    public void setInspectionDept(String inspectionDept) {
        this.inspectionDept = inspectionDept;
    }

    public String getInspectionDoc() {
        return inspectionDoc;
    }

    public void setInspectionDoc(String inspectionDoc) {
        this.inspectionDoc = inspectionDoc;
    }

    public String getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(String inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public String getTestDoc() {
        return testDoc;
    }

    public void setTestDoc(String testDoc) {
        this.testDoc = testDoc;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public String getExamineDoc() {
        return examineDoc;
    }

    public void setExamineDoc(String examineDoc) {
        this.examineDoc = examineDoc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Patient(Integer id, String code) {
        this.id = id;
        this.code = code;
        this.testDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    }

    public Patient() {
        this.testDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "pid=" + pid +
                ", id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", inpatientArea='" + inpatientArea + '\'' +
                ", bedNum=" + bedNum +
                ", sampleNum=" + sampleNum +
                ", inspectionDept='" + inspectionDept + '\'' +
                ", inspectionDoc='" + inspectionDoc + '\'' +
                ", inspectionDate='" + inspectionDate + '\'' +
                ", testDoc='" + testDoc + '\'' +
                ", testDate='" + testDate + '\'' +
                ", examineDoc='" + examineDoc + '\'' +
                ", remark='" + remark + '\'' +
                ", sampleType='" + sampleType + '\'' +
                '}';
    }

    public static void main(String[] args) {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }
}