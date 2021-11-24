package com.Places.dao;

import java.util.List;

public interface MapDao {

	/**
	 * @param user id
	 * @return the list of maps of a specific user
	 */

	List<Map> getMaps(long user);

	/**
	 * @param mapId
	 * @return the map corresponding to the id
	 */

	Map getMap(long id);

	/**
	 * @return list of public maps
	 */

	List<Map> getPublicMaps();

	/**
	 * @param id of the map creator, status (public/private) title of the map,
	 *           description, tags
	 * @return the created map
	 */
	Map createMap(long currentUser, int status, String title, String description, String tags);

	/**
	 * @param id of the map to delete
	 * @return
	 */

	void removeMap(long mapId);

	/**
	 * @param Map to edit
	 * @return Map
	 */

	Map editMap(Map map);

}
