package com.example.redis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.redis.config.AppConfig;

public class PubSubStarter {
	public static void main(String[] args) {
		final ApplicationContext context = new AnnotationConfigApplicationContext( AppConfig.class );

		final IRedisPublisher publisher = context.getBean( IRedisPublisher.class );
		publisher.start();

		final IRedisConsumer consumer = context.getBean( IRedisConsumer.class );
		consumer.start();

	}
}
