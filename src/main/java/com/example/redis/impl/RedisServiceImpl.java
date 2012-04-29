package com.example.redis.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.example.redis.IRedisService;
import com.example.redis.model.User;

public class RedisServiceImpl implements IRedisService {
	final RedisTemplate< String, Object > template;

	public RedisServiceImpl( final RedisTemplate< String, Object > template ) {
		this.template = template;
	}

	@Override
	public void doSomeHashes( final User user ) {
		final String key = String.format( "user:%s", user.getId() );
		template.delete( key );

		template.opsForHash().putAll( key, user.toMap() );
		System.out.println( "Properies are: " + template.opsForHash().entries( key ) );
		System.out.println( "Email is: " + template.opsForHash().get( key, "email" ) );

		template.opsForHash().increment( key, "friendsCount", 1 );
		System.out.println( "User has: " + template.opsForHash().get( key, "friendsCount" ) + " friend(s)" );

		if( template.opsForHash().putIfAbsent( key, "friendsCount", 2L ) ) {
			System.out.println( "Hm .. user has: " + template.opsForHash().get( key, "friendsCount" ) + " friend(s)" );
		} else {
			System.out.println( "User still has: " + template.opsForHash().get( key, "friendsCount" ) + " friend(s)" );
		}

		if( template.opsForHash().putIfAbsent( key, "enemiesCount", 2L ) ) {
			System.out.println( "But .. user has: " + template.opsForHash().get( key, "enemiesCount" ) + " enemie(s)" );
		}
	}

	@Override
	public void doSomeValues() {
		final String key = "user:text";
		template.delete( key );

		template.opsForValue().append( key, "First phrase" );
		System.out.println( "Text is: " + template.opsForValue().get( key ) );

		template.opsForValue().append( key, ", and another phrase" );
		System.out.println( "Text is: " + template.opsForValue().get( key ) );
	}

	@Override
	public void doSomeExpirations() {
		final String key = "user:expirable";

		template.opsForValue().set( key, "Should Expire Soon" );
		template.expire( key, 1, TimeUnit.SECONDS );

		System.out.println( "Waiting for expiration ..." );
		while( template.opsForValue().get( key ) != null ) {
			Thread.yield();
		}

		System.out.println( "Expired ..." );
	}

	@Override
	public void doSomeBits() {
		final String key = "user:active";

		setBit( key, 10000000, false );
		System.out.println( "Bit at 999999: " + getBit( key, 999999 ) );
		System.out.println( "Bit at 10000000: " + getBit( key, 10000000 ) );
	}

	private boolean getBit( final String key, final long offset ) {
		return template.execute(
			new RedisCallback< Boolean >() {
				@Override
				public Boolean doInRedis( RedisConnection connection ) throws DataAccessException {
					return connection.getBit( ( ( RedisSerializer< String > )template.getKeySerializer() ).serialize( key ), offset );
				}
			}
		);
	}

	private void setBit( final String key, final long offset, final boolean value ) {
		template.execute(
			new RedisCallback< Void >() {
				@Override
				public Void doInRedis( RedisConnection connection ) throws DataAccessException {
					connection.setBit( ( ( RedisSerializer< String > )template.getKeySerializer() ).serialize( key ), offset, value );
					return null;
				}
			}
		);
	}


}
