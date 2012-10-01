package com.example.redis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.redis.config.AppConfig;

public class RedisPubSubStarter {
	public static void main(String[] args) {
		new AnnotationConfigApplicationContext( AppConfig.class );		
	}
}
