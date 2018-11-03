package com.alarm.parent.hardwareutil;

public class ChangeUtils2 {
	private static String hexStr =  "0123456789ABCDEF"; 
	/**
	 * 二进制转十进制
	 * @param n
	 * @return
	 */
	String BinaryToInt(int n){
		String result = Integer.toBinaryString(n);
		return result;
	}
	
	 /** 
     *  
     * @param bytes 
     * @return 将二进制转换为十六进制字符输出 
     */  
	 public static String BinaryToHexString(byte[] bytes){  
         
	        String result = "";  
	        String hex = "";  
	        for(int i=0;i<bytes.length;i++){  
	            //字节高4位  
	            hex = String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));  
	            //字节低4位  
	            hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F));  
	            result +=hex+" ";  
	        }  
	        return result;  
	    }  
	
	 /** 
	     *  
	     * @param hexString 
	     * @return 将十六进制转换为字节数组 
	     */  
	    public static byte[] HexStringToBinary(String hexString){  
	        //hexString的长度对2取整，作为bytes的长度  
	        int len = hexString.length()/2;  
	        byte[] bytes = new byte[len];  
	        byte high = 0;//字节高四位  
	        byte low = 0;//字节低四位  
	  
	        for(int i=0;i<len;i++){  
	             //右移四位得到高位  
	             high = (byte)((hexStr.indexOf(hexString.charAt(2*i)))<<4);  
	             low = (byte)hexStr.indexOf(hexString.charAt(2*i+1));  
	             bytes[i] = (byte) (high|low);//高地位做或运算  
	        }  
	        return bytes;  
	    }
	    
	    
	    /** 
	     *  
	     * @param hexString 
	     * @return 将十进制转换为十六进制
	     */  
	    public static String IntToHexString(int valueTen){
	    	   //定义一个十进制值
//	        int valueTen = 328;
	        //将其转换为十六进制并输出
	        String strHex = Integer.toHexString(valueTen);
	        System.out.println(valueTen + " [十进制]---->[十六进制] " + strHex);
	        //将十六进制格式化输出
	        String strHex2 = String.format("%08x",valueTen);
	        System.out.println(valueTen + " [十进制]---->[十六进制] " + strHex2);
	        return strHex;
	        
	    }
	    
	    /** 
	     *  
	     * @param hexString 
	     * @return 将十六进制转换为十进制
	     */  
	    public static int HexToIntString(String strHex3){

	        System.out.println("==========================================================");
	        //定义一个十六进制值
//	        String strHex3 = "00001322";
	        //将十六进制转化成十进制
	        int valueTen2 = Integer.parseInt(strHex3,16);
	        System.out.println(strHex3 + " [十六进制]---->[十进制] " + valueTen2);
	        return valueTen2;

	      /*  System.out.println("==========================================================");
	        //可以在声明十进制时，自动完成十六进制到十进制的转换
	        int valueHex = 0x00001322;
	        System.out.println("int valueHex = 0x00001322 --> " + valueHex);*/
	        
	    }
	    
	    
	    public static String strTo16(String s) {
	        String str = "";
	        for (int i = 0; i < s.length(); i++) {
	            int ch = (int) s.charAt(i);
	            String s4 = Integer.toHexString(ch);
	            str = str + s4;
	        }
	        return str;
	    }
	
	public static void main(String[] args) {
//		byte[] bs = new byte[]{11,1,0,1};
//		System.out.println(BinaryToHexString(bs));
//		
//		System.out.println(HexStringToBinary("04 EA"));
		
		System.out.println(IntToHexString(1654));
		System.out.println(HexToIntString("000013E2"));
		System.out.println(HexStringToBinary("41542b415554483d0d0a"));
		System.out.println(strTo16("AT\r\n"));
		
	}

}
