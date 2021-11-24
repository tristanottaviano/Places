package com.Places.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.jdo.JDODataStoreException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

public class UserDaoImpl implements UserDao {

	private PersistenceManagerFactory pmf;

	public UserDaoImpl(PersistenceManagerFactory pmf) {
		this.pmf = pmf;
	}

	/**
	 * @param Username
	 * @return The User whose username corresponds to the argument
	 */
	@SuppressWarnings("unchecked")
	public User getUser(String username) {

		// List containing the user we search
		List<User> result;
		User user;
		Query q;
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();

			q = pm.newQuery(User.class, "username.equals(\"" + username + "\")");

			result = (List<User>) q.execute();

			try {
				user = (User) result.iterator().next();
			} catch (NoSuchElementException | IndexOutOfBoundsException e) {
				return null;

			}
			tx.commit();
		} finally {

			if (tx.isActive())
				tx.rollback();
		}
		q.close(result);
		return user;
	}

	/**
	 * @param Username, Password
	 * @return The created user
	 */

	/**
	 * @param Pin id
	 * @return the pin corresponding to the specified id
	 */

	public User getUser(long id) {

		User user;
		User detached = new User();
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();

			user = pm.getObjectById(User.class, id);

			detached = (User) pm.detachCopy(user);
			tx.commit();
		} finally {

			if (tx.isActive())
				tx.rollback();
		}
		pm.close();
		return detached;
	}

	public User login(String username, String password) {

		// Check if this username exists in database
		User user = getUser(username);
		if (user == null)
			return null;

		// Check if input password matches the user's password
		if (user.getPassword().equals(password))
			return user;
		else
			return null;

	}

	public User createUser(String username, String password, String bio) {

		// Check if username already exists in database
		if (getUser(username) != null)
			return null;

		User newUser = new User(username, password, bio);

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		tx.setRetainValues(true);

		tx.begin();
		try {
			pm.makePersistent(newUser);
		} catch (JDODataStoreException e) {
			System.out.println("Error creation");
			return null;
		}

		tx.commit();

		if (tx.isActive()) {
			tx.rollback();
		}
		pm.close();

		return newUser;
	}

	/**
	 * @param The user to be remove
	 * @return
	 */
	public void removeUser(long userId) {

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		tx.begin();
		try {
			User user = pm.getObjectById(User.class, userId);

			// Delete the map of this user
			for (int i = 0; i < user.getMyMaps().size(); i++)
				DAO.getMapDao().removeMap(user.getMyMaps().get(i));

			pm.deletePersistent(user);
			tx.commit();
		} finally {

			if (tx.isActive())
				tx.rollback();
		}

		pm.close();
	}

	/**
	 * @param The user to be added as friend
	 * @return the friend User
	 */
	public User addFriend(long userId, String friendUsername) {

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		User friendUser;
		tx.begin();
		try {

			User userAdder = pm.getObjectById(User.class, userId);

			// Check if input friend's username exists in database
			friendUser = getUser(friendUsername);
			if (friendUser == null)
				return null;
			userAdder.getContacts().add(friendUser.getUser_id());
			friendUser.getContacts().add(userId);
			userAdder.getMyRequests().remove((friendUser.getId()));
			tx.commit();
		} finally {

			if (tx.isActive())
				tx.rollback();
		}
		pm.close();
		return friendUser;

	}

	/**
	 * @param The user_id of current user
	 * @return current user friends list
	 */
	public List<User> getFriends(long userId) {

		List<User> friends = new ArrayList<User>();
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		tx.begin();
		try {

			User current = pm.getObjectById(User.class, userId);

			for (int i = 0; i < current.getContacts().size(); i++)
				friends.add(pm.getObjectById(User.class, current.getContacts().get(i)));

			tx.commit();
		} finally {

			if (tx.isActive())
				tx.rollback();
		}
		return friends;

	}

	/**
	 * @param The user of edited user
	 * @return edited user
	 */
	public User editUser(User user) {

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		tx.begin();
		try {
			User oldUser = pm.getObjectById(User.class, user.getId());

			if (!oldUser.getUsername().equals(user.getUsername()))
				oldUser.setUsername(user.getUsername());
			if (!oldUser.getPassword().equals(user.getPassword()))
				oldUser.setPassword(user.getPassword());
			if (!oldUser.getBio().equals(user.getBio()))
				oldUser.setBio(user.getBio());
			tx.commit();
		} finally {

			if (tx.isActive())
				tx.rollback();
		}
		pm.close();

		return null;
	}

	/**
	 * @param id of current user, friend Username
	 * @return friend User
	 */
	public User friendRequest(long userId, String friendUsername) {

		User friend = getUser(friendUsername);
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		tx.begin();
		try {

			if (friend != null) {
				// If request from this user already exists, return an error code that will be
				// read in the front
				if (friend.getMyRequests().contains((userId)))
					return new User("-1", "", "");
				// If the requested user is already a friend, return an error code that will be
				// read in the front
				else if (friend.getContacts().contains(userId))
					return new User("-2", "", "");
				else
					friend.getMyRequests().add(userId);
			}
			tx.commit();
		} finally {

			if (tx.isActive())
				tx.rollback();
		}
		pm.close();
		return friend;
	}

	/**
	 * @param id of current user, friend Username
	 * @return edited user
	 */
	// id du demandé, username du demandeur
	public User denyFriend(long userId, String friendUsername) {

		User friend = getUser(friendUsername);
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		User user = null;
		tx.begin();

		try {

			user = pm.getObjectById(User.class, userId);

			user.getMyRequests().remove((friend.getId()));
			tx.commit();
		} finally {

			if (tx.isActive())
				tx.rollback();
		}

		pm.close();
		return user;

	}

}
