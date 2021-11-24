package com.Places.ws;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.Places.dao.Comment;
import com.Places.dao.DAO;

@Path("/services")
public class WebServicesComments {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/maps/comment")
	public Comment createCommentMap(Comment comment) {

		return DAO.getCommentDao().createComment(comment.getUser(), comment.getTitle(), comment.getText(),
				comment.getNote(), comment.getObjectId(), true);

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/pins/comment")
	public Comment createCommentPin(Comment comment) {

		return DAO.getCommentDao().createComment(comment.getUser(), comment.getTitle(), comment.getText(),
				comment.getNote(), comment.getObjectId(), false);

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("maps/getComment/{mapId}")
	public List<Comment> getMapComments(@PathParam("mapId") long mapId) {

		return DAO.getCommentDao().getComments(mapId, true);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("pins/getComment/{pinId}")
	public List<Comment> getPinComments(@PathParam("pinId") long pinId) {

		return DAO.getCommentDao().getComments(pinId, false);
	}

}
