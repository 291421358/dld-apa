package com.laola.apa.utils;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: T ZHH
 * Date: 2021/6/15
 * Time: 17:57
 * To change this template use File | Settings | File Templates.
 */
public class BigMath {

    /**
     * 解决java双精度计算丢失精度问题
     * 加法
     * @param a
     * @param b
     * @return
     */
    public static double add(double a,double b)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(a));
        BigDecimal b2 = new BigDecimal(Double.toString(b));
        return b1.add(b2).doubleValue();
    }


// 减法 A - B
    /**
     *mul(a,b) 大致为 a - b;
     * @param a 减数
     * @param b 被减数
     * @return a - b
     */
    public static double sub(double a,double b)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(a));
        BigDecimal b2 = new BigDecimal(Double.toString(b));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 乘法
     * @param a
     * @param b
     * @return
     */
    public static double mul(double a,double b)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(a));
        BigDecimal b2 = new BigDecimal(Double.toString(b));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 除法，
     * @param a 除数
     * @param b 被除数
     * @param scale 小数点位数
     * @return a/b 结果保留 scale位小数
     */
    public static double div(double a,double b,int scale)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(a));
        BigDecimal b2 = new BigDecimal(Double.toString(b));

        return b1.divide(b2, scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
