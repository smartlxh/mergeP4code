package util;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class Converter {
    public static int convertShortToInt(short i) {
        return (int)i & 0xffff;
    }

    public static short convertIntToShort(int i) {
        return (short) i;
    }

    public static long convertIntToLong(int i) {
        return (long) i & 0xffffffffL;
    }

    public static int convertLongToInt(long i) {
        return (int) i;
    }

    public static byte convertShortToByte(short i) {
        return (byte) i;
    }

    public static short convertByteToShort(byte i) {
        return (short) (i & 0xffff);
    }

    public static byte[] convertToBytes(short s) {
        byte[] ret = new byte[2];
        ret[0] = (byte) ((s & 0xff00) >>> 8);
        ret[1] = (byte) (s & 0xff);
        return ret;
    }

    public static byte[] convertToBytes(int i) {
        byte[] ret = new byte[4];
        ret[0] = (byte) ((i & 0xff000000) >>> 24);
        ret[1] = (byte) ((i & 0xff0000) >>> 16);
        ret[2] = (byte) ((i & 0xff00) >>> 8);
        ret[3] = (byte) (i & 0xff);
        return ret;
    }

    public static byte[] convertToMacBytes(long mac) {
        byte[] ret = new byte[6];
        ret[0] = (byte) ((mac & ((long) 0xff << 40)) >> 40);
        ret[1] = (byte) ((mac & ((long) 0xff << 32)) >> 32);
        ret[2] = (byte) ((mac & 0xff000000) >> 24);
        ret[3] = (byte) ((mac & 0xff0000) >> 16);
        ret[4] = (byte) ((mac & 0xff00) >> 8);
        ret[5] = (byte) (mac & 0xff);
        return ret;
    }

    public static short convertBytesToShort(byte[] data) {
        if (null == data || data.length != 2) {
            return 0;
        }
        return (short) ((data[0] << 8) | (data[1] & 0xff));
    }

    public static int convertBytesToInt(byte[] data) {
        if (null == data || data.length != 4) {
            return 0;
        }
        return (data[0] << 24) |
                ((data[1] & 0xff) << 16) |
                ((data[2] & 0xff) << 8) |
                (data[3] & 0xff);
    }

    public static long convertBytesToLong(byte[] data) {
        if (null == data || data.length > 8) {
            return 0;
        }
        long ret = 0;
        for (byte b : data) {
            ret = (ret << 8) | (b & 0xff);
        }
        return ret;
    }
}
