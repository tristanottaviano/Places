package com.Places.dao;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Id;

@PersistenceCapable
public class Comment {

	@Id
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
	long id;

	long user;
	String title;
	String text;
	int note;

	// id pin or map
	long objectId;
	// True = map comment, False = pin comment
	boolean map;


	public Comment(long user, String title, String text, int note, long objectId, boolean map) {
		this.user = user;
		this.title = title;
		this.text = text;
		this.note = note;
		this.objectId = objectId;
		this.map = map;
	}

	public Comment(String title) {
		this.title = title;

	}

	public Comment(int id) {
		this.id = id;

	}

	public Comment() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUser() {
		return user;
	}

	public void setUser(long user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getNote() {
		return note;
	}

	public void setNote(int note) {
		this.note = note;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public boolean isMap() {
		return map;
	}

	public void setMap(boolean map) {
		this.map = map;
	}

}
