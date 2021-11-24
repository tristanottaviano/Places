package com.Places.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.Places.dao.DAO;
import com.Places.dao.Pin;

@Path("/services")
public class WebServicesPins {

	public static class Delete {

		long pinId;
		long mapId;

		public Delete(long pinId, long mapId) {
			this.pinId = pinId;
			this.mapId = mapId;
		}

		public Delete() {
		}

		public long getPinId() {
			return pinId;
		}

		public void setPinId(long pinId) {
			this.pinId = pinId;
		}

		public long getMapId() {
			return mapId;
		}

		public void setMapId(long mapId) {
			this.mapId = mapId;
		}

	}

	public static class Image {

		long pinId;
		String image;

		public Image(long pinId, String image) {
			this.pinId = pinId;
			this.image = image;
		}

		public long getPinId() {
			return pinId;
		}

		public void setPinId(long pinId) {
			this.pinId = pinId;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public Image() {
		}

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/pins/create")
	public Pin createPin(Pin pin) {
		return DAO.getPinDao().createPin(pin.getTitle(), pin.getDescription(), pin.getTags(), pin.getLatitude(),
				pin.getLongitude(), pin.getMap());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/pins/search/{pin_id}")
	public Pin getPin(@PathParam("pin_id") long pin_id) {
		return DAO.getPinDao().getPin(pin_id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/pins/edit")
	public Pin editPin(Pin pin) {
		return DAO.getPinDao().editPin(pin);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/pins/images/add")
	public void addImPin(Image im) {
		DAO.getPinDao().addImPin(im.pinId, im.image);
	}

	@GET
	@Path("/pins/delete/{pinId}")
	public void deletePin(@PathParam("pinId") long pinId) {

		DAO.getPinDao().removePin(pinId);
	}

}
