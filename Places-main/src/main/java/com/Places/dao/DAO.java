package com.Places.dao;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public class DAO {
	// PersistenceManagerFactory
	private static PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("BDD");
	
	 public static UserDao getUserDao() {
	        return new UserDaoImpl(pmf);
	    }
	 
	 public static PinDao getPinDao() {
	        return new PinDaoImpl(pmf);
	    }
	 
	 public static MapDao getMapDao() {
	        return new MapDaoImpl(pmf);
	    }
	 
	 public static CommentDao getCommentDao() {
	        return new CommentDaoImpl(pmf);
	    }

}
