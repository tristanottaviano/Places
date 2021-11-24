package com.Places.dao;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Id;

@PersistenceCapable
public class Pin {

	@PrimaryKey
	@Id
	@Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
	private long id;

	String title;
	String description;
	String tags;

	float latitude;
	float longitude;

	@Persistent(defaultFetchGroup = "true")
	List<Long> comments = new ArrayList<Long>();

	long map = 0;

	@Persistent(defaultFetchGroup = "true")
	List<String> images = new ArrayList<String>();

	// Constructeur
	public Pin(String title, String description, String tags, float latitude, float longitude, long map) {
		this.title = title;
		this.description = description;
		this.tags = tags;
		this.latitude = latitude;
		this.longitude = longitude;
		this.map = map;

	}

	public Pin(String title) {
		this.title = title;
	}

	public Pin() {

	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public long getMap() {
		return map;
	}

	public void setMap(long map) {
		this.map = map;
	}

	public List<Long> getComments() {
		return comments;
	}

	public void setComments(List<Long> comments) {
		this.comments = comments;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

}
