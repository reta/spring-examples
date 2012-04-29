package com.example.redis.impl;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class RedisMessageListener implements MessageListener {
	@Override
	public void onMessage(Message message, byte[] paramArrayOfByte) {
		System.out.println( "Received by RedisMessageListener: " + message.toString() );
	}
}
