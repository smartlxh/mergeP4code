package util;

import io.netty.buffer.ByteBuf;
import openflow.protocol.OFGlobal;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class ParseString {
    /**
     * Read the name ascii byte[] from channel and convert to String.
     * @param data  current channel buffer
     * @return name string
     */
    public static String NameByteToString(ByteBuf data){
        byte[] name = new byte[OFGlobal.OFP_NAME_MAX_LENGTH];
        data.readBytes(name);
        return ParseString.ByteToString(name);
    }

    /**
     * Convert the ascii byte[] to String. Could be used for reading a String from channel
     * @param byteString
     * @return string
     */
    public static String ByteToString(byte[] byteString){
        int index = 0;
        for (byte b : byteString) {
            if (0 == b)
                break;
            ++index;
        }

        return new String(Arrays.copyOf(byteString, index),
                StandardCharsets.US_ASCII);
    }

    /**
     * Convert a name string to ascii bytes. Could be used for writing a name string to channel
     * @param string
     * @return byte[] array
     */
    public static byte[] NameStringToBytes(String string){
        return StringToBytes(string, OFGlobal.OFP_NAME_MAX_LENGTH);
    }

    /**
     * Convert a String to ascii bytes. Could be used for writing a string to channel.
     * @param string
     * @param maxLength
     * @return byte[] array
     */
    public static byte[] StringToBytes(String string, int maxLength){
        byte[] returnBytes = new byte[maxLength];
        try {
            byte[] byteString = string.getBytes(StandardCharsets.US_ASCII);
            if (byteString.length < maxLength) {
                System.arraycopy(byteString, 0, returnBytes, 0, byteString.length);
            } else {
                System.arraycopy(byteString, 0, returnBytes, 0, maxLength - 1);
                returnBytes[maxLength - 1] = 0;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return returnBytes;
    }

    public static byte[] parseTextToHexBytes(String valueString) {
        if (null == valueString) {
            return null;
        }

        String tmpStr = valueString;
        if (((tmpStr.length() & 1) == 1)) {
            tmpStr = "0" + tmpStr;
        }
        byte[] valueBytes = new byte[tmpStr.length() / 2];
        for (int i = 0; i < valueBytes.length; i++) {
            valueBytes[i] = (byte) Short.parseShort(tmpStr.substring(i * 2, i * 2 + 2), 16);
        }
        return valueBytes;
    }

    public static byte[] parseNumberToBytes(byte val) {
        byte[] ret = new byte[1];
        ret[0] = val;
        return ret;
    }

    public static byte[] parseNumberToBytes(short val) {
        byte[] ret = new byte[2];
        ret[1] = (byte) (val & 0xff);
        ret[0] = (byte) ((val & 0xff00) >> 8);
        return ret;
    }
}
