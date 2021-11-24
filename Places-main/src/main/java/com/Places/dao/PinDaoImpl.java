	package com.Places.dao;

import java.util.Arrays;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

public class PinDaoImpl implements PinDao {

	private PersistenceManagerFactory pmf;

	public PinDaoImpl(PersistenceManagerFactory pmf) {
		this.pmf = pmf;
	}

	/**
	 * @param Pin id
	 * @return the pin corresponding to the specified id
	 */

	public Pin getPin(long id) {

		Pin pin = null;
		Pin detached = new Pin();
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();

			pin = pm.getObjectById(Pin.class, id);
			detached = pm.detachCopy(pin);

			tx.commit();
		} finally {
			if (tx.isActive())
				tx.rollback();

		}

		pm.close();

		return detached;

	}

	public List<String> convertTags(String tags) {

		return Arrays.asList(tags.split("\\s*,\\s*"));
	}

	/**
	 * @param title of the pin, description of the pin, tags, location of pin, and
	 *              its map
	 * @return the created pin
	 */
	public Pin createPin(String title, String description, String tags, float latitude, float longitude, long mapId) {

		Pin pin = new Pin(title, description, tags, latitude, longitude, mapId);

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();

			pm.makePersistent(pin);

			// Add the pinId to the map's pins list
			Map currentMap = pm.getObjectById(Map.class, mapId);
			currentMap.getPins().add(pin.getId());

			tx.commit();
		} finally {

			if (tx.isActive())
				tx.rollback();
		}
		pm.close();

		return pin;
	}

	/**
	 * @param id of the pin to delete
	 * @return
	 */

	public void removePin(long pinId) {

		PersistenceManager pm = pmf.getPersistenceManager();
		Pin pin = pm.getObjectById(Pin.class, pinId);
		Transaction tx = pm.currentTransaction();
		tx.setRetainValues(true);
		try {
			tx.begin();

			// Remove the pin from the map pins list
			Map map = pm.getObjectById(Map.class, pin.getMap());
			map.getPins().remove(Long.valueOf(pinId));
			pm.deletePersistent(pin);

			tx.commit();
		} finally {

			if (tx.isActive())
				tx.rollback();
		}

		pm.close();
	}

	/**
	 * @param Pin to edit
	 * @return edited Pin
	 */
	public Pin editPin(Pin newPin) {

		PersistenceManager pm = pmf.getPersistenceManager();
		Pin oldPin;
		Pin detached = null;
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();

			oldPin = pm.getObjectById(Pin.class, newPin.getId());

			if (!oldPin.getTitle().equals(newPin.getTitle()))
				oldPin.setTitle(newPin.getTitle());
			if (!oldPin.getDescription().equals(newPin.getDescription()))
				oldPin.setDescription(newPin.getDescription());
			if (!oldPin.getTags().equals(newPin.getTags()))
				oldPin.setTags(newPin.getTags());
			tx.commit();
		} finally {

			if (tx.isActive())
				tx.rollback();
		}
		detached = pm.detachCopy(oldPin);
		pm.close();
		return detached;

	}

	/**
	 * @param id of the pin, image to add (in base64)
	 * @return
	 */
	public void addImPin(long pinId, String image) {

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();

			Pin pin = pm.getObjectById(Pin.class, pinId);

			pin.getImages().add(image);
			tx.commit();
		} finally {

			if (tx.isActive())
				tx.rollback();
		}
		pm.close();

	}

}
