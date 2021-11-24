package com.Places.dao;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

public class MapDaoImpl implements MapDao {

	private PersistenceManagerFactory pmf;

	public MapDaoImpl(PersistenceManagerFactory pmf) {
		this.pmf = pmf;
	}

	/**
	 * @param user id
	 * @return the list of maps of a specific user
	 */

	public List<Map> getMaps(long userId) {

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		List<Map> userMapList = new ArrayList<Map>();
		List<Map> detachedMap = new ArrayList<Map>();
		try {
			tx.begin();

			User user = pm.getObjectById(User.class, userId);
			User detachedU = pm.detachCopy(user);

			// iterate through the user mapId list and get the maps
			for (int i = 0; i < user.getMyMaps().size(); i++)
				userMapList.add(pm.getObjectById(Map.class, detachedU.getMyMaps().get(i).longValue()));
			tx.commit();
		} finally {
			if (tx.isActive())
				tx.rollback();

		}
		detachedMap = (List<Map>) pm.detachCopyAll(userMapList);
		pm.close();
		return detachedMap;
	}

	/**
	 * @param mapId
	 * @return the map corresponding to the id
	 */
	public Map getMap(long id) {

		Map map = null;
		Map detached = new Map();
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();

			try {
				map = pm.getObjectById(Map.class, id);
			} catch (JDOObjectNotFoundException e) {
				System.out.println("No map by this id");
				return null;
			}
		} finally {

			if (tx.isActive())
				tx.rollback();
		}

		detached = pm.detachCopy(map);
		pm.close();
		return detached;

	}

	/**
	 * @return list of public maps
	 */

	@SuppressWarnings("unchecked")
	public List<Map> getPublicMaps() {

		// List containing the public maps
		List<Map> publicMapList = null;
		List<Map> detached = new ArrayList<Map>();

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		tx.setRetainValues(true);

		try {
			tx.begin();

			// Get all map where status == 1 (= public maps)
			Query q = pm.newQuery(Map.class, "status == 1");

			publicMapList = (List<Map>) q.execute();
			detached = (List<Map>) pm.detachCopyAll(publicMapList);

			tx.commit();
		} finally {

			if (tx.isActive()) {
				tx.rollback();
			}

			pm.close();
		}
		return detached;

	}

	/**
	 * @param id of the map creator, status (public/private) title of the map,
	 *           description, tags
	 * @return the created map
	 */
	public Map createMap(long currentUser, int status, String title, String description, String tags) {

		Map newMap = new Map(status, title, description, tags, currentUser);

		PersistenceManager pm = pmf.getPersistenceManager();

		Transaction tx = pm.currentTransaction();
		tx.setRetainValues(true);

		try {
			tx.begin();

			pm.makePersistent(newMap);
			User userAdder = pm.getObjectById(User.class, currentUser);

			userAdder.getMyMaps().add(newMap.getId());

			tx.commit();

		} finally {

			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
		return newMap;

	}

	/**
	 * @param id of the map to delete
	 * @return
	 */

	public void removeMap(long mapId) {

		PersistenceManager pm = pmf.getPersistenceManager();

		Map map = pm.getObjectById(Map.class, mapId);
		User user = pm.getObjectById(User.class, map.getUser());

		user.getMyMaps().remove(Long.valueOf(mapId));

		for (int i = 0; i < map.getPins().size(); i++)
			pm.deletePersistent(pm.getObjectById(Pin.class, map.getPins().get(i)));

		pm.deletePersistent(map);

		pm.close();

	}

	public Map editMap(Map map) {

		PersistenceManager pm = pmf.getPersistenceManager();
		Map oldMap;
		Map detached = null;
		Transaction tx = pm.currentTransaction();

		try {
			oldMap = pm.getObjectById(Map.class, map.getId());

			if (!oldMap.getTitle().equals(map.getTitle()))
				oldMap.setTitle(map.getTitle());
			if (!oldMap.getDescription().equals(map.getDescription()))
				oldMap.setDescription(map.getDescription());
			if (!oldMap.getTags().equals(map.getTags()))
				oldMap.setTags(map.getTags());
			if (oldMap.getStatus() != (map.getStatus()))
				oldMap.setStatus(map.getStatus());

			detached = pm.detachCopy(oldMap);
		} finally {

			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}

		return detached;
	}

}
