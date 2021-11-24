package com.Places.ws;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.Places.dao.DAO;
import com.Places.dao.User;

@Path("/services")
public class WebServicesUsers {

	public static class Request {
		public long senderId;
		public String friendUsername;

		public Request(long senderId, String friendUsername) {
			this.senderId = senderId;
			this.friendUsername = friendUsername;
		}

		public Request() {
		}

		public long getSenderId() {
			return senderId;
		}

		public void setSenderId(long senderId) {
			this.senderId = senderId;
		}

		public String getFriendUsername() {
			return friendUsername;
		}

		public void setFriendUsername(String friendUsername) {
			this.friendUsername = friendUsername;
		}

	}

	public static class Friends {
		public long friendId;
		public String senderUsername;

		public Friends(long friendId, String senderUsername) {
			this.friendId = friendId;
			this.senderUsername = senderUsername;
		}

		public Friends() {
		}

		public long getFriendId() {
			return friendId;
		}

		public void setFriendId(long friendId) {
			this.friendId = friendId;
		}

		public String getSenderUsername() {
			return senderUsername;
		}

		public void setSenderUsername(String senderUsername) {
			this.senderUsername = senderUsername;
		}

	}

	public static class Login {
		public String username;
		public String password;

		public Login(String username, String password) {
			this.username = username;
			this.password = password;
		}

		public Login() {
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

	}

	// Create a user
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/users/create")
	public User createUser(User user) {
		return DAO.getUserDao().createUser(user.getUsername(), user.getPassword(), user.getBio());
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/users/connect")
	public User login(Login login) {
		return DAO.getUserDao().login(login.username, login.password);
	}

	// Get a user by user_id
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/users/search/{user_id}")
	public User searchUser(@PathParam("user_id") long user_id) {
		return DAO.getUserDao().getUser(user_id);
	}

	// Remove a user
	@GET
	@Path("/users/delete/{userId}")
	public void removeUser(@PathParam("userId") long userId) {
		DAO.getUserDao().removeUser(userId);
	}

	// Make a friend request
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/users/request")
	public User friendRequest(Request req) {

		return DAO.getUserDao().friendRequest(req.senderId, req.friendUsername);

	}

	// Deny a friend request
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/friends/deny")
	public User friendRequestDeny(Friends friend) {
		return DAO.getUserDao().denyFriend(friend.friendId, friend.senderUsername);

	}

	// Accept Friend request
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/friends/add")
	public User addFriend(Friends friend) {

		return DAO.getUserDao().addFriend(friend.friendId, friend.senderUsername);

	}

	// Add a friend to a user
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/friends/user/{user_id}")
	public List<User> getFriends(@PathParam("user_id") long user_id) {

		return DAO.getUserDao().getFriends(user_id);

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/users/edit")
	public User editUser(User user) {
		return DAO.getUserDao().editUser(user);
	}

}
