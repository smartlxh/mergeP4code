package datatype;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import util.Converter;
import util.HexString;

import java.util.Arrays;

/**
 * 网络是大端.要发送的内容在内存中的存储应该是低地址放最高位.
 * @author twd
 * @description little-endian representation
 * @date 2019-12-26
 */
public class Value {
    public static final int VALUE_LEN = 16;
    private byte[] v = new byte[VALUE_LEN];//v[0]最高位字节.
    private int actualLen;//单位:bit

    public byte[] getV() {
        return v;
    }

    public int getActualLen() {
        return actualLen;
    }

    public int intValue() {
        int ret = 0;
        for (int i = 0; i < (actualLen + 7) / 8; i++) {
            ret = (ret << 8) + (v[i] & 0xff);
        }
        if (actualLen % 8 > 0) {
            ret >>= (8 - (actualLen % 8));
        }
        return ret;
    }

    public byte[] byteValue() {
        int len = (actualLen + 7) / 8;
        return Arrays.copyOf(v, len);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (byte b : v) {
            hash += b * 2;
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Value)) {
            return false;
        }
        Value other = (Value) obj;
        for (int i = 0; i < VALUE_LEN; i++) {
            if (this.v[i] != other.v[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return HexString.toHex(v);
    }

    /**
     * Big-endian representation
     * @param val
     * @param bitLen actual bit length
     * @return
     */
    public static Value valueOf(int val, int bitLen) {
        byte[] bytes = Converter.convertToBytes(val);
        ByteBuf buf = Unpooled.wrappedBuffer(bytes);
        return valueOf(buf, 32 - bitLen, bitLen);
    }

    public static Value valueOf(ByteBuf buf, int offset, int bitLen) {//将数据包中的数据提取出来.v[0]放高位
        Value val = new Value();
        val.actualLen = bitLen;
        if (offset >= 0 && bitLen >= 0) {
            int low = offset / 8;
            int high = (offset + bitLen - 1) / 8;
            int shift = offset % 8;
            int i = 0;
            for (; i < high - low; i++) {
                val.v[i] = buf.getByte(low + i);
                val.v[i] <<= shift;
                val.v[i] |= (buf.getByte(low + i + 1) & 0xff) >> (8 - shift);
            }
            if (high - low + 1 == (bitLen + 7) / 8) {
                val.v[i] = buf.getByte(high);
                val.v[i] <<= shift;
            } else {
                i--;
            }
            if ((bitLen % 8) > 0) {
                val.v[i] &= 0xff << (8 - (bitLen % 8));
            }
        }
        return val;
    }

    public static Value valueOf(byte[] buf, int offset, int bitLen) {//将数据包中的数据提取出来.v[0]放高位
        Value val = new Value();
        val.actualLen = bitLen;
        if (offset >= 0 && bitLen >= 0) {
            int low = offset / 8;
            int high = (offset + bitLen - 1) / 8;
            int shift = offset % 8;
            int i = 0;
            for (; i < high - low; i++) {
                val.v[i] = buf[low + i];
                val.v[i] <<= shift;
                val.v[i] |= (buf[low + i + 1] & 0xff) >> (8 - shift);
            }
            if (high - low + 1 == (bitLen + 7) / 8) {
                val.v[i] = buf[high];
                val.v[i] <<= shift;
            } else {
                i--;
            }
            if ((bitLen % 8) > 0) {
                val.v[i] &= 0xff << (8 - (bitLen % 8));
            }
        }
        return val;
    }

    public static Value valueOf(byte[] buf) {//将数据包中的数据提取出来.v[0]放高位
        Value val = new Value();
        val.actualLen = buf.length*8;
        for(int i=0;i<buf.length;i++){
            val.v[i]=buf[i];
        }
        return val;
    }
}

