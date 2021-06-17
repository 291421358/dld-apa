package com.laola.apa.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "laola.project_param")
public class ProjectParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 中文名称
     */
    @Column(name = "chinese_name")
    private String chineseName;

    /**
     * 计算方法
     */
    @Column(name = "compute_method")
    private String computeMethod;

    /**
     * 样本量（ul）
     */
    @Column(name = "samplesize")
    private String sampleSize;

    /**
     * 样本种类
     */
    @Column(name = "sampletype")
    private String sampleType;

    /**
     * 一试剂量（ul）
     */
    @Column(name = "reagent_quantity_no1")
    private String reagentQuantityNo1;

    /**
     * 二试剂量（ul）
     */
    @Column(name = "reagent_quantity_no2")
    private String reagentQuantityNo2;

    /**
     * 主波长(nm)
     */
    @Column(name = "main_wavelength")
    private String mainWavelength;

    /**
     * 辅波长(nm)
     */
    @Column(name = "auxiliary_wavelength")
    private String auxiliaryWavelength;

    /**
     * 小数位数
     */
    @Column(name = "decimal_digit")
    private String decimalDigit;

    /**
     * 计量单位
     */
    @Column(name = "meterage_unit")
    private String meterageUnit;

    /**
     * 最小吸光度*10(A)
     */
    @Column(name = "min_absorbance")
    private String minAbsorbance;

    /**
     * 最大吸光度*10(A)
     */
    @Column(name = "max_absorbance")
    private String maxAbsorbance;

    /**
     * 稀释倍数
     */
    @Column(name = "dilution_multiple")
    private String dilutionMultiple;

    /**
     * 稀释液位置
     */
    @Column(name = "dilution_position")
    private String dilutionPosition;

    /**
     * 主读数段始点
     */
    @Column(name = "main_indication_begin")
    private String mainIndicationBegin;

    /**
     * 主读数段终点
     */
    @Column(name = "main_indication_end")
    private String mainIndicationEnd;

    /**
     * 辅读数段始点
     */
    @Column(name = "auxiliary_indication_begin")
    private String auxiliaryIndicationBegin;

    /**
     * 辅读数段终点
     */
    @Column(name = "`auxiliary_indication_end`")
    private String auxiliaryIndicationEnd;

    /**
     * 正常低值*100


     */
    @Column(name = "normal_low")
    private String normalLow;

    /**
     * 正常高值*100

     */
    @Column(name = "normal_high")
    private String normalHigh;

    /**
     * 修正公式A
     */
    @Column(name = "modified_formula_A")
    private String modifiedFormulaA;

    /**
     * 修正公式B
     */
    @Column(name = "modified_formula_B")
    private String modifiedFormulaB;

    /**
     * 其他修正公式A
     */
    @Column(name = "`other_ modified_formula_A`")
    private String otherModifiedFormulaA;

    /**
     * 其他修正公式B
     */
    @Column(name = "`other_ modified_formula_B`")
    private String otherModifiedFormulaB;

    /**
     * 稀释延后周期
     */
    @Column(name = "`dilution_delay_ period`")
    private String dilutionDelayPeriod;

    /**
     * 稀释延后周期
     */
    @Column(name = "factor")
    private String factor;


    /**
     * 标准浓度低
     */
    @Column(name = "preset_density_low")
    private String preset_density_low;

     /**
     * 标准浓度中
     */
     @OneToOne()
     @JoinColumn(name = "preset_density_mid")
    private String preset_density_mid;

    /**
     * 标准浓度高
     */
    @Column(name = "preset_density_hight")
    private String preset_density_hight;

    /**
     * 稀释位置
     */
    @Column(name = "diluent_place")
    private String diluent_place;

    /**
     * 稀释量
     */
    @Column(name = "`diluent_size`")
    private String diluent_size;

    /**
     * 稀释样本量
     */
    @Column(name = "dilution_sample_size")
    private String dilution_sample_size;





    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * @return paramid
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取项目名称
     *
     * @return name - 项目名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置项目名称
     *
     * @param name 项目名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取中文名称
     *
     * @return chinese_name - 中文名称
     */
    public String getChineseName() {
        return chineseName;
    }

    /**
     * 设置中文名称
     *
     * @param chineseName 中文名称
     */
    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    /**
     * 获取计算方法
     *
     * @return compute_method - 计算方法
     */
    public String getComputeMethod() {
        return computeMethod;
    }

    /**
     * 设置计算方法
     *
     * @param computeMethod 计算方法
     */
    public void setComputeMethod(String computeMethod) {
        this.computeMethod = computeMethod;
    }

    /**
     * 获取样本量（ul）
     *
     * @return sampleSize - 样本量（ul）
     */
    public String getSampleSize() {
        return sampleSize;
    }

    /**
     * 设置样本量（ul）
     *
     * @param sampleSize 样本量（ul）
     */
    public void setSampleSize(String sampleSize) {
        this.sampleSize = sampleSize;
    }

    /**
     * 获取样本种类
     *
     * @return sampleType - 样本种类
     */
    public String getSampleType() {
        return sampleType;
    }

    /**
     * 设置样本种类
     *
     * @param sampleType 样本种类
     */
    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    /**
     * 获取一试剂量（ul）
     *
     * @return reagent_quantity_no1 - 一试剂量（ul）
     */
    public String getReagentQuantityNo1() {
        return reagentQuantityNo1;
    }

    /**
     * 设置一试剂量（ul）
     *
     * @param reagentQuantityNo1 一试剂量（ul）
     */
    public void setReagentQuantityNo1(String reagentQuantityNo1) {
        this.reagentQuantityNo1 = reagentQuantityNo1;
    }

    /**
     * 获取二试剂量（ul）
     *
     * @return reagent_quantity_no2 - 二试剂量（ul）
     */
    public String getReagentQuantityNo2() {
        return reagentQuantityNo2;
    }

    /**
     * 设置二试剂量（ul）
     *
     * @param reagentQuantityNo2 二试剂量（ul）
     */
    public void setReagentQuantityNo2(String reagentQuantityNo2) {
        this.reagentQuantityNo2 = reagentQuantityNo2;
    }

    /**
     * 获取主波长(nm)
     *
     * @return main_wavelength - 主波长(nm)
     */
    public String getMainWavelength() {
        return mainWavelength;
    }

    /**
     * 设置主波长(nm)
     *
     * @param mainWavelength 主波长(nm)
     */
    public void setMainWavelength(String mainWavelength) {
        this.mainWavelength = mainWavelength;
    }

    /**
     * 获取辅波长(nm)
     *
     * @return auxiliary_wavelength - 辅波长(nm)
     */
    public String getAuxiliaryWavelength() {
        return auxiliaryWavelength;
    }

    /**
     * 设置辅波长(nm)
     *
     * @param auxiliaryWavelength 辅波长(nm)
     */
    public void setAuxiliaryWavelength(String auxiliaryWavelength) {
        this.auxiliaryWavelength = auxiliaryWavelength;
    }

    /**
     * 获取小数位数
     *
     * @return decimal_digit - 小数位数
     */
    public String getDecimalDigit() {
        return decimalDigit;
    }

    /**
     * 设置小数位数
     *
     * @param decimalDigit 小数位数
     */
    public void setDecimalDigit(String decimalDigit) {
        this.decimalDigit = decimalDigit;
    }

    /**
     * 获取计量单位
     *
     * @return meterage_unit - 计量单位
     */
    public String getMeterageUnit() {
        return meterageUnit;
    }

    /**
     * 设置计量单位
     *
     * @param meterageUnit 计量单位
     */
    public void setMeterageUnit(String meterageUnit) {
        this.meterageUnit = meterageUnit;
    }

    /**
     * 获取最小吸光度*10(A)
     *
     * @return min_absorbance - 最小吸光度*10(A)
     */
    public String getMinAbsorbance() {
        return minAbsorbance;
    }

    /**
     * 设置最小吸光度*10(A)
     *
     * @param minAbsorbance 最小吸光度*10(A)
     */
    public void setMinAbsorbance(String minAbsorbance) {
        this.minAbsorbance = minAbsorbance;
    }

    /**
     * 获取最大吸光度*10(A)
     *
     * @return max_absorbance - 最大吸光度*10(A)
     */
    public String getMaxAbsorbance() {
        return maxAbsorbance;
    }

    /**
     * 设置最大吸光度*10(A)
     *
     * @param maxAbsorbance 最大吸光度*10(A)
     */
    public void setMaxAbsorbance(String maxAbsorbance) {
        this.maxAbsorbance = maxAbsorbance;
    }

    /**
     * 获取稀释倍数
     *
     * @return dilution_multiple - 稀释倍数
     */
    public String getDilutionMultiple() {
        return dilutionMultiple;
    }

    /**
     * 设置稀释倍数
     *
     * @param dilutionMultiple 稀释倍数
     */
    public void setDilutionMultiple(String dilutionMultiple) {
        this.dilutionMultiple = dilutionMultiple;
    }

    /**
     * 获取稀释液位置
     *
     * @return dilution_position - 稀释液位置
     */
    public String getDilutionPosition() {
        return dilutionPosition;
    }

    /**
     * 设置稀释液位置
     *
     * @param dilutionPosition 稀释液位置
     */
    public void setDilutionPosition(String dilutionPosition) {
        this.dilutionPosition = dilutionPosition;
    }

    /**
     * 获取主读数段始点
     *
     * @return main_indication_begin - 主读数段始点
     */
    public String getMainIndicationBegin() {
        return mainIndicationBegin;
    }

    /**
     * 设置主读数段始点
     *
     * @param mainIndicationBegin 主读数段始点
     */
    public void setMainIndicationBegin(String mainIndicationBegin) {
        this.mainIndicationBegin = mainIndicationBegin;
    }

    /**
     * 获取主读数段终点
     *
     * @return main_indication_end - 主读数段终点
     */
    public String getMainIndicationEnd() {
        return mainIndicationEnd;
    }

    /**
     * 设置主读数段终点
     *
     * @param mainIndicationEnd 主读数段终点
     */
    public void setMainIndicationEnd(String mainIndicationEnd) {
        this.mainIndicationEnd = mainIndicationEnd;
    }

    /**
     * 获取辅读数段始点
     *
     * @return auxiliary_indication_begin - 辅读数段始点
     */
    public String getAuxiliaryIndicationBegin() {
        return auxiliaryIndicationBegin;
    }

    /**
     * 设置辅读数段始点
     *
     * @param auxiliaryIndicationBegin 辅读数段始点
     */
    public void setAuxiliaryIndicationBegin(String auxiliaryIndicationBegin) {
        this.auxiliaryIndicationBegin = auxiliaryIndicationBegin;
    }

    /**
     * 获取辅读数段终点
     *
     * @return auxiliary_indication_end - 辅读数段终点
     */
    public String getAuxiliaryIndicationEnd() {
        return auxiliaryIndicationEnd;
    }

    /**
     * 设置辅读数段终点
     *
     * @param auxiliaryIndicationEnd 辅读数段终点
     */
    public void setAuxiliaryIndicationEnd(String auxiliaryIndicationEnd) {
        this.auxiliaryIndicationEnd = auxiliaryIndicationEnd;
    }

    /**
     * 获取正常低值*100
     * @return normal_low - 正常低值*100
     */
    public String getNormalLow() {
        return normalLow;
    }

    /**
     * 设置正常低值*100
     * @param normalLow 正常低值*100
     */
    public void setNormalLow(String normalLow) {
        this.normalLow = normalLow;
    }

    /**
     * 获取正常高值*100
     * @return normal_high - 正常高值*100
     */
    public String getNormalHigh() {
        return normalHigh;
    }

    /**
     * 设置正常高值*100
     * @param normalHigh 正常高值*100
     */
    public void setNormalHigh(String normalHigh) {
        this.normalHigh = normalHigh;
    }

    /**
     * 获取修正公式A
     * @return modified_formula_A - 修正公式A
     */
    public String getModifiedFormulaA() {
        return modifiedFormulaA;
    }

    /**
     * 设置修正公式A
     * @param modifiedFormulaA 修正公式A
     */
    public void setModifiedFormulaA(String modifiedFormulaA) {
        this.modifiedFormulaA = modifiedFormulaA;
    }

    /**
     * 获取修正公式B
     * @return modified_formula_B - 修正公式B
     */
    public String getModifiedFormulaB() {
        return modifiedFormulaB;
    }

    /**
     * 设置修正公式B
     * @param modifiedFormulaB 修正公式B
     */
    public void setModifiedFormulaB(String modifiedFormulaB) {
        this.modifiedFormulaB = modifiedFormulaB;
    }

    /**
     * 获取其他修正公式A
     * @return other_ modified_formula_A - 其他修正公式A
     */
    public String getOtherModifiedFormulaA() {
        return otherModifiedFormulaA;
    }

    /**
     * 设置其他修正公式A
     * @param otherModifiedFormulaA 其他修正公式A
     */
    public void setOtherModifiedFormulaA(String otherModifiedFormulaA) {
        this.otherModifiedFormulaA = otherModifiedFormulaA;
    }

    /**
     * 获取其他修正公式B
     * @return other_ modified_formula_B - 其他修正公式B
     */
    public String getOtherModifiedFormulaB() {
        return otherModifiedFormulaB;
    }

    /**
     * 设置其他修正公式B
     * @param otherModifiedFormulaB 其他修正公式B
     */
    public void setOtherModifiedFormulaB(String otherModifiedFormulaB) {
        this.otherModifiedFormulaB = otherModifiedFormulaB;
    }

    /**
     * 获取稀释延后周期
     * @return dilution_delay_ period - 稀释延后周期
     */
    public String getDilutionDelayPeriod() {
        return dilutionDelayPeriod;
    }

    /**
     * 设置稀释延后周期
     * @param dilutionDelayPeriod 稀释延后周期
     */
    public void setDilutionDelayPeriod(String dilutionDelayPeriod) {
        this.dilutionDelayPeriod = dilutionDelayPeriod;
    }

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
    }

    public String getPresetDensityLow() {
        return preset_density_low;
    }

    public void setPreset_density_low(String preset_density_low) {
        this.preset_density_low = preset_density_low;
    }

    public String getPresetDensityHight() {
        return preset_density_hight;
    }

    public void setPresetDensityHight(String presetDensityHight) {
        this.preset_density_hight = presetDensityHight;
    }

    public String getPresetDensityMid() {
        return preset_density_mid;
    }

    public void setPreset_density_mid(String preset_density_mid) {
        this.preset_density_mid = preset_density_mid;
    }

    /**
     * 获取创建时间
     * @return createtime - 创建时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置创建时间
     *
     * @param createtime 创建时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }


    public String getPreset_density_low() {
        return preset_density_low;
    }

    public String getPreset_density_mid() {
        return preset_density_mid;
    }

    public String getPreset_density_hight() {
        return preset_density_hight;
    }

    public ProjectParam setPreset_density_hight(String preset_density_hight) {
        this.preset_density_hight = preset_density_hight;
        return this;
    }

    public String getDiluent_place() {
        return diluent_place;
    }

    public ProjectParam setDiluent_place(String diluent_place) {
        this.diluent_place = diluent_place;
        return this;
    }

    public String getDiluent_size() {
        return diluent_size;
    }

    public ProjectParam setDiluent_size(String diluent_size) {
        this.diluent_size = diluent_size;
        return this;
    }

    public String getDilution_sample_size() {
        return dilution_sample_size;
    }

    public ProjectParam setDilution_sample_size(String dilution_sample_size) {
        this.dilution_sample_size = dilution_sample_size;
        return this;
    }

    @Override
    public String toString() {
        return "ProjectParam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", chineseName='" + chineseName + '\'' +
                ", computeMethod='" + computeMethod + '\'' +
                ", sampleSize='" + sampleSize + '\'' +
                ", sampleType='" + sampleType + '\'' +
                ", reagentQuantityNo1='" + reagentQuantityNo1 + '\'' +
                ", reagentQuantityNo2='" + reagentQuantityNo2 + '\'' +
                ", mainWavelength='" + mainWavelength + '\'' +
                ", auxiliaryWavelength='" + auxiliaryWavelength + '\'' +
                ", decimalDigit='" + decimalDigit + '\'' +
                ", meterageUnit='" + meterageUnit + '\'' +
                ", minAbsorbance='" + minAbsorbance + '\'' +
                ", maxAbsorbance='" + maxAbsorbance + '\'' +
                ", dilutionMultiple='" + dilutionMultiple + '\'' +
                ", dilutionPosition='" + dilutionPosition + '\'' +
                ", mainIndicationBegin='" + mainIndicationBegin + '\'' +
                ", mainIndicationEnd='" + mainIndicationEnd + '\'' +
                ", auxiliaryIndicationBegin='" + auxiliaryIndicationBegin + '\'' +
                ", auxiliaryIndicationEnd='" + auxiliaryIndicationEnd + '\'' +
                ", normalLow='" + normalLow + '\'' +
                ", normalHigh='" + normalHigh + '\'' +
                ", modifiedFormulaA='" + modifiedFormulaA + '\'' +
                ", modifiedFormulaB='" + modifiedFormulaB + '\'' +
                ", otherModifiedFormulaA='" + otherModifiedFormulaA + '\'' +
                ", otherModifiedFormulaB='" + otherModifiedFormulaB + '\'' +
                ", dilutionDelayPeriod='" + dilutionDelayPeriod + '\'' +
                ", factor='" + factor + '\'' +
                ", presetDensityLow='" + preset_density_low + '\'' +
                ", presetDensityMid='" + preset_density_mid + '\'' +
                ", presetDensityHight='" + preset_density_hight + '\'' +
                ", diluentPlace='" + diluent_place + '\'' +
                ", diluentSize='" + diluent_size + '\'' +
                ", dilutionSampleSize='" + dilution_sample_size + '\'' +
                ", createtime=" + createtime +
                '}';
    }
}