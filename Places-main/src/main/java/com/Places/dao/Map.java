package com.Places.dao;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Id;

@PersistenceCapable
public class Map {

	@PrimaryKey
	@Id
	@Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
	private long id;

	// Private =0 , public = 1
	int status;

	String title;
	String description;

	String tags;

	@Persistent(defaultFetchGroup = "true")
	List<Long> pins = new ArrayList<Long>();

	@Persistent(defaultFetchGroup = "true")
	List<Long> comments = new ArrayList<Long>();

	long user;

	public Map(int status, String title, String description, String tags, long user) {

		this.status = status;
		this.title = title;
		this.description = description;
		this.tags = tags;
		this.user = user;

	}

	public Map(long id) {
		this.id = id;

	}

	public Map() {

	}


	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public List<Long> getPins() {
		return pins;
	}

	public void setPins(List<Long> pins) {
		this.pins = pins;
	}

	public long getUser() {
		return user;
	}

	public void setUser(long user) {
		this.user = user;
	}

	public List<Long> getComments() {
		return comments;
	}

	public void setComments(List<Long> comments) {
		this.comments = comments;
	}

}
