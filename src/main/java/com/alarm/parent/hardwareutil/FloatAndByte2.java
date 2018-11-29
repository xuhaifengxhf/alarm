package com.alarm.parent.hardwareutil;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class FloatAndByte2 {
	 
	/**
	 * @param args
	 *            对于IEEE754编码。由float转换为IEEE754编码
	 * @author Administrator
	 * @throws DecoderException 
	 * @version1.0
	 */
	 public static void main(String[] args) throws DecoderException {
	/*	 float f = (float)2.8600001335144043;
		 byte[] bytes = new byte[4];
			bytes[0] = (byte) 0x40;
			bytes[1] = (byte) 0x37;
			bytes[2] = (byte) 0x0A;
			bytes[3] = (byte) 0x3E;
		 
		 System.out.println(bytesToFloat(bytes));
		 byte[] bytesss = floatToBytes(f);
		 System.out.println(floatToBytes(f));*/
		 System.out.println(hexStr2Float("40370A3E"));
		 byte[] bytes = new byte[4];
			bytes[0] = (byte) 0x40;
			bytes[1] = (byte) 0x37;
			bytes[2] = (byte) 0x0A;
			bytes[3] = (byte) 0x3E;
			System.out.println(getFloat(bytes, 0));
	 }
	 
	 
	 private static Float hexStr2Float(String hexStr) throws DecoderException {
			Float result = null;
			// 先通过apahce的 hex类转换十六进制字符串为byte数组. 也可以自己用JDK原声的方法来循环转
			// Character.digit(ch, 16);
			byte[] decodes = Hex.decodeHex(hexStr.toCharArray());
			// 获得byte转float的结果
			result = getFloat(decodes, 0);
			return result;
		}
		public static float getFloat(byte[] b, int index) {
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
 
	public static float bytesToFloat(byte[] data) {// 解析4个字节中的数据，按照IEEE754的标准
		int s = 0;// 浮点数的符号
		float f = 0;// 浮点数
		int e = 0;// 指数
		if ((data[3] & 0xff) >= 128) {// 求s
			s = -1;
		} else {
			s = 1;
		}
		int temp = 0;// 指数位的最后一位
		if ((data[2] & 0xff) >= 128) {
			temp = 1;
		} else
			temp = 0;
		e = ((data[3] & 0xff) % 128) * 2 + temp;// 求e
		// f=((data[2]&0xff)-temp*128+128)/128+(data[1]&0xff)/(128*256)+(data[0]&0xff)/(128*256*256);
		float[] data2 = new float[3];
		data2[0] = data[0] & 0xff;
		data2[1] = data[1] & 0xff;
		data2[2] = data[2] & 0xff;
		f = (data2[2] - temp * 128 + 128) / 128 + data2[1] / (128 * 256)
				+ data2[0] / (128 * 256 * 256);
		float result = 0;
		if (e == 0 && f != 0) {// 次正规数
			result = (float) (s * (f - 1) * Math.pow(2, -126));
			return result;
		}
		if (e == 0 && f == 0) {// 有符号的0
			result = (float) 0.0;
			return result;
		}
		if (s == 0 && e == 255 && f == 0) {// 正无穷大
			result = (float) 1111.11;
			return result;
		}
		if (s == 1 && e == 255 && f == 0) {// 负无穷大
			result = (float) -1111.11;
			return result;
		} else {
			result = (float) (s * f * Math.pow(2, e - 127));
			return result;
		}
 
	}
 
	public static byte[] floatToBytes(float a) {
		byte[] data = new byte[4];
		if(a==0){
			for(int i=0;i<4;i++){
				data[i]=0x00;
			}
			return data;
		}
		Integer[] intdata = { 0, 0, 0, 0 };
		a = Math.abs(a);
		// 首先将浮点数转化为二进制浮点数
		float floatpart = a % 1;
		int intpart = (int) (a / 1);
 
		System.out.println(intpart + " " + floatpart);
		// 将整数部分化为2进制,并转化为string类型
		String intString = "";
		String floatString = "";
		String result = "";
		String subResult = "";
		int zhishu = 0;
		if (intpart == 0) {
			intString += "0";
		}
		while (intpart != 0) {
			intString = intpart % 2 + intString;
			intpart = intpart / 2;
		}
		while (floatpart != 0) {
			floatpart *= 2;
			if (floatpart >= 1) {
				floatString += "1";
				floatpart -= 1;
			} else {
				floatString += "0";
			}
 
		}
 
		result = intString + floatString;
		System.out.println(intString + "." + floatString);
		intpart = (int) (a / 1);
		if (intpart > 0) {// 整数部分肯定有1，且以1开头..这样的话，小数点左移
			zhishu = intString.length() - 1;
		} else {// 整数位为0，右移
			for (int i = 0; i < floatString.length(); i++) {
				zhishu--;
				if (floatString.charAt(i) == '1') {
					break;
				}
			}
			// while(floatString.charAt(index)){}
		}
		// 对指数进行移码操作
 
		System.out.println("result==" + result + " zhishu==" + zhishu);
		if (zhishu >= 0) {
			subResult = result.substring(intString.length() - zhishu);
		} else {
			subResult = floatString.substring(-zhishu);
		}
		System.out.println("subResult==" + subResult);
		zhishu += 127;
		if (subResult.length() <= 7) {// 若长度
 
			for (int i = 0; i < 7; i++) {
				if (i < subResult.length()) {
					intdata[1] = intdata[1] * 2 + subResult.charAt(i) - '0';
				} else {
					intdata[1] *= 2;
				}
 
			}
 
			if (zhishu % 2 == 1) {// 如果质数是奇数，则需要在这个最前面加上一个‘1’
				intdata[1] += 128;
			}
			data[1] = intdata[1].byteValue();
		} else if (subResult.length() <= 15) {// 长度在（7,15）以内
			int i = 0;
			for (i = 0; i < 7; i++) {// 计算0-7位，最后加上第一位
				intdata[1] = intdata[1] * 2 + subResult.charAt(i) - '0';
			}
			if (zhishu % 2 == 1) {// 如果质数是奇数，则需要在这个最前面加上一个‘1’
				intdata[1] += 128;
			}
			data[1] = intdata[1].byteValue();
 
			for (i = 7; i < 15; i++) {// 计算8-15位
				if (i < subResult.length()) {
					intdata[2] = intdata[2] * 2 + subResult.charAt(i) - '0';
				} else {
					intdata[2] *= 2;
				}
 
			}
			data[2] = intdata[2].byteValue();
		} else {// 长度大于15
			int i = 0;
			for (i = 0; i < 7; i++) {// 计算0-7位，最后加上第一位
				intdata[1] = intdata[1] * 2 + subResult.charAt(i) - '0';
			}
			if (zhishu % 2 == 1) {// 如果质数是奇数，则需要在这个最前面加上一个‘1’
				intdata[1] += 128;
			}
			data[1] = intdata[1].byteValue();
 
			for (i = 7; i < 15; i++) {// 计算8-15位
				intdata[2] = intdata[2] * 2 + subResult.charAt(i) - '0';
			}
			data[2] = intdata[2].byteValue();
 
			for (i = 15; i < 23; i++) {// 计算8-15位
				if (i < subResult.length()) {
					intdata[3] = intdata[3] * 2 + subResult.charAt(i) - '0';
				} else {
					intdata[3] *= 2;
				}
 
			}
			data[3] = intdata[3].byteValue();
		}
 
		intdata[0] = zhishu / 2;
		if (a < 0) {
			intdata[0] += 128;
		}
		data[0] = intdata[0].byteValue();
		byte[] data2 = new byte[4];// 将数据转移，目的是倒换顺序
		for (int i = 0; i < 4; i++) {
			data2[i] = data[3 - i];
		}
		return data2;
	}
}
