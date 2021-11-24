package com.Places.dao;

public interface PinDao {

	/**
	 * @param Pin id
	 * @return the pin corresponding to the specified id
	 */

	Pin getPin(long id);

	/**
	 * @param title of the pin, description of the pin, tags, location of pin, and
	 *              its map id
	 * @return the created pin
	 */
	Pin createPin(String title, String description, String tags, float latitude, float longitude, long mapId);

	/**
	 * @param id of the pin to delete
	 * @return
	 */

	void removePin(long pinId);

	/**
	 * @param Pin to edit
	 * @return
	 */
	Pin editPin(Pin newPin);

	/**
	 * @param id of the pin, image to add (in base64)
	 * @return
	 */
	void addImPin(long pinId, String image);

}
