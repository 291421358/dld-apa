package com.laola.apa.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * (UsedCode)实体类
 *
 * @author tzhh
 * @since 2021-02-23 11:29:57
 */
public class UsedCode implements Serializable {
    private static final long serialVersionUID = -93011058775390375L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String code;
    
    private Integer count;
    
    private Integer total;
    
    private Integer count2;
    
    private Integer total2;


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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCount2() {
        return count2;
    }

    public void setCount2(Integer count2) {
        this.count2 = count2;
    }

    public Integer getTotal2() {
        return total2;
    }

    public void setTotal2(Integer total2) {
        this.total2 = total2;
    }

}