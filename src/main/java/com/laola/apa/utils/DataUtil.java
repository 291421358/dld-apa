package com.laola.apa.utils;

import com.laola.apa.entity.Project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期操作的工具类
 */
public class DataUtil {
    /**
     * 获得当前时间
     * @return string  "yy-MM-dd HH:mm:ss"
     */
    public static String now(String format){

        return new SimpleDateFormat(format).format(new Date());
    }
    /**
     * 时间格式转换
     * @return string  "yy-MM-dd HH:mm:ss"
     */
    public static String changeType(String strData){
        String format = new SimpleDateFormat("yyyy").format(new Date());
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yy-MM-dd HH:mm");
        Date date = null;
        try {
            date = simpleDateFormat.parse(strData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String format1 = simpleDateFormat.format(date);

        return format.substring(0,2)+format1;
    }

    /**
     * 获得当前时间
     * @return string  "yy-MM-dd HH:mm:ss"
     */
    public static String now(){

        return new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * @author tz hh
     * @apiNote 更加输入日期，获取输入日期的前一天
     * @date 
     * @strData 参数格式：yyyy-MM-dd
     * @return 返回格式：yyyy-MM-dd
     */
    public static String getPreDateByDate(String strData,int gap) {
        String preDate = "";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(strData);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        assert date != null;
        calendar.setTime(date);
        int day1 = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, day1 + gap);
        preDate = simpleDateFormat.format(calendar.getTime());
        return preDate;
    }


    /**
     * @author tz hh
     * @apiNote 更加输入日期，获取输入日期的前一天
     * @date 
     * @strData 参数格式：yyyy-MM-dd
     * @return 返回格式：yyyy-MM-dd
     *
     * @param strData
     * @param gap
     * @param unit MINUTE HOUR
     */
    public static String getPreDateByUnit(String strData,int gap,int unit) {
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm");
        strData = strData.replace("年","-");
        strData = strData.replace("月","-");
        strData = strData.replace("日"," ");
        strData = strData.replace("时",":");
        strData = strData.replace("分","");
        //strData = strData.replace("/","-");
        Date date = null;
        try {
            date = simpleDateFormat.parse(strData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int MINUTE1 = calendar.get(unit);
        calendar.set(unit, MINUTE1 + gap);
        String preDate = simpleDateFormat.format(calendar.getTime());
        return preDate;
    }

    /***
     * @apiNote
     * @author tzhh
     * @date 2021/5/13 10:39
     * @param strData
     * @return {@link long}
     **/
    public  static long getDateGap2Now(String strData){
        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTime(new Date());

        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = simpleDateFormat.parse(strData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendarOld = Calendar.getInstance();
        calendarOld.setTime(date);
        long difference=calendarNow.getTimeInMillis()-calendarOld.getTimeInMillis();
        return difference;
    }


    /***
     * @apiNote
     * @author tzhh
     * @date 2021/5/13 10:15
     * @param str
     * @return {@link double[]}
     **/
    //将字符型转换为double型
    public static double[] string2Double(String str) {
        double[] d = { 1 };
        if (str.contains(",")) {
            String[] arr = str.split(",");
            d = new double[arr.length];
            for (int i = 0; i < arr.length; i++) {
                // System.out.println(arr[i]);
                d[i] = Double.valueOf(arr[i].trim());
            }
        }
        return d;
    }

    /**
     * double[] 排序 从大到小
     * @param arrayX
     * @return
     */
    public static double[] monotonicSequence(double[] arrayX,double[] arrayY) {
        for (int i = 0; i < arrayX.length; i++) {
            for (int j = arrayX.length-1; j > i; j--) {
                if (arrayX[i] > arrayX[j]){
                    double media = arrayX[j];
                    arrayX[j] = arrayX[i];
                    arrayX[i] = media;
                    double mediaY = arrayY[j];
                    arrayY[j] = arrayY[i];
                    arrayY[i] = mediaY;
                }
            }
        }
        return null;
    }

    /**
     * double[] 去重 从大到小，并去同一x下y的平均值
     * @param arrayX Distinct
     * @return
     */
    public static int DistinctXAveY(double[] arrayX, double[] arrayY) {
        int length = arrayX.length;
        for (int i = 0; i < length-1; i++) {
            int j = 2;
            for (int k = i+1;  k<= length-1 && arrayX[k] == arrayX[i]; k++) {
                delete(k,arrayX);
                arrayY[i] = (arrayY[k]+arrayY[i]*(j-1))/j;
                delete(k,arrayY);
                length--;
                k--;
                j++;
            }
        }
        return length;
    }

    /**
     *删除方式2
     */
    public static void delete(int index, double[] array) {
        double tem[] = new  double[array.length-1];
        //数组的删除其实就是覆盖前一位
        for (int i = 0; i < array.length - 1; i++) {
            if (i >= index) {
                array[i] = array[i + 1];
                tem[i] = array[i+1];
            }
            if (i <index){
                tem[i] = array[i];
            }
        }
    }

    public static void setDAndA(List<Project> projects, StringBuilder den, StringBuilder abs, StringBuilder calDen, StringBuilder relAbs) {
        for (Project project0 : projects) {
            if (project0.getType().equals(2)){
                den.append(project0.getDensity()).append(",");
                abs.append(project0.getAbsorbance()).append(",");
            }else {
                if (null != project0.getAbsorbance() && !project0.getAbsorbance().equals("")){
                    calDen.append(project0.getDensity()).append(",");
                    relAbs.append(project0.getAbsorbance()).append(",");
                }
            }
        }
    }
    public   void main(String[] args) throws ParseException {
//        double x[] = {0,0,0,0,0};
//        double y[] = {0,2,1,2,3};
//        int length = DataUtil.DistinctXAveY(x, y);
//        double[] xX = new double[length];
//        double[] yY = new double[length];
//        for (int j = 0; j < length; j++) {
//            xX[j] = x[j];
//            yY[j] = y[j];
//        }
    }


    public static class DealXY {
        private boolean myResult;
        private double[] x;
        private double[] y;
        private double[] xX;
        private double[] yY;

        public DealXY(double[] x, double... y) {
            this.x = x;
            this.y = y;
        }

        public boolean is() {
            return myResult;
        }

        public double[] getX() {
            return x;
        }

        public double[] getxX() {
            return xX;
        }

        public double[] getyY() {
            return yY;
        }

        public DealXY invoke() {
            if (x.length < 1 || x.length != y.length) {
                myResult = true;
                return this;
            }
            // 排序
            DataUtil.monotonicSequence(x, y);
            //去重
            int distinctXAveY = DataUtil.DistinctXAveY(x, y);

            xX = new double[distinctXAveY];
            yY = new double[distinctXAveY];
            for (int j = 0; j < distinctXAveY; j++) {
                xX[j] = x[j];
                yY[j] = y[j];
            }
            x = xX;
            y = yY;

            if (xX.length == 1) {
                double[] xX1 = new double[2];
                double[] yY1 = new double[2];
                xX1[1] = xX[0];
                yY1[1] = yY[0];
                if (xX1[1] <0){
                    xX1[0] = xX1[1];
                    yY1[0] = yY1[1];
                    xX1[1] = 0;
                    yY1[1] = 0;
                }else {
                    xX1[0] = 0;
                    yY1[0] = 0;
                }
                if (xX[0] == 0){
                    xX1[1] = 0.1;
                }

                xX = xX1;
                yY = yY1;
            }
            myResult = false;
            return this;
        }
    }

}
