/*
 * Java
 *
 * Copyright 2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget;

import java.util.logging.Logger;

import ej.fp.widget.util.JSONException;
import ej.fp.widget.util.JSONObject;

/**
 * Create the mask and saving their coordinates.
 */
public class Mask {
	private static final Logger LOGGER = Logger.getLogger(Mask.class.getName());
	private static final String MASK_X_KEY = "x"; //$NON-NLS-1$
	private static final String MASK_Y_KEY = "y"; //$NON-NLS-1$
	private static final String MASK_WIDTH_KEY = "width"; //$NON-NLS-1$
	private static final String MASK_HEIGHT_KEY = "height"; //$NON-NLS-1$
	private static final String EMPTY_TEXT = ""; //$NON-NLS-1$
	private static final String EXCEPTION = "Exception";//$NON-NLS-1$
	private static final String CONVERT_MASK_TO_JSON_EXCEPTION = "Convert Mask to JSON Exception ";//$NON-NLS-1$

	private final int index;
	private String title;
	private int x;
	private int y;
	private int width;
	private int height;

	/**
	 * Creates the mask.
	 *
	 * @param index
	 *            index of the mask.
	 *
	 * @param title
	 *            title of the mask.
	 * @param x
	 *            position of the mask on the abscissa axis.
	 * @param y
	 *            mask position on the y-axis.
	 * @param width
	 *            width of the mask.
	 * @param height
	 *            height of the mask.
	 */
	public Mask(int index, String title, int x, int y, int width, int height) {
		this.index = index;
		this.title = title;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Creates the mask.
	 *
	 * @param index
	 *            index of the mask.
	 */
	public Mask(int index) {
		this.index = index;
		this.title = EMPTY_TEXT;
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
	}

	/**
	 * Gets the index.
	 *
	 * @return the index.
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Sets the title.
	 *
	 * @param newTitle
	 *            the new title.
	 *
	 */

	public void setTitle(String newTitle) {
		this.title = newTitle;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x.
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y.
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width.
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height.
	 */
	public int getHeight() {
		return this.height;
	}

	// this method return the value to show in the JComboBox
	@Override
	public String toString() {
		return this.title;
	}

	/**
	 * Sets the Mask fields from JSONObject.
	 *
	 * @param object
	 *            the JSONOBject.
	 */
	public void fromJsonObject(JSONObject object) {
		try {
			Integer xValue = (Integer) object.get(MASK_X_KEY);
			this.x = xValue.intValue();
		} catch (JSONException e) {
			String message = EXCEPTION + e.getMessage();
			LOGGER.info(message);
			this.x = 0;
		}

		try {
			Integer yValue = (Integer) object.get(MASK_Y_KEY);
			this.y = yValue.intValue();
		} catch (JSONException e) {
			String message = EXCEPTION + e.getMessage();
			LOGGER.info(message);
			this.y = 0;
		}
		try {
			Integer widthValue = (Integer) object.get(MASK_WIDTH_KEY);
			this.width = widthValue.intValue();
		} catch (JSONException e) {
			String message = EXCEPTION + e.getMessage();
			LOGGER.info(message);
			this.width = 0;
		}

		try {
			Integer heightValue = (Integer) object.get(MASK_HEIGHT_KEY);
			this.height = heightValue.intValue();
		} catch (JSONException e) {
			String message = EXCEPTION + e.getMessage();
			LOGGER.info(message);
			this.height = 0;
		}
	}

	/**
	 * Gets the Mask JSON Object.
	 *
	 * @return the Mask JSON Object.
	 */
	public JSONObject toJsonObject() {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(MASK_X_KEY, this.x);
			jsonObject.put(MASK_Y_KEY, this.y);
			jsonObject.put(MASK_WIDTH_KEY, this.width);
			jsonObject.put(MASK_HEIGHT_KEY, this.height);
		} catch (JSONException e) {
			String message = CONVERT_MASK_TO_JSON_EXCEPTION + e.getMessage();
			LOGGER.info(message);
		}

		return jsonObject;

	}

}
