package com.alarm.parent.hardwareutil;

public class BinaryUtils {
	
	
	public static int add(int a,int b) {
        int res=a;
        int xor=a^b;//得到原位和
        int forward=(a&b)<<1;//得到进位和
        if(forward!=0){//若进位和不为0，则递归求原位和+进位和
            res=add(xor, forward);
        }else{
            res=xor;//若进位和为0，则此时原位和为所求和
        }
        return res;                
    }
	
	
	public static int minus(int a,int b) {
        int B=~(b-1);
        return add(a, B);        
    }
	
	
	
	
	public static int multi(int a,int b){
        int i=0;
        int res=0;
        while(b!=0){//乘数为0则结束
            //处理乘数当前位
            if((b&1)==1){
                res+=(a<<i);
                b=b>>1;
                ++i;//i记录当前位是第几位
            }else{
                b=b>>1;
                ++i;
            }
        }
        return res;
    }
	
	
	
	public static int sub(int a,int b) {
        int res=-1;
        if(a<b){
            return 0;
        }else{
            res=sub(minus(a, b), b)+1;
        }
        return res;
    }
	
	
	
	/**
	 * 二进制加法，输入和输出都是字符串类型
	 * @param bin1
	 * @param bin2
	 * @return
	 */
	public static String sum(String bin1,String bin2){
		StringBuilder result = new StringBuilder();
		int len1 = bin1.length();
		int len2 = bin2.length();
		//cout表示进位
		int cout = 0;
		for(int i=1;i<=len1&&i<=len2;i++){
			//获取两个数第i位的数字
			int num1 = bin1.charAt(len1-i)-48;
			int num2 = bin2.charAt(len2-i)-48;
			//在开头插入单位相加结果
			result.insert(0,num1^num2^cout);
			cout = num1&num2;		
		}		
		//当循环运算完毕时，在开头插入进位数并转成String形式输出
		return result.insert(0,cout).toString();
	}
		
	public static void main(String[] args) {
		
		System.out.println(sum("1110","1010"));
		
		System.out.println(multi(1011,1001));
		
		
	}

}
