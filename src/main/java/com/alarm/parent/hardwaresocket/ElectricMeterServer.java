package com.alarm.parent.hardwaresocket;

//	   public static void main(String args[]) throws IOException {
	//报警
	import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alarm.parent.hardwareutil.ChangeUtils;
@Service
	public class ElectricMeterServer {
	private static String hexStr =  "0123456789ABCDEF"; 
	SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	ChangeUtils changeUtils;
	
	List<Socket> socketList = new ArrayList<>();
	Map<String,Socket> socketMap = new HashMap<>();
	
//	List<Map<String,Socket>> socketMapList = new ArrayList<>();


	
//		@PostConstruct
		public void preStart() {
			InitAlarmThread music = new InitAlarmThread();
			music.start();
		}

		class InitAlarmThread extends Thread{
		//2):在A类中覆盖Thread类中的run方法.
		public void run() {
			startAction();
		}
		
		
		
	//水电
		public void startAction(){
			logger.info("<<<<<<开始建立电表装置socket监听>>>>>>");
			ServerSocket serverSocket=null;
			try {
				serverSocket=new ServerSocket(9001);
				while(true){
					Socket socket=serverSocket.accept();
					logger.info("电表客户端IP:"+socket.getInetAddress()+"--"+"电表客户端端口:"+socket.getPort()+"--时间"+sdFormat.format(new Date()));
					OutputStream out = socket.getOutputStream();
					byte[] bytes = new byte[8];//一次侧电能计量数据
					bytes[0] = (byte) 0x01;
					bytes[1] = (byte) 0x03;
					bytes[2] = (byte) 0x00;
					bytes[3] = (byte) 0x84;
					bytes[4] = (byte) 0x00;
					bytes[5] = (byte) 0x02;
					bytes[6] = (byte) 0x84;
					bytes[7] = (byte) 0x22;
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
			         String hardwareId="";//设备ID
			         while ((len=inputStream.read(bbs)) != -1) {
			            temp = new String(bbs, 0, len);
			            
			            String[] str = new String[30];
			            int i = 0;
			            String respStr = "";
			            byte bytes[]= new byte[1024];
			            for(byte b : bbs){
			            	bytes[i] = b;
			            	str[i] = String.valueOf((changeUtils.binaryToHexString(b)));
			            	
//			            	logger.info("十进制-------------"+HexToIntString(BinaryToHexString(b)));
//			            	logger.info("十六进制-------------"+str[i]);
			            	respStr = respStr + str[i];
			            	i++;
			            }
			            logger.info(sdFormat.format(new Date()));
			            logger.info(respStr);
			            //234744474C2D3138303030303123
			            if(respStr.startsWith("234744474C2D")){//心跳指令#GLDL-1800001#，，，234744474C2D代表 #GLDL-
			            	String lineStr = changeUtils.convertHexToString(respStr);
			            	logger.info("心跳指令:"+lineStr+"--时间"+sdFormat.format(new Date()));
			            	hardwareId = lineStr.substring(6, lineStr.length()-1);
			            	if(!socketMap.containsKey(hardwareId)){
								socketMap.put(hardwareId, socket);
								logger.info("--socket.hashCode():"+socket.hashCode()+"获取设备ID为:"+hardwareId);
			            	}else{//设备断开重连的情况
			            		socketMap.remove(hardwareId);//之前的已断开或者重复，移除重新添加
			            		socketMap.put(hardwareId, socket);
								logger.info("--socket.hashCode():"+socket.hashCode()+"获取设备ID为:"+hardwareId);
			            	}
			            
			            	
			            }else if(respStr.startsWith("010304")){//010304418F851FFCBC30303031230000000000000000000000
//			            	logger.info("查询电量返回数据:"+changeUtils.convertHexToString(respStr)+"--时间"+sdFormat.format(new Date()));
			            	logger.info("查询电量返回数据:"+respStr+"--时间"+sdFormat.format(new Date()));
			            	logger.info("电量数据:"+str[3]+str[4]+str[5]+str[6]);
			            	 byte bytessub[]={bytes[3],bytes[4],bytes[5],bytes[6]};
			            	 changeUtils.bytesToFloat(bytessub);
			            	 logger.info("电量数据浮点数:"+changeUtils.bytesToFloat(bytessub));
			            	 
				            sb = new StringBuilder();
			            }
			            
			        
			         }
			         logger.info("from client: " + sb);
					
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
		
		public Map<String, Socket> getSocketMap() {
			return socketMap;
		}

		public void setSocketMap(Map<String, Socket> socketMap) {
			this.socketMap = socketMap;
		}

		public List<Socket> getSocketList() {
			return socketList;
		}

		public void setSocketList(List<Socket> socketList) {
			this.socketList = socketList;
		}
	}