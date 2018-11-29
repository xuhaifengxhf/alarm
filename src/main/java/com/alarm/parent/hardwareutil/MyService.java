package com.alarm.parent.hardwareutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

//import lombok.extern.slf4j.Slf4j;

//@Slf4j
public class MyService implements ApplicationContextInitializer {
	
	private static final Logger logger = LoggerFactory.getLogger(MyService.class);
    @Override
    public void initialize(ConfigurableApplicationContext arg0) {
    	logger.info(">>>>>>>>>>>>>>");
    }

}