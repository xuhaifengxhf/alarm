package com.alarm.parent.hardwareutil;

//	   public static void main(String args[]) throws IOException {
	//报警
	import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
@Service
	public class SocketServerSdOk {
	private static String hexStr =  "0123456789ABCDEF"; 
	
	   public static int HexToIntString(String strHex3){

//	        System.out.println("==========================================================");
	        //定义一个十六进制值
//	        String strHex3 = "00001322";
	        //将十六进制转化成十进制
	        int valueTen2 = Integer.parseInt(strHex3,16);
//	        System.out.println(strHex3 + " [十六进制]---->[十进制] " + valueTen2);
	        return valueTen2;

	      /*  System.out.println("==========================================================");
	        //可以在声明十进制时，自动完成十六进制到十进制的转换
	        int valueHex = 0x00001322;
	        System.out.println("int valueHex = 0x00001322 --> " + valueHex);*/
	        
	    }
	   
	   public static String BinaryArrayToHexString(byte[] bytes){  
	         
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
	   
	    public static String strTo16(String s) {
	        String str = "";
	        for (int i = 0; i < s.length(); i++) {
	            int ch = (int) s.charAt(i);
	            String s4 = Integer.toHexString(ch);
	            str = str + s4;
	        }
	        return str;
	    }
	   
	   public static String BinaryToHexString(byte bt){  
	         
//	        String result = "";  
	        String hex = "";  
	            //字节高4位  
	            hex = String.valueOf(hexStr.charAt((bt&0xF0)>>4));  
	            //字节低4位  
	            hex += String.valueOf(hexStr.charAt(bt&0x0F));  
//	            result +=hex+" ";  
	        return hex;  
	    } 
	
	//水电
//		@PostConstruct
		public void startAction(){
			System.out.println("开始水电socket...");
			ServerSocket serverSocket=null;
			try {
				serverSocket=new ServerSocket(9001);
				while(true){
					Socket socket=serverSocket.accept();
					System.out.println("客户端IP:"+socket.getInetAddress()+"--"+"客户端端口:"+socket.getPort()+"--时间"+System.currentTimeMillis());
//					System.out.println("客户端IP:"+socket.getInetAddress());
					OutputStream out = socket.getOutputStream();
//					out.write(bytes);
					byte[] bytes = new byte[8];
					bytes[0] = (byte) 0x01;
					bytes[1] = (byte) 0x03;
					bytes[2] = (byte) 0x01;
					bytes[3] = (byte) 0x00;
					bytes[4] = (byte) 0x00;
					bytes[5] = (byte) 0x09;
					bytes[6] = (byte) 0x84;
					bytes[7] = (byte) 0x30;
					out.write(bytes);
					out.flush();
					
					new Thread(new MyRuns(socket)).start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (serverSocket!=null) {
						serverSocket.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		
		public class MyRuns implements Runnable{
			
			Socket socket;
			BufferedReader reader;
			BufferedWriter writer;
	 
			public MyRuns(Socket socket) {
				super();
				this.socket = socket;
			}
	 
			@Override
			public void run() {
				try {
//				 Reader reader = new InputStreamReader(socket.getInputStream());
				 InputStream inputStream = socket.getInputStream();
//			         char chars[] = new char[1024];
				 byte[] bbs = new byte[25];
			         int len;
			         StringBuilder sb = new StringBuilder();
			         String temp;
			         int index;
			         while ((len=inputStream.read(bbs)) != -1) {
			            temp = new String(bbs, 0, len);
			            
			            for(byte b : bbs){
			            	System.out.println("十六进制-------------"+BinaryToHexString(b));
			            }
			            String[] str = new String[30];
			            int i = 0;
			            for(byte b : bbs){
			            	str[i] = String.valueOf((BinaryToHexString(b)));
			            	
//			            	System.out.println("十进制-------------"+HexToIntString(BinaryToHexString(b)));
			            	System.out.println("十六进制-------------"+str[i]);
			            	i++;
			            }

			            
			            
			            
			            
			          /*  byte[] bbs = temp.getBytes();
			            String bhex = new String();
			            for(byte bb : b){
			            	
			            	String bs = Integer.toHexString(bb & 0xFF);
			            	if (bs.length() == 1) {
			            			bs = '0' + bs;
			            		}
			            	bhex = bhex + bs;
			            	
			            }*/
			            System.out.println("数据寄存器字节数:"+HexToIntString(BinaryToHexString(bbs[2])));
			            System.out.println("A相电压:"+BigDecimal.valueOf(HexToIntString(str[3]+str[4])).divide(new BigDecimal(10)).setScale(1, BigDecimal.ROUND_HALF_UP));
			            System.out.println("B相电压:"+BigDecimal.valueOf(HexToIntString(str[5]+str[6])).divide(new BigDecimal(10)).setScale(1, BigDecimal.ROUND_HALF_UP));
			            System.out.println("C相电压:"+BigDecimal.valueOf(HexToIntString(str[7]+str[8])).divide(new BigDecimal(10)).setScale(1, BigDecimal.ROUND_HALF_UP));
			            System.out.println("AB线电压"+BigDecimal.valueOf(HexToIntString(str[9]+str[10])).divide(new BigDecimal(10)).setScale(1, BigDecimal.ROUND_HALF_UP));
			            System.out.println("BC线电压"+BigDecimal.valueOf(HexToIntString(str[11]+str[12])).divide(new BigDecimal(10)).setScale(1, BigDecimal.ROUND_HALF_UP));
			            System.out.println("CA线电压"+BigDecimal.valueOf(HexToIntString(str[13]+str[14])).divide(new BigDecimal(10)).setScale(1, BigDecimal.ROUND_HALF_UP));
			            System.out.println("A相电流"+BigDecimal.valueOf(HexToIntString(str[15]+str[16])).divide(new BigDecimal(1000)).setScale(3, BigDecimal.ROUND_HALF_UP));
			            System.out.println("B相电流"+BigDecimal.valueOf(HexToIntString(str[17]+str[18])).divide(new BigDecimal(1000)).setScale(3, BigDecimal.ROUND_HALF_UP));
			            System.out.println("C相电流"+BigDecimal.valueOf(HexToIntString(str[19]+str[20])).divide(new BigDecimal(1000)).setScale(3, BigDecimal.ROUND_HALF_UP));
//			            System.out.println(bhex);
//			            System.out.println("from client: " + chars);
//			            System.out.println("from client: " + b);
//			            System.out.println("from client: " + temp);
			            sb = new StringBuilder();
			         }
			         System.out.println("from client: " + sb);
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (reader!=null) {
							reader.close();
						}
						if (writer!=null) {
							writer.close();
						}
						if (socket!=null) {
							socket.close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
			
		}

	}