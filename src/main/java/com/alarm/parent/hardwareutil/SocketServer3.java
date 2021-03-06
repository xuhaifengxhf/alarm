package com.alarm.parent.hardwareutil;

//	   public static void main(String args[]) throws IOException {
	
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
@Service
	public class SocketServer3 {
	
	public List<Socket> socketList = new ArrayList<>();
	
	public List<Socket> getSocketList() {
		return socketList;
	}

	public void setSocketList(List<Socket> socketList) {
		this.socketList = socketList;
	}

	private static final Logger logger = LoggerFactory.getLogger(SocketServer3.class);
//		@PostConstruct
		public void preStart(){//初始化启动一个线程监听9000端口
			InitAlarmThread music = new InitAlarmThread();
			music.start();
		}

		class InitAlarmThread extends Thread{
			public void run() {
				startAction();
			}



		@Async
		public void startAction(){
//			logger.info(arg0);
			logger.info("开始socket...。。。。。。");
			ServerSocket serverSocket=null;
			try {
				serverSocket=new ServerSocket(9000);
				while(true){
					Socket socket=serverSocket.accept();
					socketList.add(socket);
					logger.info("客户端端口:"+socket.getPort());
					logger.info("客户端IP:"+socket.getInetAddress());
					BufferedWriter writer0 = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					logger.info("to client : "+"AT\r\n");
					writer0.write("AT\r\n");//AT命令用于测试通讯是否正常，不影响设备任何功能或状态。执行命令：AT / 返回信息：OK
					writer0.flush();
					
					
//					writer0.write("AT+CWMSG=SET,0\r\nOK");
//					writer0.flush();
					
					
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
//					while( !(reader.readLine()).equals("ok") ){
//					while( !(lineString=reader.readLine()).equals("OK") ){
					while(true){
						lineString=reader.readLine();
						if( lineString==null ){
						
						

						logger.info("from  client ："+socket.hashCode()+"=========="+lineStr+"OK");
						logger.info("to client : "+"AT+CWMSG=SET,0\r\nOK");
						writer.write("AT+CWMSG=SET,0\r\nOK");
						writer.flush();
					
						
						}else{
							lineStr = lineStr + lineString;
							
							logger.info(lineString);
						}
					}
//					lineStr = lineStr + reader.readLine();
					
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
			         logger.info("from client: " + sb);*/
					
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

//		@Scheduled(initialDelay = 10000, fixedDelay = 30000)
		@Async
		public void timerInit() throws IOException {
			System.out.println("定时任务执行心跳指令。。。。。");
			List<Socket> socketList = getSocketList();
			for(Socket socket : socketList){
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer.write("AT\r\n");
				writer.flush();
				String lineString = "";
				while(!(lineString=reader.readLine()).equals("OK") ){
					System.out.println("设备信息通讯正常");
					if("OK".equals(lineString)){
						break;
					}
				}
			}
//			System.out.println(getSocketList());
		    System.out.println("设备连接数:" + socketList.size());
//		    System.err.println(socketList);
		}
		
//		@Scheduled(initialDelay = 10000, fixedDelay = 20000)
		@Async
		public void findHardWareID() throws IOException {
			System.out.println("定时任务执行查询设备信息。。。。。");
			List<Socket> socketList = getSocketList();
			for(Socket socket : socketList){
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer.write("ATI\r\n");
				writer.flush();
				String lineString = "";
				String lineStr = "";
				while( !(lineString=reader.readLine()).equals("OK") ){
						lineStr = lineStr + lineString;
						System.out.println("设备信息" + lineStr);
						if("OK".equals(lineString)){
							System.out.println("设备信息读取结束，结束while" + lineStr);
							break;
						}
				}
				
			}
//			System.out.println(getSocketList());
		    System.out.println("设备连接数:" + socketList.size());
//		    System.err.println(socketList);
		}


	}