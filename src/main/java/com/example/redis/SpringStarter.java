package com.example.redis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.redis.config.AppConfig;
import com.example.redis.model.User;

public class SpringStarter {
	public static void main(String[] args) {
		final ApplicationContext context = new AnnotationConfigApplicationContext( AppConfig.class );
		final IRedisService service = context.getBean( IRedisService.class );

		service.doSomeHashes( new User( 1L, "Tom", "user@example.com" ) );
		service.doSomeValues();
		service.doSomeExpirations();
		service.doSomeBits();
	}
}
