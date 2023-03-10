/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * Handles comparing of images.
 */
public class ScreenshotComparator {
	private static final Logger LOGGER = Logger.getLogger(ScreenshotComparator.class.getName());
	private static final int TWENTY_FOUR = 24;
	private static final int SIXTEEN = 16;
	private static final int EIGHT = 8;
	private static final int FOUR = 4;
	private static final int THREE = 3;
	private static final int HUNDRED_PERCENT = 100;

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
			byte a = (byte) (compare[i] >> TWENTY_FOUR);
			byte r = (byte) (compare[i] >> SIXTEEN);
			byte g = (byte) (compare[i] >> EIGHT);
			byte b = (byte) (compare[i]);
			if (a != pixels[i * FOUR] || r != pixels[i * FOUR + 1] || g != pixels[i * FOUR + 2]
					|| b != pixels[i * FOUR + THREE]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Compares two image files.
	 *
	 * @param fileA
	 *            the first image file.
	 * @param fileB
	 *            the second image file.
	 * @return the compare percentage.
	 */
	public static float compareImage(File fileA, File fileB) {

		float percentage = 0;
		try {
			// take buffer data from both image files //
			BufferedImage bufferedImageA = ImageIO.read(fileA);
			DataBuffer dataBufferA = bufferedImageA.getData().getDataBuffer();
			int sizeA = dataBufferA.getSize();
			BufferedImage bufferedImageB = ImageIO.read(fileB);

			DataBuffer dataBufferB = bufferedImageB.getData().getDataBuffer();
			int sizeB = dataBufferB.getSize();
			int count = 0;
			// compare data-buffer objects //
			if (sizeA == sizeB) {
				for (int i = 0; i < sizeA; i++) {

					if (dataBufferA.getElem(i) == dataBufferB.getElem(i)) {
						count = count + 1;
					}
				}
				percentage = (float) (count * HUNDRED_PERCENT) / sizeA;
			} else {
				LOGGER.info("Both the images are not of same size"); //$NON-NLS-1$
			}

		} catch (Exception e) {
			String msg = "Failed to compare image files ..." + e.getMessage(); //$NON-NLS-1$
			LOGGER.log(Level.SEVERE, msg, e);
		}
		return percentage;
	}
}
