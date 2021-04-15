package com.laola.apa.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 最小二乘法
 * @author coshaho
 *
 */
public class LineRegression
{
    /**
     * 最小二乘法
     * @param X
     * @param Y
     * @return y = ax + b, r
     */
    public Map<String, Double> lineRegression(double[] X, double[] Y)
    {
        if(null == X || null == Y || 0 == X.length
                || 0 == Y.length || X.length != Y.length)
        {
            throw new RuntimeException();
        }

        // x平方差和
        double sxx = varianceSum(X);
        // y平方差和
        double syy = varianceSum(Y);
        // xy协方差和
        double Sxy = covarianceSum(X, Y);

        double xAvg = arraySum(X) / X.length;
        double yAvg = arraySum(Y) / Y.length;

        double a = Sxy / sxx;
        double b = yAvg - a * xAvg;

        // 相关系数
        double r = Sxy / Math.sqrt(sxx * syy);
        Map<String, Double> result = new HashMap<String, Double>();
        result.put("a", a);
        result.put("b", b);
        result.put("r", r);

        return result;
    }

    /**
     * 计算方差和
     * @param X
     * @return
     */
    private double varianceSum(double[] X)
    {   //取平均数
        double xAvg = arraySum(X) / X.length;
        //数组减常数
        return arraySqSum(arrayMinus(X, xAvg));
    }

    /**
     * 计算协方差和
     * @param X
     * @param Y
     * @return
     */
    private double covarianceSum(double[] X, double[] Y)
    {
        double xAvg = arraySum(X) / X.length;
        double yAvg = arraySum(Y) / Y.length;
        return arrayMulSum(arrayMinus(X, xAvg), arrayMinus(Y, yAvg));
    }

    /**
     * 数组减常数
     * @param X
     * @param x
     * @return
     */
    private double[] arrayMinus(double[] X, double x)
    {
        int n = X.length;
        double[] result = new double[n];
        for(int i = 0; i < n; i++)
        {
            result[i] = X[i] - x;
        }

        return result;
    }

    /**
     * 数组求和
     * @param X
     * @return
     */
    private double arraySum(double[] X)
    {
        double s = 0 ;
        for( double x : X )
        {
            s = s + x ;
        }
        return s ;
    }

    /**
     * 数组平方求和
     * @param X
     * @return
     */
    private double arraySqSum(double[] X)
    {
        double s = 0 ;
        for( double x : X )
        {
            s = s + Math.pow(x, 2) ; ;
        }
        return s ;
    }

    /**
     * 数组对应元素相乘求和
     * @param X
     * @return
     */
    private double arrayMulSum(double[] X, double[] Y)
    {
        double s = 0 ;
        for( int i = 0 ; i < X.length ; i++ )
        {
            s = s + X[i] * Y[i] ;
        }
        return s ;
    }

    public   void main(String[] args)
    {
        Random random = new Random();
        double[] X = new double[20];
        double[] Y = new double[20];

        for(int i = 0; i < 20; i++)
        {
            X[i] = Double.valueOf(Math.floor(random.nextDouble() * 97));
            Y[i] = Double.valueOf(Math.floor(random.nextDouble() * 997));
        }

        System.out.println(new LineRegression().lineRegression(X, Y));



        // TODO Auto-generated method stub
        /**
         * 一元回归
         */
        // int i;
        // double[] dt=new double[6];
        // double[] a=new double[2];
        // double[] x={ 0.0,0.1,0.2,0.3,0.4,0.5,
        // 0.6,0.7,0.8,0.9,1.0};
        // double[] y={ 2.75,2.84,2.965,3.01,3.20,
        // 3.25,3.38,3.43,3.55,3.66,3.74};
        // SPT.SPT1(x,y,11,a,dt);
        // System.out.println("");
        // System.out.println("a="+a[1]+" b="+a[0]);
        // System.out.println("q="+dt[0]+" s="+dt[1]+" p="+dt[2]);
        // System.out.println(" umax="+dt[3]+" umin="+dt[4]+" u="+dt[5]);
        /**
         * 多元回归
         */
        int i;
        double[] a = new double[4];
        double[] v = new double[3];
        double[] dt = new double[4];

        double[][] x = { { 1.1, 1.0, 1.2, 1.1, 0.9 },
                { 2.0, 2.0, 1.8, 1.9, 2.1 }, { 3.2, 3.2, 3.0, 2.9, 2.9 } };
        double[] y = { 10.1, 10.2, 10.0, 10.1, 10.0 };
        sqt2(x, y, 3, 5, a, dt, v);
        for (i = 0; i <= 3; i++) {
            System.out.println("a(" + i + ")=" + a[i]);
        }
        System.out.println("q=" + dt[0] + "  s=" + dt[1] + "  r=" + dt[2]);
        for (i = 0; i <= 2; i++) {
            System.out.println("v(" + i + ")=" + v[i]);
        }
        System.out.println("u=" + dt[3]);
    }



    /**
     * 多元线性回归分析
     *
     * @param x [m][n]
     *            每一列存放m个自变量的观察值
     * @param y [n]
     *            存放随即变量y的n个观察值
     * @param m
     *            自变量的个数
     * @param n
     *            观察数据的组数
     * @param a
     *            返回回归系数a0,...,am
     * @param dt [4]
     *            dt[0]偏差平方和q,dt[1] 平均标准偏差s dt[2]返回复相关系数r dt[3]返回回归平方和u
     * @param v [m]
     *            返回m个自变量的偏相关系数
     */
    public static void sqt2(double[][] x, double[] y, int m, int n, double[] a,
                            double[] dt, double[] v) {
        int i, j, k, mm;
        double q, e, u, p, yy, s, r, pp;
        double[] b = new double[(m + 1) * (m + 1)];
        mm = m + 1;
        b[mm * mm - 1] = n;
        for (j = 0; j <= m - 1; j++) {
            p = 0.0;
            for (i = 0; i <= n - 1; i++) {
                p = p + x[j][i];
            }
            b[m * mm + j] = p;
            b[j * mm + m] = p;
        }
        for (i = 0; i <= m - 1; i++) {
            for (j = i; j <= m - 1; j++) {
                p = 0.0;
                for (k = 0; k <= n - 1; k++) {
                    p = p + x[i][k] * x[j][k];
                }
                b[j * mm + i] = p;
                b[i * mm + j] = p;
            }
        }
        a[m] = 0.0;
        for (i = 0; i <= n - 1; i++) {
            a[m] = a[m] + y[i];
        }
        for (i = 0; i <= m - 1; i++) {
            a[i] = 0.0;
            for (j = 0; j <= n - 1; j++) {
                a[i] = a[i] + x[i][j] * y[j];
            }
        }
        chlk(b, mm, 1, a);
        yy = 0.0;
        for (i = 0; i <= n - 1; i++) {
            yy = yy + y[i] / n;
        }
        q = 0.0;
        e = 0.0;
        u = 0.0;
        for (i = 0; i <= n - 1; i++) {
            p = a[m];
            for (j = 0; j <= m - 1; j++) {
                p = p + a[j] * x[j][i];
            }
            q = q + (y[i] - p) * (y[i] - p);
            e = e + (y[i] - yy) * (y[i] - yy);
            u = u + (yy - p) * (yy - p);
        }
        s = Math.sqrt(q / n);
        r = Math.sqrt(1.0 - q / e);
        for (j = 0; j <= m - 1; j++) {
            p = 0.0;
            for (i = 0; i <= n - 1; i++) {
                pp = a[m];
                for (k = 0; k <= m - 1; k++) {
                    if (k != j) {
                        pp = pp + a[k] * x[k][i];
                    }
                }
                p = p + (y[i] - pp) * (y[i] - pp);
            }
            v[j] = Math.sqrt(1.0 - q / p);
        }
        dt[0] = q;
        dt[1] = s;
        dt[2] = r;
        dt[3] = u;
    }

    private static int chlk(double[] a, int n, int m, double[] d) {
        int i, j, k, u, v;
        if ((a[0] + 1.0 == 1.0) || (a[0] < 0.0)) {
            System.out.println("fail\n");
            return (-2);
        }
        a[0] = Math.sqrt(a[0]);
        for (j = 1; j <= n - 1; j++) {
            a[j] = a[j] / a[0];
        }
        for (i = 1; i <= n - 1; i++) {
            u = i * n + i;
            for (j = 1; j <= i; j++) {
                v = (j - 1) * n + i;
                a[u] = a[u] - a[v] * a[v];
            }
            if ((a[u] + 1.0 == 1.0) || (a[u] < 0.0)) {
                System.out.println("fail\n");
                return (-2);
            }
            a[u] = Math.sqrt(a[u]);
            if (i != (n - 1)) {
                for (j = i + 1; j <= n - 1; j++) {
                    v = i * n + j;
                    for (k = 1; k <= i; k++) {
                        a[v] = a[v] - a[(k - 1) * n + i] * a[(k - 1) * n + j];
                    }
                    a[v] = a[v] / a[u];
                }
            }
        }
        for (j = 0; j <= m - 1; j++) {
            d[j] = d[j] / a[0];
            for (i = 1; i <= n - 1; i++) {
                u = i * n + i;
                v = i * m + j;
                for (k = 1; k <= i; k++) {
                    d[v] = d[v] - a[(k - 1) * n + i] * d[(k - 1) * m + j];
                }
                d[v] = d[v] / a[u];
            }
        }
        for (j = 0; j <= m - 1; j++) {
            u = (n - 1) * m + j;
            d[u] = d[u] / a[n * n - 1];
            for (k = n - 1; k >= 1; k--) {
                u = (k - 1) * m + j;
                for (i = k; i <= n - 1; i++) {
                    v = (k - 1) * n + i;
                    d[u] = d[u] - a[v] * d[i * m + j];
                }
                v = (k - 1) * n + k - 1;
                d[u] = d[u] / a[v];
            }
        }
        return (2);
    }

    /**
     * @param args
     */

}