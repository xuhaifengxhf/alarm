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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
	public class SocketServerDianliang {
	private static String hexStr =  "0123456789ABCDEF"; 
	SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	ChangeUtils changeUtils;
	
	//水电
//		@PostConstruct
		public void startAction(){
			logger.info("开始电量socket........");
			ServerSocket serverSocket=null;
			try {
				serverSocket=new ServerSocket(9001);
				while(true){
					Socket socket=serverSocket.accept();
					logger.info("客户端IP:"+socket.getInetAddress()+"--"+"客户端端口:"+socket.getPort()+"--时间"+sdFormat.format(new Date()));
//					logger.info("客户端IP:"+socket.getInetAddress());
					OutputStream out = socket.getOutputStream();
//					out.write(bytes);
					byte[] bytes = new byte[8];
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
			         while ((len=inputStream.read(bbs)) != -1) {
			            temp = new String(bbs, 0, len);
			            
			            String[] str = new String[30];
			            int i = 0;
			            String respStr = "";
			            for(byte b : bbs){
			            	str[i] = String.valueOf((changeUtils.binaryToHexString(b)));
			            	
//			            	logger.info("十进制-------------"+HexToIntString(BinaryToHexString(b)));
//			            	logger.info("十六进制-------------"+str[i]);
			            	respStr = respStr + str[i];
			            	i++;
			            }
			            logger.info(sdFormat.format(new Date()));
			            logger.info(respStr);
			            
			            if(respStr.contains("234744474C2D3138303030303123")){
			            	logger.info("心跳指令:"+ChangeUtils.convertHexToString(respStr)+"--时间"+sdFormat.format(new Date()));
			            }else{
			            	logger.info("查询电量返回数据:"+ChangeUtils.convertHexToString(respStr)+"--时间"+sdFormat.format(new Date()));
			            	logger.info("电量数据:"+str[3]+str[4]+str[5]+str[6]);
			            	
			               /* logger.info("数据寄存器字节数:"+changeUtils.hexToIntString(changeUtils.binaryToHexString(bbs[2])));
				            logger.info("A相电压:"+BigDecimal.valueOf(changeUtils.hexToIntString(str[3]+str[4])).divide(new BigDecimal(10)).setScale(1, BigDecimal.ROUND_HALF_UP));
				            logger.info("B相电压:"+BigDecimal.valueOf(changeUtils.hexToIntString(str[5]+str[6])).divide(new BigDecimal(10)).setScale(1, BigDecimal.ROUND_HALF_UP));
				            logger.info("C相电压:"+BigDecimal.valueOf(changeUtils.hexToIntString(str[7]+str[8])).divide(new BigDecimal(10)).setScale(1, BigDecimal.ROUND_HALF_UP));
				            logger.info("AB线电压"+BigDecimal.valueOf(changeUtils.hexToIntString(str[9]+str[10])).divide(new BigDecimal(10)).setScale(1, BigDecimal.ROUND_HALF_UP));
				            logger.info("BC线电压"+BigDecimal.valueOf(changeUtils.hexToIntString(str[11]+str[12])).divide(new BigDecimal(10)).setScale(1, BigDecimal.ROUND_HALF_UP));
				            logger.info("CA线电压"+BigDecimal.valueOf(changeUtils.hexToIntString(str[13]+str[14])).divide(new BigDecimal(10)).setScale(1, BigDecimal.ROUND_HALF_UP));
				            logger.info("A相电流"+BigDecimal.valueOf(changeUtils.hexToIntString(str[15]+str[16])).divide(new BigDecimal(1000)).setScale(3, BigDecimal.ROUND_HALF_UP));
				            logger.info("B相电流"+BigDecimal.valueOf(changeUtils.hexToIntString(str[17]+str[18])).divide(new BigDecimal(1000)).setScale(3, BigDecimal.ROUND_HALF_UP));
				            logger.info("C相电流"+BigDecimal.valueOf(changeUtils.hexToIntString(str[19]+str[20])).divide(new BigDecimal(1000)).setScale(3, BigDecimal.ROUND_HALF_UP));*/
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