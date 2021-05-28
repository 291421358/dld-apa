package com.laola.apa.entity;

import com.laola.apa.utils.DataUtil;
import com.laola.apa.utils.DateUtils;

import java.io.Serializable;

/**
 * (Scaling)实体类
 *
 * @author tzhh
 * @since 2020-05-28 17:13:50
 */
public class Scaling implements Serializable {
    private static final long serialVersionUID = -70569154632839822L;

    /**
     * 定标id/年-月-日 时:分
     */
    private String dateid;

    /**
     * 定标算法
     */
    private String algorithm;


    public String getDateid() {
        return dateid;
    }

    public void setDateid(String dateid) {
        this.dateid = dateid;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String toString() {
        return "Scaling{" +
                "dateid='" + dateid + '\'' +
                ", algorithm='" + algorithm + '\'' +
                '}';
    }

    public Scaling() {
        this.dateid = DataUtil.now("yyyy-MM-dd HH:mm");
    }

    public Scaling(String dateid, String algorithm) {
        this.dateid = dateid;
        this.algorithm = algorithm;
    }
}