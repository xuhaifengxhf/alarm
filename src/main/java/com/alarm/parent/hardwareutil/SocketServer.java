package com.alarm.parent.hardwareutil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
@Service
public class SocketServer {
	 
//	   public static void main(String args[]) throws IOException {
//	@PostConstruct
	public void start() throws IOException{
			   System.out.println("开始Socket");
			      //为了简单起见，所有的异常信息都往外抛
			     int port = 9000;
			      //定义一个ServerSocket监听在端口8899上
			     ServerSocket server = new ServerSocket(port);
			      while (true) {
			    	  
			         //server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的
			         Socket socket = server.accept();
			         //每接收到一个Socket就建立一个新的线程来处理它
			         new Thread(new Task(socket)).start();
			      }
			   }
			   
			   /**
			    * 用来处理Socket请求的
			   */
			   static class Task implements Runnable {
			 
			      private Socket socket;
			      
			      public Task(Socket socket) {
			         this.socket = socket;
			      }
			      
			      public void run() {

			         try {

			            handleSocket();
			         } catch (Exception e) {
			            e.printStackTrace();
			         }
			      }
			      
			      /**
			       * 跟客户端Socket进行通信
			       * @throws Exception
			       */
			      private void handleSocket() throws Exception {
			         Reader reader = new InputStreamReader(socket.getInputStream());
			         char chars[] = new char[64];
			         int len;
			         StringBuilder sb = new StringBuilder();
			         String temp;
			         int index;
			         while ((len=reader.read(chars)) != -1) {
			            temp = new String(chars, 0, len);
			            if ((index = temp.indexOf("ok")) != -1) {//遇到eof时就结束接收
			             sb.append(temp.substring(0, index));
			                break;
			            }
			            sb.append(temp);
			         }
			         System.out.println("from client: " + sb);
			         //读完后写一句
			       Writer writer = new OutputStreamWriter(socket.getOutputStream());
			         writer.write("000000110000011");
			         writer.flush();
//			         writer.close();
//			         reader.close();
//			         socket.close();
			      }
			      
			   }
			   
			   
			   
			   
			   
			   
			   
			   
			   
			   
			   //字符串转16进制


			   /**
			    * 字符串转换成为16进制(无需Unicode编码)
			    * @param str
			    * @return
			    */

			  public static String str2HexStr(String str) {
			   char[] chars = "0123456789ABCDEF".toCharArray();
			   StringBuilder sb = new StringBuilder("");
			   byte[] bs = str.getBytes();
			   int bit;
			   for (int i = 0; i < bs.length; i++) {
			           bit = (bs[i] & 0x0f0) >> 4;
			           sb.append(chars[bit]);
			           bit = bs[i] & 0x0f;
			           sb.append(chars[bit]);
			           // sb.append(' ');
			      }
			       return sb.toString().trim();
			   }
			   //16进制转为字符串

			   /**
			    * 16进制直接转换成为字符串(无需Unicode解码)
			    * @param hexStr
			    * @return
			    */
			   public static String hexStr2Str(String hexStr) {
			       String str = "0123456789ABCDEF";
			       char[] hexs = hexStr.toCharArray();
			       byte[] bytes = new byte[hexStr.length() / 2];
			       int n;
			       for (int i = 0; i < bytes.length; i++){
			           n = str.indexOf(hexs[2 * i]) * 16;
			           n += str.indexOf(hexs[2 * i + 1]);
			           bytes[i] = (byte) (n & 0xff);
			       }
			       return new String(bytes);
			   }
			   
			   
			   
			   public static void main(String[] args) {
				   System.out.println(str2HexStr("046688"));
				   System.out.println(hexStr2Str(str2HexStr("046688")));
			}
	}