package com.laola.apa.utils;

public final class LaserHomogenizationUtils {
    /**
     * 通过电流值（毫瓦）计算激光等级
     */
    public static int calculateLevelByElectricity(int[] laserHomogenizationCoefficients, int electricity) {
        double a = 0;
        double b = 0;
        int l0 = laserHomogenizationCoefficients[0];
        int l1 = laserHomogenizationCoefficients[1];
        int l2 = laserHomogenizationCoefficients[2];
        int l3 = laserHomogenizationCoefficients[3];
        if (0 < electricity && electricity <= 1) {
            double x1 = 0;
            double y1 = 0;
            double x2 = 1;
            double y2 = (double) l0;
            // a = (y3 - y1)/(x3 - x0)
            a = (y2 - y1) / (x2 - x1);
        } else if (1 < electricity && electricity <= 100) {
            double x1 = 1;
            double y1 = (double) l0;
            double x2 = 100;
            double y2 = (double) l1;
            // a = (y3 - y1)/(x3 - x0)
            a = (y2 - y1) / (x2 - x1);
            // 将两个点带入后计算得出 b = (4*y1 - y3)/3
            b = (y1 - a * x1 + y2 - a * x2) / 2;
        } else if (100 < electricity && electricity <= 300) {
            double x1 = 100;
            double y1 = (double) l1;
            double x2 = 300;
            double y2 = (double) l2;
            // a = (y3 - y1)/(x3 - x0)
            a = (y2 - y1) / (x2 - x1);
            // b = y - ax
            // 将两个点带入后计算得出 b = (4*y1 - y3)/3
            b = (y1 - a * x1 + y2 - a * x2) / 2;
        } else if (300 < electricity) {
            double x1 = 300;
            double y1 = (double) l2;
            double x2 = 500;
            double y2 = (double) l3;
            // a = (y3 - y1)/(x3 - x0)
            a = (y2 - y1) / (x2 - x1);
            // b = y - ax
            // 将两个点带入后计算得出 b = (4*y1 - y3)/3
            b = (y1 - a * x1 + y2 - a * x2) / 2;
        }
        return (int) (a * electricity + b);
    }

    /**
     * 通过百分比计算电流值（毫瓦）
     */
    public static int calculateElectricity(int progress) {
        if (progress > 500) {
            progress = 500;
        }
        double a = 0;
        double b = 0;
        if (0 < progress && progress <= 1) {
            double x1 = 0;
            double y1 = 0;
            double x2 = 1;
            double y2 = 1;
            // a = (y3 - y1)/(x3 - x0)
            a = (y2 - y1) / (x2 - x1);
            //
        } else if (1 < progress && progress <= 100) {
            double x1 = 1;
            double y1 = 1;
            double x2 = 100;
            double y2 = 100;
            // a = (y3 - y1)/(x3 - x0)
            a = (y2 - y1) / (x2 - x1);
            // b = y - ax
            // 将两个点带入后计算得出 b = (4*y1 - y3)/3
            b = (y1 - a * x1 + y2 - a * x2) / 2;
        } else if (100 < progress && progress <= 300) {
            double x1 = 100;
            double y1 = 100;
            double x2 = 300;
            double y2 = 300;
            // a = (y3 - y1)/(x3 - x0)
            a = (y2 - y1) / (x2 - x1);
            // b = y - ax
            // 将两个点带入后计算得出 b = (4*y1 - y3)/3
            b = (y1 - a * x1 + y2 - a * x2) / 2;
        } else if (300 < progress) {
            double x1 = 300;
            double y1 = 300;
            double x2 = 500;
            double y2 = 500;
            // a = (y3 - y1)/(x3 - x0)
            a = (y2 - y1) / (x2 - x1);
            // b = y - ax
            // 将两个点带入后计算得出 b = (4*y1 - y3)/3
            b = (y1 - a * x1 + y2 - a * x2) / 2;
        }
        return (int) (a * progress + b);
    }

    /**
     * 通过电流值（毫瓦）计算百分比
     */
    public static int calculateProgress(int electricity) {
        if (electricity > 500) {
            electricity = 500;
        }
        double a = 0;
        double b = 0;
        if (0 < electricity && electricity <= 1) {
            double x1 = 0;
            double y1 = 0;
            double x2 = 1;
            double y2 = 1;
            // a = (y3 - y1)/(x3 - x0)
            a = (y2 - y1) / (x2 - x1);
            //
        } else if (1 < electricity && electricity <= 100) {
            double x1 = 1;
            double y1 = 1;
            double x2 = 100;
            double y2 = 100;
            // a = (y3 - y1)/(x3 - x0)
            a = (y2 - y1) / (x2 - x1);
            // b = y - ax
            // 将两个点带入后计算得出 b = (4*y1 - y3)/3
            b = (y1 - a * x1 + y2 - a * x2) / 2;
        } else if (100 < electricity && electricity <= 300) {
            double x1 = 100;
            double y1 = 100;
            double x2 = 300;
            double y2 = 300;
            // a = (y3 - y1)/(x3 - x0)
            a = (y2 - y1) / (x2 - x1);
            // b = y - ax
            // 将两个点带入后计算得出 b = (4*y1 - y3)/3
            b = (y1 - a * x1 + y2 - a * x2) / 2;
        } else if (300 < electricity) {
            double x1 = 300;
            double y1 = 300;
            double x2 = 500;
            double y2 = 500;
            // a = (y3 - y1)/(x3 - x0)
            a = (y2 - y1) / (x2 - x1);
            // b = y - ax
            // 将两个点带入后计算得出 b = (4*y1 - y3)/3
            b = (y1 - a * x1 + y2 - a * x2) / 2;
        }
        return (int) (a * electricity + b);
    }
}
