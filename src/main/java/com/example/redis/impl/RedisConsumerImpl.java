package com.example.redis.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.example.redis.IRedisConsumer;

public class RedisConsumerImpl implements IRedisConsumer, MessageListener {
	final RedisTemplate< String, Object > template;

	public RedisConsumerImpl( final RedisTemplate< String, Object > template ) {
		this.template = template;
	}

	@Override
	public void start() {
		template.execute(
			new RedisCallback< Void >() {
				@Override
				public Void doInRedis( RedisConnection connection ) throws DataAccessException {
					connection.subscribe( RedisConsumerImpl.this, ( ( RedisSerializer< String > )template.getKeySerializer() ).serialize( "pubsub:queue" ) );
					return null;
				}
			}
		);
	}

	@Override
	public void onMessage( Message message, byte[] pattern ) {
		System.out.println( "Received by RedisConsumerImpl: " + message.toString() );
	}
}
