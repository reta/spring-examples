package com.example.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.redis.IRedisConsumer;
import com.example.redis.IRedisPublisher;
import com.example.redis.IRedisService;
import com.example.redis.impl.RedisConsumerImpl;
import com.example.redis.impl.RedisMessageListener;
import com.example.redis.impl.RedisPublisherImpl;
import com.example.redis.impl.RedisServiceImpl;

@Configuration
@EnableScheduling
public class AppConfig {
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		return new JedisConnectionFactory();
	}

	@Bean
	RedisTemplate< String, Object > redisTemplate() {
		final RedisTemplate< String, Object > template =  new RedisTemplate< String, Object >();
		template.setConnectionFactory( jedisConnectionFactory() );
		template.setKeySerializer( new StringRedisSerializer() );
		template.setHashValueSerializer( new GenericToStringSerializer< Object >( Object.class ) );
		template.setValueSerializer( new GenericToStringSerializer< Object >( Object.class ) );
		return template;
	}

	@Bean
	MessageListenerAdapter messageListener() {
		return new MessageListenerAdapter( new RedisMessageListener() );
	}

	@Bean
	RedisMessageListenerContainer redisContainer() {
		final RedisMessageListenerContainer container = new RedisMessageListenerContainer();

		container.setConnectionFactory( jedisConnectionFactory() );
		container.addMessageListener( messageListener(), new ChannelTopic( "pubsub:queue" ) );

		return container;
	}

	@Bean
	DefaultRedisList< Object > pubsubQueue() {
		return new DefaultRedisList< Object >( "pubsub:queue", redisTemplate() );
	}

	@Bean
	IRedisService redisService() {
		return new RedisServiceImpl( redisTemplate() );
	}

	@Bean
	IRedisConsumer redisConsumer() {
		return new RedisConsumerImpl( redisTemplate() );
	}

	@Bean
	IRedisPublisher redisPublisher() {
		return new RedisPublisherImpl( redisTemplate() );
	}
}
