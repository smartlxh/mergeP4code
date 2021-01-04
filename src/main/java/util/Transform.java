package util;

import org.apache.commons.codec.binary.Hex;

public class Transform {// transform the byte value to Hexstring(length is 2)
    public static String byte2String(byte data)
    {
        String hex = Integer.toHexString(data & 0xFF);
        if(hex.length() < 2){
            hex = "0" + hex;
        }
        return hex;
    }

    public static byte[] hexStrWith0x2Bytes(String HexStr){
        //"HexStr" is a Hex String with "0x" in the begin of String
        String no0xStr = HexStr.substring(2);
        byte[] bytes = null;
        try {
            bytes = Hex.decodeHex(no0xStr);
        }catch(Exception e){
            e.printStackTrace();
        }
        return bytes;
    }


    public static byte[] bytes2SpecLenBytes(byte[] from,int len){
        // "len" is bit unit
        // "from" should be shorter than result. Otherwise it will be truncated
        // int byte[], byte[0] is the highest byte.
        int byteLen = (len+7)/8;
        int restBit = len%8;
        byte[] result = new byte[byteLen];

        if(from.length<=byteLen){
            int copyedStart = byteLen-from.length;
            System.arraycopy(from,0,result,copyedStart,from.length);
            result[copyedStart] = (byte)(result[copyedStart]&GenerateBytes.generatePartRight1Bits(1,restBit)[0]);
        }
        else{
            System.arraycopy(from,from.length-byteLen,result,0,byteLen);
        }

        return result;
    }

    public static int bytes2Int(byte[] bytes){// we view all is unsigned
        //bytes[] : its length should be less than 4
        int result = 0;
        for(int i=0;i<bytes.length;i++){
            result = result *256+(((int)bytes[i])&0x000000ff);
        }
        return result;
    }


}
