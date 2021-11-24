package com.Places.dao;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

public class CommentDaoImpl implements CommentDao {

	private PersistenceManagerFactory pmf;

	public CommentDaoImpl(PersistenceManagerFactory pmf) {
		this.pmf = pmf;
	}

	public CommentDaoImpl() {
		this.pmf = JDOHelper.getPersistenceManagerFactory("BDD");

	}

	/**
	 * @param comment_id
	 * @return Comment corresponding to the id
	 */
	public Comment getComment(long id) {

		PersistenceManager pm = pmf.getPersistenceManager();
		Comment comment;
		Comment detached = new Comment();
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();

			comment = pm.getObjectById(Comment.class, id);
			detached = pm.detachCopy(comment);
			tx.commit();
		} finally {
			if (tx.isActive())
				tx.rollback();

		}

		pm.close();
		return detached;
	}

	// Return all comments from a specified map or pin
	@SuppressWarnings("unchecked")
	public List<Comment> getComments(long id, boolean map) {

		List<Comment> comments = null;
		List<Comment> detached = new ArrayList<Comment>();

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();

			Query q = pm.newQuery(Comment.class, "objectId == " + id);
			comments = (List<Comment>) q.execute();
			detached = (List<Comment>) pm.detachCopyAll(comments);


	
			for(int i=0; i<detached.size();i++) {
				if(detached.get(i).isMap() != map)
					detached.remove(i);
			}


			tx.commit();
		} finally {

			if (tx.isActive()) {	
				tx.rollback();
			}
		}
		pm.close();
		return detached;
	}

	/**
	 * @param User , title, text, map
	 * @return Comment to be posted on the pin
	 */
	public Comment createComment(long user, String title, String text, int note, long objectId, boolean map) {

		Comment newComment = new Comment(user, title, text, note, objectId, map);

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		tx.setRetainValues(true);
		try {
			tx.begin();

			pm.makePersistent(newComment);

			if (map) {

				Map mapC = pm.getObjectById(Map.class, objectId);
				mapC.getComments().add(newComment.getId());

			}

			else {

				Pin pinC = pm.getObjectById(Pin.class, objectId);
				pinC.getComments().add(newComment.getId());
			}

			tx.commit();
		} finally {
			if (tx.isActive())
				tx.rollback();

		}

		pm.close();
		return newComment;
	}

	/**
	 * @param Comment to be remove
	 * @return
	 */
	public void removeComment(long commentId) {

		PersistenceManager pm = pmf.getPersistenceManager();
		Comment comment;
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();

			comment = pm.getObjectById(Comment.class, commentId);

			// If comment is a map comment, delete the comment from the map
			if (comment.map) {
				Map map = pm.getObjectById(Map.class, comment.objectId);
				map.getComments().remove(Long.valueOf(commentId));
			}

			// else if pin comment, delete the comment from the pin
			else {
				Pin pin = pm.getObjectById(Pin.class, comment.objectId);
				pin.getComments().remove(Long.valueOf(commentId));
			}

			pm.deletePersistent(comment);

			tx.commit();
		} finally {
			if (tx.isActive())
				tx.rollback();

		}

		pm.close();
	}

}
