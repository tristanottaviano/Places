package com.Places.dao;

import java.util.List;

public interface CommentDao {

	/**
	 * @param comment id
	 * @return Comment corresponding to the id
	 */
	Comment getComment(long id);

	/**
	 * @param id of pin or map, boolean if map
	 * @return List of comments of the specified pinId or mapId
	 */
	List<Comment> getComments(long objectId, boolean map);

	/**
	 * @param author id , title, text, note, id of the object (pin/map), boolean if
	 *               map comment
	 * @return Comment to be posted on the pin/map
	 */
	Comment createComment(long user, String title, String text, int note, long objectId, boolean map);

	/**
	 * @param commentId of comment to be remove
	 * @return
	 */
	void removeComment(long commentId);

}
