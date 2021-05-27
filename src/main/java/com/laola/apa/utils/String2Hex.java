package com.laola.apa.utils;

public class String2Hex {

    /**
     * @apiNote string转16进制
     * @author tzhh 
     * @date 2021/5/27 16:58
     * @param str
     * @return {@link String}
     **/
    public static String convertStringToHex(String str){

        char[] chars = str.toCharArray();

        StringBuilder hex = new StringBuilder();
        for (char aChar : chars) {
            hex.append(Integer.toHexString((int) aChar));
        }

        return hex.toString();
    }

    /***
     * @apiNote  16进制转string
     * @author tzhh
     * @date 2021/5/27 16:58
     * @param hex
     * @return {@link String}
     **/
    public static String convertHexToString(String hex){
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for( int i=0; i<hex.length()-1; i+=2 ){
            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char)decimal);

            temp.append(decimal);
        }

        return sb.toString();
    }

    //504F533838383834  POS88884
    public static void main(String[] args) {

        String2Hex strToHex = new String2Hex();
        System.out.println("\n-----ASCII码转换为16进制 -----");
        String str = "eb9401712c35302c322c31302c31302c33616161616161616161610d";
        System.out.println("字符串: " + str);
        String hex = strToHex.convertStringToHex(str);
        System.out.println("转换为16进制 : " + hex);

        System.out.println("\n***** 16进制转换为ASCII *****");
        System.out.println("Hex : " + str);
        System.out.println("ASCII : " + strToHex.convertHexToString(str));
    }
}
