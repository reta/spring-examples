package com.example.redis.impl;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.redis.IRedisPublisher;

public class RedisPublisherImpl implements IRedisPublisher {
	private final RedisTemplate< String, Object > template;
	private final AtomicBoolean started = new AtomicBoolean( false );
	private final AtomicLong counter = new AtomicLong( 0 );

	public RedisPublisherImpl( final RedisTemplate< String, Object > template ) {
		this.template = template;
	}

	@Override
	public void start() {
		started.getAndSet( true );
	}

	@Scheduled( fixedDelay = 100 )
	public void publish() {
		if( started.get() ) {
			template.execute(
				new RedisCallback< Long >() {
					@Override
					public Long doInRedis( RedisConnection connection ) throws DataAccessException {
						return connection.publish( ( ( RedisSerializer< String > )template.getKeySerializer() ).serialize( "pubsub:queue" ),
								( ( RedisSerializer< Object > )template.getValueSerializer() ).serialize( "Message " +
										+ counter.incrementAndGet() + ", " + Thread.currentThread().getName() ) );
					}
				}
			);
		}
	}
}
