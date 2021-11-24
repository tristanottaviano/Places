package com.Places.dao;

import java.util.List;

public interface UserDao {

	/**
	 * @param Username
	 * @return The User whose username corresponds to the argument
	 */
	User getUser(String username);

	/**
	 * @param id
	 * @return The User whose user_id corresponds to the argument
	 */
	User getUser(long id);

	/**
	 * @param username, password, bio
	 * @return The created user
	 */
	User createUser(String username, String password, String bio);

	/**
	 * @param Username, Password
	 * @return The user if correct input
	 */
	User login(String username, String password);

	/**
	 * @param The userId of the user to remove
	 * @return
	 */
	void removeUser(long userId);

	/**
	 * @param The userId, friend's username
	 * @return the friend User
	 */
	User addFriend(long user, String friend);

	/**
	 * @param The userId of current user
	 * @return current user friends list
	 */
	List<User> getFriends(long userId);

	/**
	 * @param The user of edited user
	 * @return edited user
	 */
	User editUser(User user);

	/**
	 * @param id of current user, friend Username
	 * @return friend User
	 */
	User friendRequest(long userId, String friendUsername);

	/**
	 * @param id of current user, friend Username
	 * @return denied User
	 */
	User denyFriend(long userId, String friendUsername);

}
