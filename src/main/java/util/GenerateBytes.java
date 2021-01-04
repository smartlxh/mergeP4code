package util;

public class GenerateBytes {
    public static byte[] generateAll1Bits(int length)
    {
        byte[] result  = new byte[length];
        for(int i=0;i<length;i++)
        {
            result[i] = (byte)0xff;
        }
        return result;
    }

    public static byte[] generatePartRight1Bits(int length, int right1Num){
        byte[] result  = new byte[length];
        for(int i=length-1;i>=0;i--)
        {
            if(right1Num>=8){
                result[i] = (byte)0xff;
                right1Num = right1Num-8;
            }
            else if(right1Num<8&&right1Num>0){
                result[i]=0;
                for(int k=0;k<right1Num;k++){
                    result[i] = (byte)((result[i]<<1)|0x01);
                }
                right1Num = right1Num-8;
            }
            else{
                result[i]=0;
            }

        }
        return result;
    }


}