package com.laola.apa.utils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据工具类
 */
public class DateUtils {
    /**
     * @apiNote 浮点数退位
     * @author tzhh
     * @date 2021/5/27 17:05
     * @param strDig
	 * @param Multiple
     * @return {@link java.lang.String}
     **/
    public static String carryDigit(String strDig, Float Multiple) {

        if (null == strDig) {
            return "";
        }
        float v = 0;
        try {
            v = Float.parseFloat(strDig);
        } catch (NumberFormatException e) {
            System.out.println(strDig);
            e.printStackTrace();
        }
        v = v / Multiple;

        return String.valueOf(v);
    }

    /**
     * @apiNote 将十六进制的字符串转换成字节数组
     * @author tzhh
     * @date 2021/5/27 17:05
     * @param hexString
     * @return {@link byte[]}
     **/
    public static byte[] hexStrToBinaryStr(String hexString) throws Exception{
        if (hexString.equals("")) {
            return null;
        }
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        int index = 0;
        byte[] bytes = new byte[len / 2];
        while (index < len) {
            String sub = hexString.substring(index, index + 2);
            bytes[index / 2] = (byte) Integer.parseInt(sub, 16);
            index += 2;
        }
        return bytes;
    }

    /**
     *
     */
    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    /*     * byte[]数组转十六进制     */

    /**
     * @apiNote 字节转成字符串
     * @author tzhh
     * @date 2021/5/27 17:06
     * @param bytes
     * @return {@link java.lang.String}
     **/
    public static String bytes2hexStr(byte[] bytes) {

        System.out.println("GET BYTE LEN: "+ bytes.length);
        int len = bytes.length;
        if (len == 0) {
            return null;
        }
        char[] cbuf = new char[len * 2];
        for (int i = 0; i < len; i++) {
            int x = i * 2;
            cbuf[x] = HEX_CHARS[(bytes[i] >>> 4) & 0xf];
            cbuf[x + 1] = HEX_CHARS[bytes[i] & 0xf];
        }
        return String.valueOf(cbuf);
    }


    /**
     * 将byte[]转为各种进制的字符串
     *
     * @param bytes byte[]
     * @param radix 基数可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix) {
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }

    /**
     * @apiNote 將16进制字符串轉換為10进制
     * @author tzhh
     * @date 2021/5/27 17:06
     * @param hexs
     * @return {@link int}
     **/
    public static int decodeHEX(String hexs) {
        BigInteger bigint = new BigInteger(hexs, 16);
        int numb = bigint.intValue();
        return numb;
    }

    /**
     * @apiNote 10进制转16进制
     * @author tzhh
     * @date 2021/5/27 17:06
     * @param dec
     * @return {@link String}
     **/
    public static String DEC2HEX(String dec) {
        if (dec == null || dec.equals("null") || dec.equals("")) {
            return "00";
        }
        String hex = Integer.toHexString(Integer.valueOf(dec));
        if (hex.length() == 1) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * @apiNote 10进制转16进制4位
     * @author tzhh
     * @date 2021/5/27 17:07
     * @param dec
     * @return {@link String}
     **/
    public static String DEC2HEX4Place(String dec) {
        if (dec == null || dec.equals("null") || dec.equals("")) {
            return "00 00";
        }
        String hex = Integer.toHexString(Integer.valueOf(dec));
        if (hex.length() == 1) {
            hex = "00 " + "0"+hex;
        }
        if (hex.length() == 2) {
            hex = "00 " + hex;
        }
        if (hex.length() == 3) {
            hex = "0" + hex.substring(0,1) + " "+ hex.substring(1,3);
        }
        return hex;
    }

    /**
     * @apiNote 十六进制ASCII码hex字符串转String明文
     * @author tzhh
     * @date 2021/5/27 17:07
     * @param hex
     * @return {@link String}
     **/
    public static String hexAscii2Str(String hex) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hex.length() - 1; i += 2) {
            String h = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(h, 16);
            sb.append((char) decimal);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        voidConvertToASCII("[{chinese_name=TP, normal_low=31.0, normal_high=0.0, density=≤0.5, bar_code=11111111, name=TP, id=7552, meterage_unit=}]");
    }
    //string 轉ascii 十進制
    public static String voidConvertToASCII(String string)
    {
        StringBuilder sb = new StringBuilder();
        char[] ch = string.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            sb.append(Integer.toHexString(Integer.valueOf(ch[i]).intValue())).append(" ");// 加空格
            // sb.append(Integer.valueOf(ch[i]).intValue());// 不加空格
//            System. out.println( sb.toString()) ;
        }

            return  sb.toString();
    }

    /**
     * @apiNote 取得终点数差
     * @author tzhh
     * @date 2021/5/27 17:07
     * @param mapList
    	 * @param factor
     * @return {@link float}
     **/
    public static float getAbsorbanceGap(List<Map<String, Object>> mapList, float factor) {
        Map<String, Object> minMap = new HashMap<>();
        Map<String, Object> maxMap = new HashMap<>();
        for (Map<String, Object> map :
                mapList) {
            if ("1".equals(String.valueOf(map.get("x")))) {
                minMap = map;
            }
            if (null == maxMap.get("x") || Integer.parseInt(String.valueOf(maxMap.get("x"))) < Integer.parseInt(String.valueOf(map.get("x")))) {
                maxMap = map;
            }
        }
        System.out.println(minMap.get("y") + "----" + maxMap.get("y"));
        return Float.parseFloat(String.valueOf(maxMap.get("y"))) - Integer.parseInt(String.valueOf(minMap.get("y")));
    }

    /**
     * @apiNote 取得平均数差
     * @author tzhh
     * @date 2021/5/27 17:07
     * @param mapList
	 * @param mainBegin
	 * @param mainEnd
     * @param r2Incubation
     * @return {@link float}
     **/
    public static float getAbsorbanceGap(List<Map<String, Object>> mapList, String mainBegin, String mainEnd, int R2t, String r2Incubation) {
        int R2i = Integer.parseInt(r2Incubation);
        int R2Read = R2t+R2i;

        float R2ReadBey = 0;
        float R2ReadEndy = 0;
        float R2tBey = 0;
        float R2tEnd = 99999;

        float endy = 0;
        float bey = 0;
        float tBey = 0;
        float tEnd = 99999;
        float mainEND = Float.parseFloat(mainEnd); // Integer.parseInt(a)+R2t+Integer.parseInt(R2Incubation) - y1 总时长 孵育时间+r2加入时间+r2反应时间
        float y1 = 0;
        float y2 = 0;
        int t1 = 0;
        for (Map<String, Object> map : mapList) {
            if (String.valueOf(map.get("x")).equals("1")){
                t1 = Integer.parseInt(String.valueOf(map.get("t")));
                break;
            }
        }
//        mainEND = mainEND -t1;
        System.out.println(mainEND);
        for (Map<String, Object> map : mapList) {
            //第n个点
            int tThis = Integer.parseInt(String.valueOf(map.get("t")));
            //第n个点
            int xThis = Integer.parseInt(String.valueOf(map.get("x")));
            //第n个点的数据
            float yFloat = Float.parseFloat(String.valueOf(map.get("y")));
            int tUse = tThis-t1;
            if (xThis == 1){
                y1 = yFloat;
            }
            if (xThis == 2){
                y2 = yFloat;
            }
            if (R2Read >= tThis  && tThis >= R2tBey) {
                R2ReadBey = yFloat;
                R2tBey = tThis;
            }
            if ( R2Read <= tThis  && tThis <= R2tEnd) {
                R2ReadEndy = yFloat;
                R2tEnd = tThis;
            }
            //找到最接近(mainEnd 且比(mainEnd小的数
            if (mainEND >= tUse  && tUse >= tBey) {
                bey = yFloat;
                tBey = tUse;
            }
            //找到最接近(mainEnd 且比(mainEnd大的数
            if ( mainEND <= tUse  && tUse <= tEnd) {
                endy = yFloat;
                tEnd = tUse;
            }
        }
        float pro = 0;
        if (tEnd != tBey){
             pro = (mainEND - tBey) / (tEnd - tBey);
        }
        float R2pro = 0;
        if (R2tEnd != R2tBey){
            R2pro = (R2Read - R2tBey) / (R2tEnd - R2tBey);
        }
        float R2end = 0;
        R2end = (R2ReadEndy - R2ReadBey) * R2pro + R2ReadBey;
        float end = 0;
        end = (endy - bey) * pro + bey;

        if (R2t == 0){
            if (mainBegin.equals("1")){
                R2end = y1;
            }else if (mainBegin.equals("2")){
                R2end = (y1+y2)/2;
            }
        }
        System.out.println("R2ReadBey:" + R2ReadBey + "----R2ReadEndy" + R2ReadEndy);

        System.out.println("R2end:" + R2end + "----end" + end);

        return end  -  R2end ;
    }

    /**
     * @apiNote 取得最后一点
     * @author tzhh
     * @date 2021/5/27 17:07
     * @param mapList
	 * @param mainEnd
     * @return {@link float}
     **/
    public static float getAbsorbanceGap(List<Map<String, Object>> mapList, String mainEnd) {
        float end = 0;
        float endy = 0;
        float bey = 0;
        float tBey = 0;
        float tEnd = 99999;
        float mainEND = Float.parseFloat(mainEnd);
        int t1 = 0;
        for (Map<String, Object> map : mapList) {
            if (String.valueOf(map.get("y")).equals("1")){
                t1 = Integer.parseInt(String.valueOf(map.get("t")));
                break;
            }
        }

        for (Map<String, Object> map : mapList) {
            //第n个点
            int tThis = Integer.parseInt(String.valueOf(map.get("t")));
            int tUse = tThis-t1;
            //第n个点的数据
            float yFloat = Float.parseFloat(String.valueOf(map.get("y")));
            //如果 主/辅终点都为空或者0；只取主/辅始点
            //找到最接近(mainEnd 且比(mainEnd小的数
            if (mainEND >= tUse  && tUse >= tBey) {
                bey = yFloat;
                tBey = tUse;
            }
            //找到最接近(mainEnd 且比(mainEnd大的数
            if ( mainEND <= tUse  && tUse <= tEnd) {
                endy = yFloat;
                tEnd = tUse;
            }
        }
        float pro = 0;
        if (tEnd != tBey){
            pro = (mainEND - tBey) / (tEnd - tBey);
        }
        end = (endy - bey) * pro + bey;
        System.out.println( "----endSum" + end);
        return end  ;
//        return Math.abs(Float.parseFloat(mainEnd) - Float.parseFloat(mainBegin));
//        return Float.parseFloat(mainEnd);
    }





    /**
     * @param string
     * @return
     * @Title: unicodeDecode
     * @Description: unicode解码
     */
    public static String unicodeDecode(String string) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(string);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            string = string.replace(matcher.group(1), ch + "");
        }
        return string;
    }

    /**
     * @param string
     * @return
     * @Title: unicodeEncode
     * @Description: unicode编码
     */
    public static String unicodeEncode(String string) {
        char[] utfBytes = string.toCharArray();
        String unicodeBytes = "";
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

    /**
     * @param string
     * @return
     * @Title: unicodeEncode
     * @Description: unicode编码
     */
    public static String unicodeEncodeOnlyCN(String string) {
        char[] utfBytes = string.toCharArray();
        String unicodeBytes = "";
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);

            if (hexB.length() <= 2) {

                hexB = "00" + hexB;
            }

            if (!(Integer.parseInt(hexB, 16) > 0x4e00 && Integer.parseInt(hexB, 16) < 0x9fa5)) {
                hexB = String.valueOf(utfBytes[i]);
            } else {
                hexB = "\\u" + hexB;
            }

            unicodeBytes = unicodeBytes + hexB;
        }
        return unicodeBytes;
    }
//<summary>
/// 将指定的自然数转换为26进制表示。映射关系：[1-26] ->[A-Z]。
/// </summary>
/// <param name="n">自然数（如果无效，则返回空字符串）。</param>
/// <returns>26进制表示。</returns>
    public static String ToNumberSystem26(int n) {
        String s = "";
        while (n > 0) {
            int m = n % 26;
            if (m == 0) m = 26;
            s = (char) (m + 96) + s;
            n = (n - m) / 26;
        }
        return s;
    }


    /// <summary>
/// 将指定的26进制表示转换为自然数。映射关系：[A-Z] ->[1-26]。
/// </summary>
/// <param name="s">26进制表示（如果无效，则返回0）。</param>
/// <returns>自然数。</returns>
    public static int FromNumberSystem26(String s){
        if (s == null || s.equals("")) return 0;
        int n = 0;
        for (int i = s.length() - 1, j = 1; i >= 0; i--, j *= 26){
            char c =  s.charAt(i);
            if (c < 'a' || c > 'z') return 0;
            n += ((int)c - 96) * j;
        }
        return n;
    }

    public static float terminalMethod(List<Map<String, Object>> selectOneCurve, String mainBegin, String mainEnd, int t, String readBegin, String r2Incubation) {
        float absorbanceGap = 0;
       //  终点-起点
//        if (mainBegin.equals("0")) {
//            absorbanceGap = DateUtils.getAbsorbanceGap(selectOneCurve, mainEnd) / 1000;
//        }
//        if (!readBegin.equals("")){
            absorbanceGap = DateUtils.getAbsorbanceGap(selectOneCurve, readBegin, mainEnd,t,r2Incubation) / 1000;
//        }
        return absorbanceGap;
    }

    public   void mai1n(String[] args) {
        int[] numbers = { 1, 10, 26, 27, 256, 702, 703 };
        for (int n :numbers) {
            String s = ToNumberSystem26(n);
            System.out.println( n + "\t" + s + "\t" + FromNumberSystem26(s));
        }

//        String formate = "E59c81010200010203040506070D";
//        System.out.println(formate.substring(12,formate.length()-2));

// hex转char
// 先将hex字符串转成int
            int i = Integer.parseInt("46", 16);
// hex转char方法一，结果为F
            String str1 = new String(new char[]{(char) i});
// hex转char方法二，结果为F
            String str2 = new StringBuffer().append((char) i).toString();
// char转hex方法一，结果为46（第二个参数16表16进制）
            String hex1 = Integer.toString(16, 16);
// char转hex方法二，结果为46
            String hex2 = Integer.toHexString('F');
    }
}

