package com.laola.apa.utils;

import java.util.Arrays;

public final class ByteUtils {
    public static double byteToStringToDouble(byte[] a) {
        String c = bytesToString(a);
        double d = stringToDouble(c);
        return d;
    }

    public static String bytesToString(byte[] a) {
        char[] b = byteToChar(a);
        String c = charsToString(b);
        return c;
    }

    public static double stringToDouble(String a) {
        double b = Double.valueOf(a);
        return b;
    }

    public static String charsToString(char[] a) {
        String b = new String(a, 0, a.length);
        return b;
    }

    public static byte[] asc2ToByte(byte[] a) {
        byte[] d = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            d[i] = (byte) (a[i] - 48);
        }
        return d;
    }

    public static String byteToString(byte[] a) {
        String b = new String(Arrays.toString(a));
        return b;
    }

    public static byte[] stringToBytes(String a) {
        byte[] b = a.getBytes();
        return b;
    }

    public static char[] byteToChar(byte[] a) {
        char[] b = new char[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = (char) a[i];
        }
        return b;
    }

    public static byte[] charToBytes(char c) {
        byte[] b = new byte[8];
        b[0] = (byte) (c >>> 8);
        b[1] = (byte) c;
        return b;
    }

    public static byte[] floatToBytes(float f) {
        return intToBytes(Float.floatToIntBits(f));
    }

    public static byte[] doubleToBytes(double d) {
        return longToBytes(Double.doubleToLongBits(d));
    }

    public static byte[] intToBytes(int i) {
        byte[] b = new byte[4];
        b[0] = (byte) (i >>> 24);
        b[1] = (byte) (i >>> 16);
        b[2] = (byte) (i >>> 8);
        b[3] = (byte) i;
        return b;
    }

    public static byte[] longToBytes(long l) {
        byte[] b = new byte[8];
        b[0] = (byte) (l >>> 56);
        b[1] = (byte) (l >>> 48);
        b[2] = (byte) (l >>> 40);
        b[3] = (byte) (l >>> 32);
        b[4] = (byte) (l >>> 24);
        b[5] = (byte) (l >>> 16);
        b[6] = (byte) (l >>> 8);
        b[7] = (byte) (l);
        return b;
    }

    public static char bytesToChar(byte[] b) {
        char c = (char) ((b[0] << 8) & 0xFF00L);
        c |= (char) (b[1] & 0xFFL);
        return c;
    }

    public static double bytesToDouble(byte[] b) {
        return Double.longBitsToDouble(bytesToLong(b));
    }

    public static float bytesToFloat(byte[] b) {
        return Float.intBitsToFloat(bytesToInt(b));
    }

    public static int bytesToInt(byte[] b) {
        int i = (b[0] << 24) & 0xFF000000;
        i |= (b[1] << 16) & 0xFF0000;
        i |= (b[2] << 8) & 0xFF00;
        i |= b[3] & 0xFF;
        return i;
    }

    public static long bytesToLong(byte[] b) {
        long l = ((long) b[0] << 56) & 0xFF00000000000000L;
        l |= ((long) b[1] << 48) & 0xFF000000000000L;
        l |= ((long) b[2] << 40) & 0xFF0000000000L;
        l |= ((long) b[3] << 32) & 0xFF00000000L;
        l |= ((long) b[4] << 24) & 0xFF000000L;
        l |= ((long) b[5] << 16) & 0xFF0000L;
        l |= ((long) b[6] << 8) & 0xFF00L;
        l |= (long) b[7] & 0xFFL;
        return l;
    }
}
