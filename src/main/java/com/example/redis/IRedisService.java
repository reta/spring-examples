package com.example.redis;

import com.example.redis.model.User;

public interface IRedisService {
	void doSomeHashes( final User user );
	void doSomeValues();
	void doSomeExpirations();
	void doSomeBits();
}
