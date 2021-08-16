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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Formula {
    private static Logger logger = Logger.getGlobal();
    private static String yStep = "0.0";

    public static float getDensity(float absorbanceGap, double[] xX, double[] yY, String algorithm, double[] calDen, double[] calAbs) {


        if (yY[yY.length - 1] - yY[0] < 1) {
            yStep = "0.000";
        }
        if (yY[yY.length - 1] - yY[0] < 0.1) {
            yStep = "0.0000";
        }
        if (yY[yY.length - 1] - yY[0] < 0.01) {
            yStep = "0.00000";
        }
        if (yY[yY.length - 1] - yY[0] < 0.001) {
            yStep = "0.000000";
        }



        float density = 0;
        if (xX.length < 3 && "三次样条函数".equals(algorithm)) {
            algorithm = "一次曲线";
        }
        if ("三次样条函数".equals(algorithm)) {
            logger.info("三次样条");
            density = Formula.getSplineDensity(absorbanceGap, xX, yY,calDen,calAbs);

        }
        if ("一次曲线".equals(algorithm)) {
            logger.info("一次曲线");
            density = Formula.getDensityByLinear(absorbanceGap, xX, yY,calDen,calAbs);
//                        logger.info(density);
        }
        if ("二次曲线拟合".equals(algorithm)) {
            logger.info("二次曲线拟合");
            //二次曲线拟合
            density = Formula.quadratic(xX, yY, absorbanceGap,calDen,calAbs);
        }

        if ("RodBard".equals(algorithm)) {
            density = Formula.RodBard(xX, yY, absorbanceGap,calDen,calAbs);
            logger.info(String.valueOf(density));
        }

        if ("三次曲线拟合".equals(algorithm)) {
            density = Formula.cubicCurveFitting(xX, yY, absorbanceGap,calDen,calAbs);
            logger.info(String.valueOf(density));
        }
        return density;
    }

    /**
     * @apiNote 二次曲线获得浓度
     * @author tzhh
     * @date 2021/5/27 17:46
     * @param x
	 * @param y
	 * @param absorbanceGap
     * @param calDen
     * @param calAbs
     * @return {@link float}
     **/
    public static float quadratic(double[] x, double[] y, float absorbanceGap, double[] calDen, double[] calAbs) {
        double density = 0;//多项式曲线拟合器创建阶数为二的拟合器
        PolynomialCurveFitter polynomialCurveFitter = PolynomialCurveFitter.create(2);
        //权重以及坐标点
        List<WeightedObservedPoint> weightedObservedPoints = new ArrayList<>();
        for (int j = 0; j < x.length; j++) {
            //遍历xy值，并加入权重坐标点
//            logger.info(xX[j] + "yy:" + yY[j]);
            WeightedObservedPoint weightedObservedPoint = new WeightedObservedPoint(1, x[j], y[j]);
            weightedObservedPoints.add(weightedObservedPoint);
        }
        //得出多项式的系数-1.284270711704957E-15
        double[] doubles = polynomialCurveFitter.fit(weightedObservedPoints);
        //带入吸光度算出浓度
//        logger.info(doubles[0] + "1:" + doubles[1]);
        double a = doubles[2];
        double b = doubles[1];
        double c = doubles[0] ;
        double v1 = b * b;
        double v2 = 4 * a * (c-absorbanceGap);
        double d = v1 - v2;        //根据b^2-4ac判断方程可解性
        System.out.println(d);
        density = divideAndConquerQuadratic(a,b,c,absorbanceGap,x[x.length-1]);
        density = Float.parseFloat(new DecimalFormat("0.000").format(density));

        for (int k = 0; k < calDen.length; k++) {
            double key =  calDen[k];
            float v = Float.parseFloat(new DecimalFormat(yStep).format(( a*key*key+b*key+c)));
            for (int i = 0; i < x.length; i++) {
                if (x[i] ==key){
                    v = (float) y[i];
                }
            }
            setCalAbs(calAbs, k, v);
        }
        if (calDen.length == 2 && calAbs.length ==2){
            setY(x, y, calAbs, calDen);
            polynomialCurveFitter = PolynomialCurveFitter.create(2);
            //权重以及坐标点
             weightedObservedPoints = new ArrayList<>();
            for (int j = 0; j < x.length; j++) {
                //遍历xy值，并加入权重坐标点
//            logger.info(xX[j] + "yy:" + yY[j]);
                WeightedObservedPoint weightedObservedPoint = new WeightedObservedPoint(1, x[j], y[j]);
                weightedObservedPoints.add(weightedObservedPoint);
            }
            doubles = polynomialCurveFitter.fit(weightedObservedPoints);
             a = doubles[2];
             b = doubles[1];
             c = doubles[0];
            density = divideAndConquerQuadratic(a,b,c,absorbanceGap,x[x.length-1]);
        }
        return Float.parseFloat(new DecimalFormat("0.000").format(density));
    }
    public static double quadratic(double x, double a, double b, double c) {
        double v = BigMath.mul(BigMath.mul(a,x),x);
        double v1 = BigMath.mul(b,x);
        return v + v1 + c;
    }
    private static float getDensity(float density,double abs, double a, double b, double c, double d) {
        if (a < 0.0000000000001){
            density = -200;//(float) ((abs-c)/b);
        }
        if (d < 0) {
            density = -500;
        } else if (d == 0) {
            density = (float) (-b / (2 * a));
        } else {
            double sqrt = Math.sqrt(d);
            System.out.println("方程有两个解：" + (-b + sqrt) / (2 * a) + "和" + (-b - sqrt) / (2 * a));
            if (a > 0) {
                density = (float) ((-b + sqrt) / (2 * a));
            }
            if (a < 0) {
                density = (float) ((-b - sqrt) / (2 * a));
            }
        }
        return density;
    }


    /**
     * @apiNote 一次函数获得浓度
     * @author tzhh
     * @date 2021/5/27 17:46
     * @param absorbanceGap
	 * @param x
	 * @param y
     * @param calDen
     * @param calAbs
     * @return {@link float}
     **/
    public static float getDensityByLinear(float absorbanceGap, double[] x, double[] y, double[] calDen, double[] calAbs) {
        float density;//多项式曲线拟合器创建阶数为一的拟合器
        PolynomialCurveFitter polynomialCurveFitter = PolynomialCurveFitter.create(1);
        //权重以及坐标点
        List<WeightedObservedPoint> weightedObservedPoints = new ArrayList<>();
        for (int j = 0; j < x.length; j++) {
            //遍历xy值，并加入权重坐标点
//            logger.info(xX[j] + "yy:" + yY[j]);
            WeightedObservedPoint weightedObservedPoint = new WeightedObservedPoint(1, x[j], y[j]);
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

        for (int k = 0; k < calDen.length; k++) {
            double key =  calDen[k];
            float v = Float.parseFloat(new DecimalFormat(yStep).format( doubles[0] + doubles[1]*key));
            for (int i = 0; i < x.length; i++) {
                if (x[i] ==key){
                    v = (float) y[i];
                }
            }
            setCalAbs(calAbs, k, v);
        }

        if (calDen.length == 2 && calAbs.length ==2){
            setY(x, y, calAbs, calDen);
            polynomialCurveFitter = PolynomialCurveFitter.create(1);
            weightedObservedPoints = new ArrayList<>();
            for (int j = 0; j < x.length; j++) {
                //遍历xy值，并加入权重坐标点
//            logger.info(xX[j] + "yy:" + yY[j]);
                WeightedObservedPoint weightedObservedPoint = new WeightedObservedPoint(1, x[j], y[j]);
                weightedObservedPoints.add(weightedObservedPoint);
            }
            polynomialCurveFitter.fit(weightedObservedPoints);
            doubles = polynomialCurveFitter.fit(weightedObservedPoints);
            densityValue = (absorbanceGap - doubles[0]) / doubles[1];
            density = Float.parseFloat(new DecimalFormat("0.000").format(densityValue));
        }

        return density;
    }


    /**
     * @apiNote 样条曲线获得浓度
     * @author tzhh
     * @date 2021/5/27 17:45
     * @param absorbanceGap
	 * @param x
	 * @param y
     * @param calDen
     * @param calAbs
     * @return {@link float}
     **/
    public static float getSplineDensity(float absorbanceGap, double[] x, double[] y, double[] calDen, double[] calAbs) {

        logger.info(String.valueOf(absorbanceGap));
        logger.info(String.valueOf(y[y.length - 1]));
        logger.info(String.valueOf(y[0]));
        if (absorbanceGap < y[0]) {
            logger.info("absorbance:"+absorbanceGap+" < the min yY[0]:"+y[0]);
            return -501;
        }
        if (absorbanceGap > y[y.length - 1]) {
            logger.info("absorbance:"+absorbanceGap+" > the min yY[yY.length - 1]:"+y[y.length - 1]);
            return -502;
        }
        float density;// 样条曲线
        SplineInterpolator sp = new SplineInterpolator();
        //插入样条值
        PolynomialSplineFunction interpolate = sp.interpolate(x, y);
        //取得三次样条多项式
        PolynomialFunction[] polynomials = interpolate.getPolynomials();
//        int n = interpolate.getN();
        //knots is key list
        double[] knots = interpolate.getKnots();

        double maxX = x[x.length - 1];
        if (calDen.length == 2 && calAbs.length ==2){
            setCalAbs(x, calDen, calAbs, polynomials, knots, maxX ,y);
            setY(x, y, calAbs, calDen);
            sp = new SplineInterpolator();
            //插入样条值
             interpolate = sp.interpolate(x, y);
            //取得三次样条多项式
            polynomials = interpolate.getPolynomials();

            knots = interpolate.getKnots();
        }

        //v is
        int j = 0;

        //
        for (int i = 0; i < y.length - 1; i++) {
            if ((absorbanceGap > y[i] && absorbanceGap < y[i + 1]) || (absorbanceGap < y[i] && absorbanceGap > y[i + 1])) {
                //找到 abs 所在区间
                j = i;
            }
        }
        double densityValue = divideAndConquer(knots[j], x[j], x[j + 1], polynomials[j], absorbanceGap);
        density = Float.parseFloat(new DecimalFormat("0.000").format(densityValue));

        for (int k = 0; k < calDen.length; k++) {
            double key =  calDen[k];
            float v = Float.parseFloat(new DecimalFormat(yStep).format(polynomials[j].value(key)));
            for (int i = 0; i < x.length; i++) {
                if (x[i] ==key){
                    v = (float) y[i];
                }
            }
            setCalAbs(calAbs, k, v);
        }
        if (calDen.length == 2 && calAbs.length ==2){
            setY(x, y, calAbs, calDen);
            sp = new SplineInterpolator();
            interpolate = sp.interpolate(x, y);
            polynomials = interpolate.getPolynomials();
            knots = interpolate.getKnots();
            for (int i = 0; i < y.length - 1; i++) {
                if ((absorbanceGap > y[i] && absorbanceGap < y[i + 1]) || (absorbanceGap < y[i] && absorbanceGap > y[i + 1])) {
                    //找到 abs 所在区间
                    j = i;
                }
            }
            densityValue = divideAndConquer(knots[j], x[j], x[j + 1], polynomials[j], absorbanceGap);
            density = Float.parseFloat(new DecimalFormat("0.000").format(densityValue));
        }
        return density;
    }



    /**
     * @apiNote logit4p获得浓度
     * @author tzhh
     * @date 2021/5/27 17:45
     * @param x
	 * @param y
	 * @param absorbanceGap
     * @param calDen
     * @param calAbs
     * @return {@link float}
     **/
    public static float  RodBard(double[] x, double[] y, float absorbanceGap, double[] calDen, double[] calAbs) {
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

        for (int k = 0; k < calDen.length; k++) {
            double key =  calDen[k];
            float v = Float.parseFloat(new DecimalFormat(yStep).format(logit4p(key, a, b, c, d)));
            for (int i = 0; i < x.length; i++) {
                if (x[i] ==key){
                    v = (float) y[i];
                }
            }
            setCalAbs(calAbs, k, v);
        }
        if (calDen.length == 2 && calAbs.length ==2){
            setY(x, y, calAbs, calDen);
            curveFitter.doFit(7);
            p = curveFitter.getParams();
            a = Double.parseDouble(IJ.d2s(p[0], 5, 9));
            b = Double.parseDouble(IJ.d2s(p[1], 5, 9));
            c = Double.parseDouble(IJ.d2s(p[2], 5, 9));
            d = Double.parseDouble(IJ.d2s(p[3], 5, 9));
            densityValue = divideAndConquerRodBard(a, b, c, d, absorbanceGap,x[x.length-1]);
        }
        return Float.parseFloat(new DecimalFormat("0.000").format(densityValue));

    }
    //    logit4p

    public static double logit4p(double x, double a, double b, double c, double d) {
        return d + (a - d) / (1 + Math.pow((x / c), b));
    }




    /**
     * @apiNote 三次曲线获得浓度
     * @author tzhh
     * @date 2021/5/27 17:45
     * @param x
     * @param y
     * @param absorbanceGap
     * @param calDen
     * @param calAbs
     * @return {@link float}
     **/
    public static float cubicCurveFitting(double[] x, double[] y, float absorbanceGap, double[] calDen, double[] calAbs) {
        CurveFitter curveFitter = new CurveFitter(x, y);
        curveFitter.doFit(2);
        double[] p = curveFitter.getParams();
        double a = Double.parseDouble(IJ.d2s(p[0], 5, 9));
        double b = Double.parseDouble(IJ.d2s(p[1], 5, 9));
        double c = Double.parseDouble(IJ.d2s(p[2], 5, 9));
        double d = Double.parseDouble(IJ.d2s(p[3], 5, 9));

        if (absorbanceGap < cubicCurve(0,a,b,c,d)) {
            logger.info(absorbanceGap+"   "+logit4p(0,a,b,c,d));
            return -501;
        }
        double densityValue = divideAndConquerCubic(a, b, c, d, absorbanceGap,x[x.length-1]);

        for (int k = 0; k < calDen.length; k++) {
            double key =  calDen[k];
            float v = Float.parseFloat(new DecimalFormat(yStep).format(cubicCurve(key, a, b, c, d)));
            for (int i = 0; i < x.length; i++) {
                if (x[i] ==key){
                    v = (float) y[i];
                }
            }
            setCalAbs(calAbs, k, v);
        }


        if (calDen.length == 2 && calAbs.length ==2){
            setY(x, y, calAbs, calDen);

            curveFitter = new CurveFitter(x, y);
            curveFitter.doFit(2);

            p = curveFitter.getParams();
            a = Double.parseDouble(IJ.d2s(p[0], 5, 9));
            b = Double.parseDouble(IJ.d2s(p[1], 5, 9));
            c = Double.parseDouble(IJ.d2s(p[2], 5, 9));
            d = Double.parseDouble(IJ.d2s(p[3], 5, 9));

            densityValue= divideAndConquerCubic(a, b, c, d, absorbanceGap,x[x.length-1]);
        }


        return Float.parseFloat(new DecimalFormat("0.000").format(densityValue));
    }

    private static double cubicCurve(double x, double a, double b, double c, double d){
        return d*x*x*x + c*x*x +b*x +a;
    }
    /**
     * @apiNote 分治三次曲线
     * @author tzhh
     * @date 2021/6/11 14:44
     * @param a
     * @param b
     * @param c
     * @param absorbanceGap
     * @param xMax
     * @return {@link double}
     **/
    private static double divideAndConquerQuadratic(double a, double b, double c, float absorbanceGap, double xMax) {
        //没隔 1 为一个区间
        for (double x = 0; x <= xMax*2; x += 100) {
            double x1 = x;
            double x2 = x1 + 100;
            //取x的值
            double cubic = quadratic(x1, a, b, c);
//            logit4p(x1, a, b, c, d);
            if (cubic == absorbanceGap) {
                System.out.println(x1 + " ");
                return x1;
            } else {
                double cubic1 = quadratic(x2, a, b, c); //logit4p(x2, a, b, c, d)
                if ((cubic - absorbanceGap) * (cubic1 - absorbanceGap) < 0) {
                    while (x2 - x1 >= 0.0001) {
                        double mid = (x2 + x1) / 2;
                        double midVal = quadratic(mid, a, b, c);

                        if ((cubic - absorbanceGap) * (midVal - absorbanceGap) <= 0){

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

    /**
     * @apiNote 分治样条曲线
     * @author tzhh 
     * @date 2021/6/11 14:43
     * @param knode
	 * @param minX
	 * @param maxX
	 * @param polynomials
	 * @param absorbanceGap
     * @return {@link double}
     **/

    private static double divideAndConquer(double knode, double minX, double maxX, PolynomialFunction polynomials, double absorbanceGap) {
        //没隔 1 为一个区间
        for (double x = minX; x < maxX; x += 1) {
            double x1 = x - knode;
            double x2 = x1 + 1;
            double cubic = polynomials.value(x1);
            logger.info("cubic"+cubic+"x1"+x1+"x2"+x2);
//            logit4p(x1, a, b, c, d);
            if (cubic == absorbanceGap) return x1+ knode;
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

    /**
     * @apiNote 分治logit4p
     * @author tzhh
     * @date 2021/6/11 14:44
     * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param absorbanceGap
	 * @param xMax
     * @return {@link double}
     **/
    private static double divideAndConquerRodBard(double a, double b, double c, double d, double absorbanceGap, double xMax) {
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

    /**
     * @apiNote 分治三次曲线
     * @author tzhh
     * @date 2021/6/11 14:44
     * @param a
     * @param b
     * @param c
     * @param d
     * @param absorbanceGap
     * @param xMax
     * @return {@link double}
     **/
    private static double divideAndConquerCubic(double a, double b, double c, double d, float absorbanceGap, double xMax) {
        //没隔 1 为一个区间
        for (double x = 0; x <= xMax*2; x += 100) {
            double x1 = x;
            double x2 = x1 + 100;
            //取x的值
            double cubic = cubicCurve(x1, a, b, c, d);
//            logit4p(x1, a, b, c, d);
            if (cubic == absorbanceGap) {
                System.out.println(x1 + " ");
                return x1;
            } else {
                double cubic1 = cubicCurve(x2, a, b, c, d); //logit4p(x2, a, b, c, d)
                if ((cubic - absorbanceGap) * (cubic1 - absorbanceGap) < 0) {
                    while (x2 - x1 >= 0.0001) {
                        double mid = (x2 + x1) / 2;
                        if ((cubic - absorbanceGap) * (cubicCurve(mid, a, b, c, d) - absorbanceGap) <= 0){

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


    public static List<List<Float>> getValueList(String algorithm, double[] y, double[] xX, double[] yY, double[] calAbs, double[] calDen) {

        List<List<Float>> relAndValue = new ArrayList<>();

        List<Float> relVal = new ArrayList<>();

        List<Float> valueList = new ArrayList<>();
        if (y[y.length - 1] - y[0] < 1) {
            yStep = "0.000";
        }
        if (y[y.length - 1] - y[0] < 0.1) {
            yStep = "0.0000";
        }
        if (y[y.length - 1] - y[0] < 0.01) {
            yStep = "0.00000";
        }
        if (y[y.length - 1] - y[0] < 0.001) {
            yStep = "0.000000";
        }
        if ("三次样条函数".equals(algorithm)) {
            // 样条曲线
            splineInterpolation(xX, yY, valueList ,calAbs ,calDen ,relVal);
        }
        if ("一次曲线".equals(algorithm)) {
            //一次曲线
            Instance(xX, yY, valueList,calAbs ,calDen ,relVal);
        }
        if ("二次曲线拟合".equals(algorithm)) {
            //二次曲线拟合
            quadratic(xX, yY, valueList,calAbs ,calDen ,relVal);
        }
        if ("RodBard".equals(algorithm)) {
            //log4p
            RodBard(xX, yY, valueList,calAbs ,calDen ,relVal);
        }
        if ("三次曲线拟合".equals(algorithm)) {
            //三次曲线拟合
            cubicCurveFitting(xX, yY, valueList,calAbs ,calDen ,relVal);
        }
        relAndValue.add(valueList);
        relAndValue.add(relVal);
        return relAndValue;
    }

    /***
     * @apiNote 三次曲线拟合
     * @author tzhh 
     * @date 2021/6/15 10:22
     * @param x
	 * @param y
	 * @param value
	 * @param calAbs
	 * @param calDen
	 * @param relVal
     * @return 
     **/
    private static void cubicCurveFitting(double[] x, double[] y, List<Float> value, double[] calAbs, double[] calDen, List<Float> relVal) {
        CurveFitter curveFitter = new CurveFitter(x, y);
        curveFitter.doFit(2);
        double[] p = curveFitter.getParams();
        double   a = Double.parseDouble(IJ.d2s(p[0], 3, 5));
        double   b = Double.parseDouble(IJ.d2s(p[1], 3, 5));
        double   c = Double.parseDouble(IJ.d2s(p[2], 3, 5));
        double   d = Double.parseDouble(IJ.d2s(p[3], 3, 5));
        double minKey;
        if (x[0] < 0) {
            minKey = x[0];
        }else {
            minKey = x[0]*0.9;
        }
        double step = 0.1;
        double maxKey = x[x.length - 1];
        if (maxKey - minKey < 1) {
            step = 0.01;
        }
        if (maxKey - minKey < 0.1) {
            step = 0.001;
        }
        for (double key = minKey; key <= maxKey; key = key + step) {
            value.add(Float.parseFloat(new DecimalFormat(yStep).format(cubicCurve(key, a, b, c, d))));
        }


        for (int k = 0; k < calDen.length; k++) {
            double key =  calDen[k];
            float v = Float.parseFloat(new DecimalFormat(yStep).format(cubicCurve(key, a, b, c, d)));
            for (int i = 0; i < x.length; i++) {
                if (x[i] ==key){
                    v = (float) y[i];
                }
            }
            setCalAbs(calAbs, k, v);
        }

        if (calDen.length == 2 && calAbs.length ==2){
            setY(x, y, calAbs, calDen);

            curveFitter = new CurveFitter(x, y);
            curveFitter.doFit(2);
            p = curveFitter.getParams();
            a = Double.parseDouble(IJ.d2s(p[0], 3, 5));
            b = Double.parseDouble(IJ.d2s(p[1], 3, 5));
            c = Double.parseDouble(IJ.d2s(p[2], 3, 5));
            d = Double.parseDouble(IJ.d2s(p[3], 3, 5));
            for (double key = minKey; key <= maxKey; key = key + step) {
                relVal.add(Float.parseFloat(new DecimalFormat(yStep).format(cubicCurve(key, a, b, c, d))));
            }
        }
    }



    /**
     * @apiNote logit4p
     * @author tzhh 
     * @date 2021/6/15 10:22
     * @param x
	 * @param y
	 * @param value
	 * @param calAbs
	 * @param calDen
	 * @param relVal
     * @return 
     **/
    private static void RodBard(double[] x, double[] y, List<Float> value, double[] calAbs, double[] calDen, List<Float> relVal) {
        CurveFitter curveFitter = new CurveFitter(x, y);
        curveFitter.doFit(7);
        double[] p = curveFitter.getParams();
        double   a = Double.parseDouble(IJ.d2s(p[0], 5, 9));
        double   b = Double.parseDouble(IJ.d2s(p[1], 5, 9));
        double   c = Double.parseDouble(IJ.d2s(p[2], 5, 9));
        double   d = Double.parseDouble(IJ.d2s(p[3], 5, 9));
        double minKey;
        if (x[0] < 0) {
            minKey = x[0];
        }else {
            minKey = x[0]*0.9;
        }
        double step = 0.1;
        double maxKey = x[x.length - 1];
        if (maxKey - minKey < 1) {
            step = 0.01;
        }
        if (maxKey - minKey < 0.1) {
            step = 0.001;
        }



        for (double key = minKey; key <= maxKey; key = key + step) {
            value.add(Float.parseFloat(new DecimalFormat(yStep).format(d + (a - d) / (1 + Math.pow((key / c), b)))));
        }
        for (int k = 0; k < calDen.length; k++) {
            double key =  calDen[k];
//            double v1 = 1.0D + Math.pow(key / p[2], p[1]);
//            float v = Float.parseFloat(new DecimalFormat(yStep).format(p[3] + (p[0] - p[3]) / v1));
            float f = (float) CurveFitter.f(7,p, key);
//            double v2 = 1 + Math.pow((key / c), b);
//            v = Float.parseFloat(new DecimalFormat(yStep).format(d + (a - d) / v2));
            for (int i = 0; i < x.length; i++) {
                if (x[i] ==key){
                    f = (float) y[i];
                }
            }
            setCalAbs(calAbs, k, f);
        }


        if (calDen.length == 2 && calAbs.length ==2){
            setY(x, y, calAbs, calDen);
            curveFitter = new CurveFitter(x, y);
            curveFitter.doFit(7);
            p = curveFitter.getParams();
            a = Double.parseDouble(IJ.d2s(p[0], 5, 9));
            b = Double.parseDouble(IJ.d2s(p[1], 5, 9));
            c = Double.parseDouble(IJ.d2s(p[2], 5, 9));
            d = Double.parseDouble(IJ.d2s(p[3], 5, 9));
        }

        for (double key = minKey; key <= maxKey; key = key + step) {
            relVal.add(Float.parseFloat(new DecimalFormat(yStep).format(d + (a - d) / (1 + Math.pow((key / c), b)))));
        }
    }

    private static void setY(double[] x, double[] y, double[] calAbs, double[] calDen) {

          double k = (calAbs[1]-calAbs[0])/(calDen[1]-calDen[0]);
          double b = calAbs[0]- k * calDen[0];
        if (calAbs[0] == calAbs[1] ){
            k = 0;
            b = calAbs[0];
        }
//        double b = (calDen[0] * calAbs[1] - calDen[1] * calAbs[0]) / (calDen[0] - calDen[1]);
//        double k = (calAbs[0] - b) / calDen[0];
        for (int i = 0; i < x.length; i++) {
            y[i] = (k * x[i] + b) * y[i];
        }
    }

    /**
     * 取得一次多项式
     *  @param x
     * @param y
     * @param value
     * @param calAbs
     * @param calDen
     * @param relVal
     */
    private static void Instance(double[] x, double[] y, List<Float> value, double[] calAbs, double[] calDen, List<Float> relVal) {
        //多项式曲线拟合器创建阶数为一的拟合器
        PolynomialCurveFitter polynomialCurveFitter = PolynomialCurveFitter.create(1);
        //权重以及坐标点
        List<WeightedObservedPoint> weightedObservedPoints = new ArrayList<>();
        for (int i = 0; i < x.length; i++) {
            //遍历xy值，并加入权重坐标点
            WeightedObservedPoint weightedObservedPoint = new WeightedObservedPoint(1, x[i], y[i]);
            weightedObservedPoints.add(weightedObservedPoint);
        }

        double[] doubles = polynomialCurveFitter.fit(weightedObservedPoints);

        double minKey = 0;
        if (x[0] < 0) {
            minKey = x[0];
        }
        double step = 0.1;
        double maxKey = x[x.length - 1];
        if (maxKey - minKey < 1) {
            step = 0.01;
        }
        if (maxKey - minKey < 0.1) {
            step = 0.001;
        }
        for (double key = minKey; key <= maxKey; key = key + step) {
            value.add(Float.parseFloat(new DecimalFormat(yStep).format(doubles[1] * key + doubles[0])));

        }

        for (int k = 0; k < calDen.length; k++) {
            double key =  calDen[k];
            float v = Float.parseFloat(new DecimalFormat(yStep).format(doubles[1] * key + doubles[0]));
            for (int i = 0; i < x.length; i++) {
                if (x[i] ==key){
                    v = (float) y[i];
                }
            }
            setCalAbs(calAbs, k, v);
        }
        if (calDen.length == 2 && calAbs.length ==2){
            setY(x, y, calAbs, calDen);
            polynomialCurveFitter = PolynomialCurveFitter.create(1);
            //权重以及坐标点
            weightedObservedPoints = new ArrayList<>();
            for (int i = 0; i < x.length; i++) {
                //遍历xy值，并加入权重坐标点
                WeightedObservedPoint weightedObservedPoint = new WeightedObservedPoint(1, x[i], y[i]);
                weightedObservedPoints.add(weightedObservedPoint);
            }

            doubles = polynomialCurveFitter.fit(weightedObservedPoints);
            for (double key = minKey; key <= maxKey; key = key + step) {
                relVal.add(Float.parseFloat(new DecimalFormat(yStep).format(doubles[1] * key + doubles[0])));
            }
        }

    }

    /**
     * 取得二次多项式
     *  @param x
     * @param y
     * @param value
     * @param calAbs
     * @param calDen
     * @param relVal
     */
    private static void quadratic(double[] x, double[] y, List<Float> value, double[] calAbs, double[] calDen, List<Float> relVal) {
        //多项式曲线拟合器创建阶数为一的拟合器
        PolynomialCurveFitter polynomialCurveFitter = PolynomialCurveFitter.create(2);
        //权重以及坐标点
        List<WeightedObservedPoint> weightedObservedPoints = new ArrayList<>();
        for (int i = 0; i < x.length; i++) {
            //遍历xy值，并加入权重坐标点
            WeightedObservedPoint weightedObservedPoint = new WeightedObservedPoint(1, x[i], y[i]);
            weightedObservedPoints.add(weightedObservedPoint);
        }

        double[] doubles = polynomialCurveFitter.fit(weightedObservedPoints);

        double minKey = 0;
        if (x[0] < 0) {
            minKey = x[0];
        }
        double step = 0.1;
        double maxKey = x[x.length - 1];
        if (maxKey - minKey < 1) {
            step = 0.01;
        }
        if (maxKey - minKey < 0.1) {
            step = 0.001;
        }
        for (double key = minKey; key <= maxKey; key = key + step) {
            value.add(Float.parseFloat(new DecimalFormat(yStep).format(doubles[2] * key * key + doubles[1] * key + doubles[0])));
        }

        for (int k = 0; k < calDen.length; k++) {
            double key =  calDen[k];
            float v = Float.parseFloat(new DecimalFormat(yStep).format(doubles[2] * key * key + doubles[1] * key + doubles[0]));
            for (int i = 0; i < x.length; i++) {
                if (x[i] ==key){
                    v = (float) y[i];
                }
            }
            setCalAbs(calAbs, k, v);
        }
        if (calDen.length == 2 && calAbs.length ==2){
            setY(x, y, calAbs, calDen);
            polynomialCurveFitter = PolynomialCurveFitter.create(2);
            //权重以及坐标点
             weightedObservedPoints = new ArrayList<>();
            for (int i = 0; i < x.length; i++) {
                //遍历xy值，并加入权重坐标点
                WeightedObservedPoint weightedObservedPoint = new WeightedObservedPoint(1, x[i], y[i]);
                weightedObservedPoints.add(weightedObservedPoint);
            }

            doubles = polynomialCurveFitter.fit(weightedObservedPoints);
            for (double key = minKey; key <= maxKey; key = key + step) {
                relVal.add(Float.parseFloat(new DecimalFormat(yStep).format(doubles[2] * key * key + doubles[1] * key + doubles[0])));
            }
        }
    }

    /**
     * 取得三次样条多项式
     * @param x
     * @param y
     * @param value
     * @param calAbs
     * @param calDen
     */
    private static void splineInterpolation(double[] x, double[] y, List<Float> value, double[] calAbs, double[] calDen, List<Float> relValue) {
        SplineInterpolator sp = new SplineInterpolator();
        //插入样条值
        PolynomialSplineFunction interpolate = sp.interpolate(x, y);
        //取得三次样条多项式
        PolynomialFunction[] polynomials = interpolate.getPolynomials();
//        int n = interpolate.getN();
        // knots is key list
        double[] knots = interpolate.getKnots();
        //v is
        double minKey = 0;
        if (x[0] < 0) {
            minKey = x[0];
        }else {
            minKey = x[0]*0.9;
        }
        double step = 0.1;
        double maxX = x[x.length - 1];
        if (maxX - minKey < 1) {
            step = 0.01;
        }
        if (maxX - minKey < 0.1) {
            step = 0.001;
        }
        double maxKey = ((maxX / (step * 5)) + 1) * (step * 5);
        getValue(x, value, polynomials, knots, minKey, step, maxX, maxKey);

        setCalAbs(x, calDen, calAbs, polynomials, knots, maxX,y);
        if (calDen.length == 2 && calAbs.length ==2){
            setY(x, y, calAbs, calDen);

            SplineInterpolator relsp = new SplineInterpolator();
            //插入样条值
            PolynomialSplineFunction relinterpolate = relsp.interpolate(x, y);
            //取得三次样条多项式
            PolynomialFunction[] relpolynomials = relinterpolate.getPolynomials();

            double[] relknots = interpolate.getKnots();
            //v is
            double relminKey = 0;
            if (x[0] < 0) {
                relminKey = x[0];
            }else {
                relminKey = x[0]*0.9;
            }
            double relstep = 0.1;
            double relmaxX = x[x.length - 1];
            if (relmaxX - relminKey < 1) {
                relstep = 0.01;
            }
            if (relmaxX - relminKey < 0.1) {
                relstep = 0.001;
            }
            double relmaxKey = ((relmaxX / (relstep * 5)) + 1) * (relstep * 5);
            getValue(x, relValue, relpolynomials, relknots, relminKey, relstep, relmaxX, relmaxKey);

        }

    }

    private static void getValue(double[] x, List<Float> relValue, PolynomialFunction[] polynomials, double[] relknots, double relminKey, double relstep, double relmaxX, double relmaxKey) {
         for (double key = relminKey; key < relmaxKey; key = key + relstep) {
            int i = Arrays.binarySearch(relknots, key);

            if (i < 0) {
                i = -i - 2;
            }
            // key 大于最大的x值
            if (key > relmaxX) {
                i = x.length - 1;
            }
            // key小于最小的x值
            if (key <= x[0]) {
                i = 0;
            }
            // 这将处理v是最后一个结值的情况 只有n-1多项式，所以如果v是最后一个结
            //然后我们用最后一个多项式来计算这个值。
            if (i >= polynomials.length) {
                i--;
            }
             double value = polynomials[i].value(key - relknots[i]);
             String format = new DecimalFormat(yStep).format(value);
             float e = Float.parseFloat(format);
             relValue.add(e);

        }
    }


    private static void setCalAbs(double[] x, double[] calDen, double[] calAbs, PolynomialFunction[] polynomials, double[] knots, double maxX, double[] y) {
        for (int k = 0; k < calDen.length; k++) {
            double key =  calDen[k];
            int i = Arrays.binarySearch(knots, key);

            if (i < 0) {
                i = -i - 2;
            }
            // key 大于最大的x值
            if (key > maxX) {
                i = x.length - 1;
            }
            // key小于最小的x值
            if (key <= x[0]) {
                i = 0;
            }
            // 这将处理v是最后一个结值的情况 只有n-1多项式，所以如果v是最后一个结
            //然后我们用最后一个多项式来计算这个值。
            if (i >= polynomials.length) {
                i--;
            }

            float v = Float.parseFloat(new DecimalFormat(yStep).format(polynomials[i].value(key - knots[i])));
            for (int j = 0; j < x.length; j++) {
                if (x[i] ==key){
                    v = (float) y[i];
                }
            }
            setCalAbs(calAbs, k, v);

        }
    }
    private static void setCalAbs(double[] calAbs, int k, float v) {
        calAbs[k] = calAbs[k] /v;
    }

}


