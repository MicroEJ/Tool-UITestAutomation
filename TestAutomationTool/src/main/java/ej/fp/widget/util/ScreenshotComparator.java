/*
 * Java
 *
 * Copyright 2021-2022 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget.util;

/**
 * Handles comparing of images.
 */
public class ScreenshotComparator {

	private ScreenshotComparator() {
	}

	/**
	 * Compares two pixel arrays.
	 *
	 * @param pixels
	 *            the first pixel array
	 * @param compare
	 *            the second pixel array
	 * @return true if they are equal, false otherwise
	 */
	public static boolean compare(byte[] pixels, int[] compare) {
		for (int i = 0; i < compare.length; i++) {
			byte a = (byte) (compare[i] >> 24);
			byte r = (byte) (compare[i] >> 16);
			byte g = (byte) (compare[i] >> 8);
			byte b = (byte) (compare[i]);
			if (a != pixels[i * 4] || r != pixels[i * 4 + 1] || g != pixels[i * 4 + 2] || b != pixels[i * 4 + 3]) {
				return false;
			}
		}
		return true;
	}
}
