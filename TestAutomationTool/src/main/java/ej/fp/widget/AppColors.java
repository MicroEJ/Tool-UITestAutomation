/*
 * Java
 *
 * Copyright 2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget;

import java.awt.Color;

/**
 * Provides functions to get different application colors.
 */
public class AppColors {

	private static final int GREY_COLOR_CODE = 225;
	private static final int GREEN_R_COLOR_CODE = 29;
	private static final int GREEN_G_COLOR_CODE = 150;
	private static final int GREEN_B_COLOR_CODE = 65;

	// Prevents initialization.
	private AppColors() {
	}

	/**
	 * Gets light grey color.
	 *
	 * @return the light grey color.
	 */
	public static Color getLightGrey() {
		return new Color(GREY_COLOR_CODE, GREY_COLOR_CODE, GREY_COLOR_CODE);
	}

	/**
	 * Gets light grey color.
	 *
	 * @return the light grey color.
	 */
	public static Color getGreenColor() {
		return new Color(GREEN_R_COLOR_CODE, GREEN_G_COLOR_CODE, GREEN_B_COLOR_CODE);
	}
}
