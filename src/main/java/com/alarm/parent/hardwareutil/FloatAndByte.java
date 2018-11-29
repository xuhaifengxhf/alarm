package com.alarm.parent.hardwareutil;

public class FloatAndByte {   
	
	public static float byte2float(byte[] b, int index) {  
    int l;                                           
    l = b[index + 0];                                
    l &= 0xff;                                       
    l |= ((long) b[index + 1] << 8);                 
    l &= 0xffff;                                     
    l |= ((long) b[index + 2] << 16);                
    l &= 0xffffff;                                   
    l |= ((long) b[index + 3] << 24);                
    return Float.intBitsToFloat(l);                  
}


public static float getFloat(byte[] b) {
    int accum = 0; 
    accum = accum|(b[0] & 0xff) << 0;
    accum = accum|(b[1] & 0xff) << 8; 
    accum = accum|(b[2] & 0xff) << 16; 
    accum = accum|(b[3] & 0xff) << 24; 
    return Float.intBitsToFloat(accum); 
}

public static void main(String[] args) {
	byte[] bytes = new byte[4];
	bytes[0] = (byte) 0x40;
	bytes[1] = (byte) 0x37;
	bytes[2] = (byte) 0x0A;
	bytes[3] = (byte) 0x3E;
	
	System.out.println(getFloat(bytes));
}
}
