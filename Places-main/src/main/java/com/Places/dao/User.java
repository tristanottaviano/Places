package com.Places.dao;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Id;

@PersistenceCapable
public class User {

	@Id
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
	long id;

	String username;

	String password;
	String bio;

	@Persistent(defaultFetchGroup = "true")
	List<Long> contacts = new ArrayList<Long>();

	@Persistent(defaultFetchGroup = "true")
	List<Long> myMaps = new ArrayList<Long>();

	@Persistent(defaultFetchGroup = "true")
	List<Long> myComments = new ArrayList<Long>();

	@Persistent(defaultFetchGroup = "true")
	List<Long> myRequests = new ArrayList<Long>();

	public User(String username, String password, String bio) {
		this.username = username;
		this.password = password;
		this.bio = bio;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public User(String un) {
		this.username = un;
	}

	public User() {

	}

	public Long getUser_id() {
		return id;
	}

	public void setUser_id(long user_id) {
		this.id = user_id;
	}

	public List<Long> getContacts() {
		return this.contacts;
	}

	public List<Long> getMyMaps() {
		return this.myMaps;
	}

	public List<Long> getMyComments() {
		return this.myComments;
	}

	public void setContacts(List<Long> us) {
		this.contacts = us;
	}

	public void setMyMaps(List<Long> m) {
		this.myMaps = m;
	}

	public void setMyComments(List<Long> c) {
		this.myComments = c;
	}

	public List<Long> getMyRequests() {
		return myRequests;
	}

	public void setMyRequests(List<Long> myRequests) {
		this.myRequests = myRequests;
	}

}
