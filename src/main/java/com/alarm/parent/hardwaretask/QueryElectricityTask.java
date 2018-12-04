package com.alarm.parent.hardwaretask;

import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alarm.parent.hardwaresocket.ElectricMeterServer;
@Component
@EnableScheduling

	public class QueryElectricityTask {
	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	ElectricMeterServer electricMeterServer;
	SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	@Scheduled(initialDelay = 10000, fixedDelay = 30000)
//	@Scheduled(fixedDelay = 6000)
	public void send(){
		Map<String , Socket> socketmap = electricMeterServer.getSocketMap();
		for(String key : socketmap.keySet()){
			Socket socket = socketmap.get(key);
			logger.info("发送电量查询,设备ID:"+ key +socket.getInetAddress()+":"+socket.getPort()+"--时间"+sdFormat.format(new Date()));
			try{
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
			}catch(Exception e){
				e.printStackTrace();
//				socketmap.remove(key);//移除断开的连接
				logger.info("设备已断开连接设备ID:"+key);
			}
			
			
			
		}
	}
	
}


