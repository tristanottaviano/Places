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
import com.Places.dao.Map;

@Path("/services")
public class WebServicesMap {

	// modify existing map
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/maps/edit")
	public Map editMap(Map map) {
		return DAO.getMapDao().editMap(map);

	}

	// Return list of user's map
	// modify existing map
	@GET
	@Path("/maps/user/{user_id}")
	public List<Map> getMapsUser(@PathParam("user_id") long user_id) {

		return DAO.getMapDao().getMaps(user_id);
	}

	// Create a map
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/maps/create")
	public Map createMap(Map map) {
		return DAO.getMapDao().createMap(map.getUser(), map.getStatus(), map.getTitle(), map.getDescription(),
				map.getTags());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/maps/search/{map_id}")
	public Map getMap(@PathParam("map_id") long map_id) {
		return DAO.getMapDao().getMap(map_id);

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/maps/publicMaps")
	public List<Map> getPublicMaps() {
		return DAO.getMapDao().getPublicMaps();

	}

	@GET
	@Path("/maps/delete/{mapId}")
	public void deleteMap(@PathParam("mapId") long mapId) {

		DAO.getMapDao().removeMap(mapId);
	}

}
