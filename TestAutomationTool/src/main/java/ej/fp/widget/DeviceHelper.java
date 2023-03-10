/*
 * Java
 *
 * Copyright 2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget;

import java.awt.image.BufferedImage;

import ej.fp.Device;
import ej.fp.widget.util.ImageHelper;

/**
 * Manages the image displayed on the front panel of the device.
 */
public class DeviceHelper {

	// Prevents initialization.
	private DeviceHelper() {
	}

	/**
	 * Returns the copy of raw image from a visible buffer of the device display.
	 *
	 * @return a copy of visible raw image.
	 */
	public static BufferedImage getDeviceRawImage() {
		Display display = Device.getDevice().getWidget(Display.class, null);
		ej.fp.Image displayBuffer = display.visibleBuffer;
		BufferedImage rawImage = (BufferedImage) displayBuffer.getRAWImage();
		return ImageHelper.copyImage(rawImage);
	}
}
