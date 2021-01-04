package util;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author twd
 * @description
 * @date 2019-12-03
 */
public class StringByteSerializer {
    public static String readFrom(ByteBuf data, int length) {
        byte[] stringBytes = new byte[length];
        data.readBytes(stringBytes);
        // find the first index of 0
        int index = 0;
        for (byte b : stringBytes) {
            if (0 == b)
                break;
            ++index;
        }
        return new String(Arrays.copyOf(stringBytes, index),
                StandardCharsets.US_ASCII);
    }

    public static void writeTo(ByteBuf data, int length, String value) {
        byte[] name = value.getBytes(StandardCharsets.US_ASCII);
        if (name.length < length) {
            data.writeBytes(name);
            for (int i = name.length; i < length; ++i) {
                data.writeByte((byte) 0);
            }
        } else {
            data.writeBytes(name, 0, length-1);
            data.writeByte((byte) 0);
        }
    }
}
