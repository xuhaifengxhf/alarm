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
import java.net.ServerSocket;
import java.net.Socket;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
@Service
	public class SocketServerSdbak {
	
	static{}
	
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
				 Reader reader = new InputStreamReader(socket.getInputStream());
			         char chars[] = new char[64];
			         int len;
			         StringBuilder sb = new StringBuilder();
			         String temp;
			         int index;
			         while ((len=reader.read(chars)) != -1) {
			            temp = new String(chars, 0, len);
			         /*   if ((index = temp.indexOf("ok")) != -1) {//遇到eof时就结束接收
			             sb.append(temp.substring(0, index));
			                break;
			            }*/
			            sb.append(temp);
			            String s = sb.toString();
//			            public static String strTo16(String s) {
			    	        String str = "";
			    	        for (int i = 0; i < s.length(); i++) {
			    	            int ch = (int) s.charAt(i);
			    	            String s4 = Integer.toHexString(ch);
			    	            str = str + s4;
//			    	        }
//			    	        return str;
			    	    }
			            System.out.println("from client: " + str);
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