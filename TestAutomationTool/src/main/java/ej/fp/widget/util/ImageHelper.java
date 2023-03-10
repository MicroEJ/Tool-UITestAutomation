/*
 * Java
 *
 * Copyright 2022-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ej.fp.widget.Menu;

/**
 * Handles creation mask for images and image copy.
 */
public class ImageHelper {

	// Prevents initialization.
	private ImageHelper() {
	}

	/**
	 * Add a mask to BufferedImage.
	 *
	 * @param rawImage
	 *            buffered image.
	 * @param x
	 *            The x coordinate relative to Mask position.
	 * @param y
	 *            The y coordinate relative to Mask position.
	 * @param width
	 *            The width of the Mask.
	 * @param height
	 *            The height of the Mask.
	 * @param maskColor
	 *            the mask color to use.
	 * @return the buffered image with applied mask.
	 */
	public static BufferedImage addMask(BufferedImage rawImage, int x, int y, int width, int height, int maskColor) {
		int color = maskColor;

		if (color == Menu.AUTO_COLOR) {
			color = invertColor(getDominantColor(rawImage));
		}

		for (int i = x; i < (x + width); i++) {
			int maskX = i;
			for (int j = y; j < (y + height); j++) {
				int maskY = j;
				// Check if pixel is not out of bounds.
				if (maskX < rawImage.getWidth() && maskY < rawImage.getHeight()) {
					rawImage.setRGB(maskX, maskY, color);
				}
			}
		}
		return rawImage;
	}

	/**
	 * Copy a bufferedImage.
	 *
	 * @param source
	 *            the source image.
	 * @return the copy of buffered image.
	 */
	public static BufferedImage copyImage(BufferedImage source) {
		BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
		Graphics g = b.getGraphics();
		g.drawImage(source, 0, 0, null);
		g.dispose();
		return b;
	}

	private static int getDominantColor(BufferedImage rawImage) {
		Map<String, Integer> color2counter = new HashMap<>();
		for (int x = 0; x < rawImage.getWidth(); x++) {
			for (int y = 0; y < rawImage.getHeight(); y++) {
				int color = rawImage.getRGB(x, y);
				Integer occurrences = color2counter.get(Integer.valueOf(color));
				Integer newOccurences;
				if (occurrences != null) {
					newOccurences = new Integer(occurrences.intValue() + 1);
				} else {
					newOccurences = new Integer(1);
				}
				color2counter.put(String.valueOf(color), newOccurences);
			}
		}

		return getMostCommonColour(color2counter);
	}

	private static int getMostCommonColour(Map<String, Integer> map) {
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
			}
		});
		Map.Entry me = (Map.Entry) list.get(list.size() - 1);
		String mostCommonColor = (String) me.getKey();

		return Integer.parseInt(mostCommonColor);
	}

	private static int invertColor(int color) {

		Color inverted = new Color(255 - new Color(color).getRed(), 255 - new Color(color).getGreen(),
				255 - new Color(color).getBlue());

		return inverted.getRGB();
	}

}
