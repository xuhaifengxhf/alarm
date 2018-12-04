package com.alarm.parent.hardwaresocket;

	
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
	public class AlarmServer {
	private static final Logger logger = LoggerFactory.getLogger(AlarmServer.class);
	List<Socket> socketList = new ArrayList<>();
	
	List<Map<String,Socket>> socketMapList = new ArrayList<>();
	
	public List<Socket> getSocketList() {
		return socketList;
	}

	public void setSocketList(List<Socket> socketList) {
		this.socketList = socketList;
	}

	
		@PostConstruct
		public void preStart() {
			InitAlarmThread music = new InitAlarmThread();
			music.start();
		}

		class InitAlarmThread extends Thread{
		//2):在A类中覆盖Thread类中的run方法.
		public void run() {
			startAction();
		}

//		@PostConstruct
		public void startAction(){
			logger.info("<<<<<<开始建立警报装置socket监听>>>>>>");
			ServerSocket serverSocket=null;
			try {
				serverSocket=new ServerSocket(9000);
				while(true){
					Socket socket=serverSocket.accept();
					socketList.add(socket);
					logger.info("建立连接警报客户端端口:"+socket.getPort());
					logger.info("建立连接警报客户端IP:"+socket.getInetAddress());
					BufferedWriter writer0 = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					logger.info("--socket.hashCode():"+socket.hashCode()+"--发送--"+"AT");
					writer0.write("AT\r\n");
					writer0.flush();
					
					logger.info("--socket.hashCode():"+socket.hashCode()+"--发送--"+"ATI");
					writer0.write("ATI\r\n");
					writer0.flush();
					
//					writer0.write("AT+CWMSG=SET,0\r\nOK");
					logger.info("--socket.hashCode():"+socket.hashCode()+"--发送--"+"AT+CWMSG=SET,0,0,60,5,1,1");
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
			public void run() {//这一个线程里面是一个设备连接
				try {
					reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					String lineString="";
					String lineStr="";
					String id="";//消息ID
					String time="";//时间戳
					String msgType="";//消息类型
					String defenceArea="";//防区号
					String alarmType="";//警情代码-----111：烟感    130：门磁  140：红外
					String hardwareId="";//设备ID
//					while( !(reader.readLine()).equals("ok") ){
//					while( !(lineString=reader.readLine()).equals("OK") ){
					while( (lineString=reader.readLine())!=null ){
//						lineStr = lineStr + lineString;
						logger.info("--socket.hashCode():"+socket.hashCode()+"获取--"+lineString);

						if(lineString.startsWith("ID:")){//获取设备ID
							hardwareId = lineString.substring(4, lineString.length());
							Map<String,Socket> socketMap = new HashMap<>();
							socketMap.put(hardwareId, socket);
							socketMapList.add(socketMap);
							logger.info("--socket.hashCode():"+socket.hashCode()+"获取设备ID为:"+hardwareId);
						}
						if(lineString.contains("DFA_ALARM")){//防区报警确认
							String ss[] = lineString.split(",");
							id = ss[0].substring(8, ss[0].length());
							time = ss[1];
							msgType = ss[2];
							defenceArea = ss[3];
							alarmType = ss[4];
							logger.info("--socket.hashCode():"+socket.hashCode()+"发送---------"+"AT+CWMSG="+id);
							writer.write("AT+CWMSG="+id+"\r\n");//确认消息
							writer.flush();
							
//							if(lineString.equals("OK")){
							logger.info("--socket.hashCode():"+socket.hashCode()+"发送---------"+"AT+CDAM=1,0,"+defenceArea+","+defenceArea);
							writer.write("AT+CDAM=1,0,"+defenceArea+","+defenceArea+"\r\n");//撤防
							writer.flush();
//							}
						}
						if(lineString.contains("DFA_RESTORE")){//防区报警恢复确认
							String ss[] = lineString.split(",");
							id = ss[0].substring(8, ss[0].length());
						/*	time = ss[1];
							msgType = ss[2];
							defenceArea = ss[3];
							alarmType = ss[4];*/
							logger.info("--socket.hashCode():"+socket.hashCode()+"发送---------"+"AT+CWMSG="+id);
							writer.write("AT+CWMSG="+id+"\r\n");//确认消息
							writer.flush();
							
						}
						if(lineString.contains("USER_SETARM")){//用户布防确认
							String ss[] = lineString.split(",");
							id = ss[0].substring(8, ss[0].length());
						/*	time = ss[1];
							msgType = ss[2];
							defenceArea = ss[3];
							alarmType = ss[4];*/
							logger.info("--socket.hashCode():"+socket.hashCode()+"发送---------"+"AT+CWMSG="+id);
							writer.write("AT+CWMSG="+id+"\r\n");//确认消息
							writer.flush();
							
						}
						if(lineString.contains("USER_DISARM")){//用户撤防确认
							String ss[] = lineString.split(",");
							id = ss[0].substring(8, ss[0].length());
							/*time = ss[1];
							msgType = ss[2];
							defenceArea = ss[3];
							alarmType = ss[4];*/
							logger.info("--socket.hashCode():"+socket.hashCode()+"发送---------"+"AT+CWMSG="+id);
							writer.write("AT+CWMSG="+id+"\r\n");//确认消息
							writer.flush();
							
						}
					
					}
					
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
public static void main(String[] args) {
	String ss = "#GLDL-1800001#";
	String s = ss.substring(6, ss.length()-1);
	System.out.println(s);
	
}
	}