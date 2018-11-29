package com.alarm.parent.hardwareutil;

//	   public static void main(String args[]) throws IOException {
	
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
	public class SocketServerXiaojing2 {
	
	List<Socket> socketList = new ArrayList<>();
	
	public List<Socket> getSocketList() {
		return socketList;
	}

	public void setSocketList(List<Socket> socketList) {
		this.socketList = socketList;
	}

	private static final Logger logger = LoggerFactory.getLogger(SocketServerXiaojing2.class);
//		@PostConstruct
		public void startAction(){
//			System.out.println(arg0);
			System.out.println("开始socket...");
//			System.out.println();
			ServerSocket serverSocket=null;
			try {
				serverSocket=new ServerSocket(9000);
				while(true){
					Socket socket=serverSocket.accept();
					socketList.add(socket);
					System.out.println("客户端端口:"+socket.getPort());
					System.out.println("客户端IP:"+socket.getInetAddress());
					BufferedWriter writer0 = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					writer0.write("AT\r\n");
					writer0.flush();
					
					
//					writer0.write("AT+CWMSG=SET,0\r\nOK");
					writer0.write("AT+CWMSG=SET,0,0,60,5,1,1\r\nOK");
					writer0.flush();
					
					
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
		
		class MyRuns implements Runnable{
			
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
					reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					String lineString="";
					String lineStr="";
					String id="";//消息ID
					String time="";//时间戳
					String msgType="";//消息类型
					String defenceArea="";//防区号
					String alarmType="";//警情代码
//					while( !(reader.readLine()).equals("ok") ){
//					while( !(lineString=reader.readLine()).equals("OK") ){
					while( (lineString=reader.readLine())!=null ){
//						lineStr = lineStr + lineString;
						System.out.println("---------"+socket.hashCode()+"获取---------"+lineString);
						
						if(lineString.contains("DFA_ALARM")){//防区报警
							String ss[] = lineString.split(",");
							id = ss[0];
							time = ss[1];
							msgType = ss[2];
							defenceArea = ss[3];
							alarmType = ss[4];
							System.out.println("---------"+socket.hashCode()+"发送---------"+"AT+CWMSG="+id);
							writer.write("AT+CWMSG="+id+"\r\n");//确认消息
							writer.flush();
							
//							if(lineString.equals("OK")){
							System.out.println("---------"+socket.hashCode()+"发送---------"+"AT+CDAM=1,0,"+defenceArea+","+defenceArea);
							writer.write("AT+CDAM=1,0,"+defenceArea+","+defenceArea+"\r\n");//撤防
							writer.flush();
//							}
						}
						if(lineString.contains("DFA_RESTORE")){//防区报警恢复
							String ss[] = lineString.split(",");
							id = ss[0];
							time = ss[1];
							msgType = ss[2];
							defenceArea = ss[3];
							alarmType = ss[4];
							System.out.println("---------"+socket.hashCode()+"发送---------"+"AT+CWMSG="+id);
							writer.write("AT+CWMSG="+id+"\r\n");//确认消息
							writer.flush();
							
						}
					
//						Stream<String> lineTotal = reader.lines();
					/*	if(lineString.equals("OK")){
							System.out.println("from  client ："+socket.hashCode()+"==="+lineStr);
							System.out.println("return from server ："+socket.hashCode()+"==="+"success ok");
							lineStr = "";
							
						}*/
//						System.out.println("to client : "+"AT+CWMSG=SET,0\r\nOK");
//						writer.write("AT+CWMSG=SET,0\r\nOK");
//						writer.flush();
						
					}
//					lineStr = lineStr + reader.readLine();
					System.out.println("from  client ："+socket.hashCode()+"=========="+lineStr+"OK");
//					writer.write("return from server:"+"success ok"+"\n");
//					writer.flush();
//					
				/*	 Reader reader = new InputStreamReader(socket.getInputStream());
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
			         System.out.println("from client: " + sb);*/
					
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