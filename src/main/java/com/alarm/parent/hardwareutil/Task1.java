package com.alarm.parent.hardwareutil;

import java.text.SimpleDateFormat;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alarm.parent.hardwareutil.SocketServer2;
@Component
@EnableScheduling
	public class Task1 {
	
//	@Autowired
//	SocketServer2 socketServer2;
	SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//	@Scheduled(cron = "0/10 * * * * ? ")
/*	@Scheduled(fixedDelay = 6000)
	public void send(){
		for(Socket socket : socketServer2.getSocketList()){
			System.out.println("ip端口："+socket.getInetAddress()+":"+socket.getPort());
		}
	}*/
	
    /*第一次10秒后执行，当执行完后2秒再执行*/
//    @Scheduled(initialDelay = 10000, fixedDelay = 2000)
/*	@Scheduled(cron = "0/5 * * * * ?")
    public void timerInit() {
        System.out.println("init :");
    }*/
    
    // 给sched用的，默认只有1个，时间太长会阻塞
//    @Bean(destroyMethod = "shutdown")
//    public Executor taskScheduler() {
//        return Executors.newScheduledThreadPool(3);
//    }
    
    /*
    @Configuration
    @EnableScheduling
    public class SchedulingConfiguration {
        // 给sched用的，默认只有1个，时间太长会阻塞
        @Bean(destroyMethod = "shutdown")
        public Executor taskScheduler() {
            return Executors.newScheduledThreadPool(3);
        }
    }*/
}


