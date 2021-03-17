package com.laola.apa.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ArrayUtils {
    public static String arrayList2String(ArrayList<?> list) {
        return list.toString();
    }

    public static ArrayList<Double> string2ArrayList(String str) {
        str = str.substring(str.lastIndexOf("[") + 1, str.lastIndexOf("]") == -1 ? 0 : str.lastIndexOf("]"));
        ArrayList<Double> list = new ArrayList<>();
        String[] s = str.split(",");
        String item;
        for (String doubleStr : s) {
            item = doubleStr.trim();
            if (!"".equals(item)) {
                list.add(Double.parseDouble(item));
            }
        }
        return list;
    }

    public static String doubleArray2String(double[] temp) {
        return Arrays.toString(temp);
    }

    public static double[] string2DoubleArray(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }
        str = str.substring(str.indexOf("[") + 1, str.lastIndexOf("]") == -1 ? 0 : str.lastIndexOf("]"));
        if (str.equals("")) {
            return new double[0];
        }
        String[] arrayStr = str.split(",");
        double[] arrayDouble = new double[arrayStr.length];
        for (int i = 0; i < arrayStr.length; i++) {
            arrayDouble[i] = Double.parseDouble(arrayStr[i].trim());
        }
        return arrayDouble;
    }

    public static double[] ArrayList2doubleArray(ArrayList<Double> list) {
        double[] arrayDouble = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arrayDouble[i] = list.get(i);
        }
        return arrayDouble;
    }

    public static int[] ArrayList2intArray(ArrayList<Integer> list) {
        int[] arrayInt = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arrayInt[i] = list.get(i);
        }
        return arrayInt;
    }

    public static ArrayList<Double> doubleArray2ArrayList(double[] temp) {
        ArrayList<Double> list = new ArrayList<>();
        for (double d : temp) {
            list.add(d);
        }
        return list;
    }

    public static double[] intArray2doubleArray(int[] temp) {
        double[] arrayDouble = new double[temp.length];
        for (int i = 0; i < temp.length; i++) {
            arrayDouble[i] = temp[i];
        }
        return arrayDouble;
    }

    public static int[] doubleArray2intArray(double[] temp) {
        int[] arrayInt = new int[temp.length];
        for (int i = 0; i < temp.length; i++) {
            arrayInt[i] = (int)Math.round(temp[i]);
        }
        return arrayInt;
    }

    public static int[] string2intArray(String str) {
        str = str.substring(str.indexOf("[") + 1, str.lastIndexOf("]") == -1 ? 0 : str.lastIndexOf("]"));
        if (str.equals("")) {
            return new int[0];
        }
        String[] arrayStr = str.split(",");
        int[] arrayDouble = new int[arrayStr.length];
        for (int i = 0; i < arrayStr.length; i++) {
            arrayDouble[i] = Integer.parseInt(arrayStr[i].trim());
        }
        return arrayDouble;
    }

    public static String intArray2String(int[] temp) {
        return Arrays.toString(temp);
    }

    public static int[] ArrayList2intArray(List<Integer> list) {
        int[] arrayInt = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arrayInt[i] = list.get(i);
        }
        return arrayInt;
    }

    /**
     * 求数组平均值
     * @param arrs 数组
     * @return 平均值
     */
    public static double aveForArray(double[] arrs) {
        int length = arrs.length;
        double sum = 0;
        for (int i = 0; i < length; i++) {
            sum += arrs[i];
        }
        return sum / length;
    }

}
