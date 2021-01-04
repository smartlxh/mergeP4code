package util;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */

import openflow.protocol.OFGlobal;

/**
 * convert to hex string
 */
public class HexString {
    /**
     * add prefix zeros
     * @param sbuf
     * @param zeroNum
     */
    private static void addZeros(StringBuilder sbuf, int zeroNum) {
        while (zeroNum-- > 0) {
            sbuf.append('0');
        }
    }

    public static String getByteZeros(int byteNum) {
        StringBuilder sbuf = new StringBuilder();
        while (byteNum-- > 0) {
            sbuf.append("00");
        }
        sbuf.append(" ");
        return sbuf.toString();
    }

    public static String getZeros(int num) {
        StringBuilder sbuf = new StringBuilder();
        while (num-- > 0) {
            sbuf.append("0");
        }
        sbuf.append(" ");
        return sbuf.toString();
    }

    public static String toHex(byte byteVal) {
        return Integer.toHexString((byteVal & 0x000000ff) | 0xffffff00).substring(6);
    }

    public static String toHex(short shortVal) {
        return Integer.toHexString((shortVal & 0x0000ffff) | 0xffff0000).substring(4);
    }

    public static String toHex(int intVal) {
        char arr[] = Integer.toHexString(intVal).toCharArray();
        StringBuilder sbuf = new StringBuilder();
        addZeros(sbuf, 8 - arr.length);
        sbuf.append(arr);
        return sbuf.toString();
    }

    public static String toHex(byte[] byteArray) {
        if (null == byteArray || 0 == byteArray.length) {
            return "";
        }
        StringBuilder sbuf = new StringBuilder();
        for (byte b : byteArray) {
            sbuf.append(toHex(b));
        }
        return sbuf.toString();
    }

    public static String toReverseHex(byte[] byteArray) {
        if (null == byteArray || 0 == byteArray.length) {
            return "";
        }
        StringBuilder sbuf = new StringBuilder();
        for (int i = byteArray.length - 1; i >= 0; i--) {
            sbuf.append(toHex(byteArray[i]));
        }
        return sbuf.toString();
    }

    /**
     * Convert the byte[] array stream to hex string.
     * @param byteArray
     * @param start start index of byteArray
     * @param length
     * @return hex string
     */
    public static String toHex(byte[] byteArray, int start, int length) {
        if(null == byteArray
                || 0 == byteArray.length
                || start < 0
                || (start + length) > byteArray.length){
            return "";
        }
        StringBuilder ret = new StringBuilder();
        for(int i = start; i < (start + length); i++){
            ret.append(toHex(byteArray[i]));
        }
        return ret.toString();
    }

    public static String toHex(String str, int len) {
        if (null == str) {
            return "";
        }
        byte[] bytes = ParseString.StringToBytes(str, len);
        return toHex(bytes);
    }

    public static String nameToHex(String str) {
        if (null == str) {
            return "";
        }
        return toHex(str, OFGlobal.OFP_NAME_MAX_LENGTH) + " ";
    }

    /**
     * Convert a string of bytes to a ':' separated hex string
     * @param bytes
     * @return "0f:ca:fe:de:ad:be:ef"
     */
    public static String toHexString(byte[] bytes) {
        int i;
        StringBuilder ret = new StringBuilder();
        String tmp;
        for(i=0; i< bytes.length; i++) {
            if(i> 0)
                ret.append(":");
            tmp = Integer.toHexString(bytes[i]);
            if (tmp.length() == 1)
                ret.append("0");
            ret.append(tmp);
        }
        return ret.toString();
    }

    public static String toHexString(long val, int padTo) {
        char[] arr = Long.toHexString(val).toCharArray();
        StringBuilder ret = new StringBuilder();
        // prepend the right number of leading zeros
        int i = 0;
        for (; i < (padTo * 2 - arr.length); i++) {
            ret.append("0");
            if ((i % 2) == 1)
                ret.append(":");
        }
        for (int j = 0; j < arr.length; j++) {
            ret.append(arr[j]);
            if ((((i + j) % 2) == 1) && (j < (arr.length - 1)))
                ret.append(":");
        }
        return ret.toString();
    }

    public static String toHexString(long i) {
        return toHexString(i, 8);
    }
}
