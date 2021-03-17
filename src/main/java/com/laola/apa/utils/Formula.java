package com.laola.apa.utils;

import ij.IJ;
import ij.measure.CurveFitter;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Formula {
    static Logger logger = Logger.getGlobal();

    /**
     * 二次曲线获得浓度
     *
     * @param absorbanceGap
     * @param xX
     * @param yY
     * @return
     */
    public static float quadratic(double[] xX, double[] yY, float absorbanceGap) {
        float density;//多项式曲线拟合器创建阶数为二的拟合器
        PolynomialCurveFitter polynomialCurveFitter = PolynomialCurveFitter.create(2);
        //权重以及坐标点
        List<WeightedObservedPoint> weightedObservedPoints = new ArrayList<>();
        for (int j = 0; j < xX.length; j++) {
            //遍历xy值，并加入权重坐标点
//            logger.info(xX[j] + "yy:" + yY[j]);
            WeightedObservedPoint weightedObservedPoint = new WeightedObservedPoint(1, xX[j], yY[j]);
            weightedObservedPoints.add(weightedObservedPoint);
        }
        //得出多项式的系数
        double[] doubles = polynomialCurveFitter.fit(weightedObservedPoints);
        //带入吸光度算出浓度
//        logger.info(doubles[0] + "1:" + doubles[1]);
        double a = doubles[2];
        double b = doubles[1];
        double c = doubles[0];
        double d = b * b - 4 * a * (c - absorbanceGap);        //根据b^2-4ac判断方程可解性
        System.out.println(d);
        if (d < 0) {
            density = -500;
        } else if (d == 0) {
            density = (float) (-b / (2 * a));
        } else {
            System.out.println("方程有两个解：" + (-b + Math.sqrt(d)) / (2 * a) + "和" + (-b - Math.sqrt(d)) / (2 * a));
            if (a > 0) {
                density = (float) ((-b + Math.sqrt(d)) / (2 * a));
            }
            if (a < 0) {
                density = (float) ((-b - Math.sqrt(d)) / (2 * a));
            }
        }
        double densityValue = doubles[1] * absorbanceGap + doubles[0];
//        logger.info(densityValue);
        density = Float.parseFloat(new DecimalFormat("0.000").format(densityValue));
        return density;
    }


    /**
     * 一次函数获得浓度
     *
     * @param absorbanceGap
     * @param xX
     * @param yY
     * @return
     */
    public static float getDensityByLinear(float absorbanceGap, double[] xX, double[] yY) {
        float density;//多项式曲线拟合器创建阶数为一的拟合器
        PolynomialCurveFitter polynomialCurveFitter = PolynomialCurveFitter.create(1);
        //权重以及坐标点
        List<WeightedObservedPoint> weightedObservedPoints = new ArrayList<>();
        for (int j = 0; j < xX.length; j++) {
            //遍历xy值，并加入权重坐标点
//            logger.info(xX[j] + "yy:" + yY[j]);
            WeightedObservedPoint weightedObservedPoint = new WeightedObservedPoint(1, xX[j], yY[j]);
            weightedObservedPoints.add(weightedObservedPoint);
        }
        //得出多项式的系数
        double[] doubles = polynomialCurveFitter.fit(weightedObservedPoints);
        //带入吸光度算出浓度
//        logger.info(doubles[0] + "1:" + doubles[1]);
        //2020 1013         double densityValue = doubles[1] * (absorbanceGap - doubles[0]);改成
        double densityValue = (absorbanceGap - doubles[0]) / doubles[1];
//        logger.info(densityValue);
        density = Float.parseFloat(new DecimalFormat("0.000").format(densityValue));
        return density;
    }


    /**
     * 样条曲线获得浓度
     *
     * @param absorbanceGap
     * @param xX
     * @param yY
     * @return
     */
    public static float getSplineDensity(float absorbanceGap, double[] xX, double[] yY) {
        logger.info(String.valueOf(absorbanceGap));
        logger.info(String.valueOf(yY[yY.length - 1]));
        logger.info(String.valueOf(yY[0]));
        if (absorbanceGap < yY[0]) {
            return -501;
        }
        if (absorbanceGap > yY[yY.length - 1]) {
            return -502;
        }
        float density;// 样条曲线
        SplineInterpolator sp = new SplineInterpolator();
        //插入样条值
        PolynomialSplineFunction interpolate = sp.interpolate(xX, yY);
        //取得三次样条多项式
        PolynomialFunction[] polynomials = interpolate.getPolynomials();
//        int n = interpolate.getN();
        //knots is key list
        double[] knots = interpolate.getKnots();
        //v is
        int j = 0;

        for (int i = 0; i < yY.length - 1; i++) {
            if ((absorbanceGap > yY[i] && absorbanceGap < yY[i + 1]) || (absorbanceGap < yY[i] && absorbanceGap > yY[i + 1])) {
                //找到 abs 所在区间
                j = i;
            }
        }
        double densityValue = divideAndConquer(knots[j], xX[j], xX[j + 1], polynomials[j], absorbanceGap);
        density = Float.parseFloat(new DecimalFormat("0.000").format(densityValue));
        return density;
    }
    /**
     * logit4p获得浓度
     *
     * @param absorbanceGap
     * @param x
     * @param y
     * @return float
     */
    public static float RodBard(double[] x, double[] y, float absorbanceGap) {
        CurveFitter curveFitter = new CurveFitter(x, y);
        curveFitter.doFit(7);
        double[] p = curveFitter.getParams();
        double a = Double.parseDouble(IJ.d2s(p[0], 5, 9));
        double b = Double.parseDouble(IJ.d2s(p[1], 5, 9));
        double c = Double.parseDouble(IJ.d2s(p[2], 5, 9));
        double d = Double.parseDouble(IJ.d2s(p[3], 5, 9));

        if (absorbanceGap < logit4p(0,a,b,c,d)) {
            logger.info(absorbanceGap+"   "+logit4p(0,a,b,c,d));
            return -501;
        }

        double densityValue = divideAndConquerRodBard(a, b, c, d, absorbanceGap,x[x.length-1]);


        return Float.parseFloat(new DecimalFormat("0.000").format(densityValue));
    }
    //    logit4p

    public static double logit4p(double x, double a, double b, double c, double d) {
        return d + (a - d) / (1 + Math.pow((x / c), b));
    }
    //    分治法 divideAndConquer

    private static double divideAndConquer(double knode, double minX, double maxX, PolynomialFunction polynomials, float absorbanceGap) {
        //没隔 1 为一个区间
        for (double x = minX; x < maxX; x += 1) {
            double x1 = x - knode;
            double x2 = x1 + 1;
            double cubic = polynomials.value(x1);
//            logit4p(x1, a, b, c, d);
            if (cubic == 0) System.out.println(x + " ");
            else {
                double cubic1 = polynomials.value(x2); //logit4p(x2, a, b, c, d)
                if ((cubic - absorbanceGap) * (cubic1 - absorbanceGap) < 0) {
                    while (x2 - x1 >= 0.001) {
                        double mid = (x2 + x1) / 2;
                        if ((cubic - absorbanceGap) * (polynomials.value(mid) - absorbanceGap) <= 0)
                            x2 = mid; //logit4p(mid, a, b, c, d)
                        else x1 = mid;
                    }
                    return x1 + knode;
                }
            }

        }
        return -503;
    }

    //分治logit4p
    private static double divideAndConquerRodBard(double a, double b, double c, double d, float absorbanceGap, double xMax) {
        //没隔 1 为一个区间
        for (double x = 0; x <= xMax*2; x += 100) {
            double x1 = x;
            double x2 = x1 + 100;
            //取x的值
            double cubic = logit4p(x1, a, b, c, d);
//            logit4p(x1, a, b, c, d);
            if (cubic == absorbanceGap) {
                System.out.println(x1 + " ");
                return x1;
            } else {
                double cubic1 = logit4p(x2, a, b, c, d); //logit4p(x2, a, b, c, d)
                if ((cubic - absorbanceGap) * (cubic1 - absorbanceGap) < 0) {
                    while (x2 - x1 >= 0.0001) {
                        double mid = (x2 + x1) / 2;
                        if ((cubic - absorbanceGap) * (logit4p(mid, a, b, c, d) - absorbanceGap) <= 0){

                            x2 = mid;
                        } else {
                            x1 = mid;
                        }
                    }
                    System.out.println(x1);
                    return x1;
                }
            }

        }
        return -503;
    }

}


