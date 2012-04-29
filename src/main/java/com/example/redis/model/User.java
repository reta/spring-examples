package com.example.redis.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class User {
	private final Long id;
	private String name;
	private String email;
	private Collection< String > friends;
	private int friendsCount;

	public User( final Long id, final String name, final String email ) {
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public Collection<String> getFriends() {
		return friends;
	}

	public String getName() {
		return name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFriends(Collection<String> friends) {
		this.friends = friends;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public Map< ?, ? > toMap() {
		final Map< String, Object > properties = new HashMap< String, Object >();

		properties.put( "id", getId().toString() );
		if( getName() != null ) {
			properties.put( "name", getName() );
		}

		if( getEmail() != null ) {
			properties.put( "email", getEmail() );
		}

		return properties;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}
}
