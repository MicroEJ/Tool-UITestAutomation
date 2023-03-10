/*
 * Java
 *
 * Copyright 2022-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget.command;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import ej.fp.widget.DeviceHelper;
import ej.fp.widget.Display;
import ej.fp.widget.Menu;
import ej.fp.widget.util.FileHelper;

/**
 * Represents the override screenshot command.
 */
public class OverrideScreenshotCommand extends Command<Display> {

	private String id;
	/** PNG file type. */
	public static final String PNG = "PNG"; //$NON-NLS-1$
	/** The .png extension. */
	public static final String PNG_EXTENSION = ".png"; //$NON-NLS-1$
	private static final Logger LOGGER = Logger.getLogger(OverrideScreenshotCommand.class.getName());

	/**
	 * Constructor.
	 */
	public OverrideScreenshotCommand() {
		super();
		Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.ALL);
		LOGGER.addHandler(consoleHandler);
		LOGGER.setLevel(Level.ALL);
		LOGGER.setUseParentHandlers(false);
	}

	/**
	 * Parses the screenshot command information to save the screenshot id.
	 *
	 * @param args
	 *            array with screenshot command information.
	 */
	@Override
	public void parse(String... args) {
		this.id = args[1];
	}

	@Override
	public void execute() throws CommandExecutionException {
		LOGGER.info("Execute override screenshot"); //$NON-NLS-1$

		BufferedImage currentRawImage = DeviceHelper.getDeviceRawImage();

		String screenOverride = "screenshot" + this.id + PNG_EXTENSION; //$NON-NLS-1$
		File screenshotOverrideFile = Paths
				.get(Menu.getInstance().getFolder(), FileHelper.getSubfolder(), screenOverride).toFile();

		try {
			boolean success = ImageIO.write(currentRawImage, PNG, screenshotOverrideFile);
			LOGGER.info("Override '" + screenOverride + "': " + success); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (IOException e) {
			throw new CommandExecutionException(e);
		}
	}

	/**
	 * Serialize the screenshot id to the command.
	 *
	 * @return serialized id.
	 */
	@Override
	/* package */ String serialize() {
		return this.id;
	}

	/**
	 * Creates an override screenshot command with the specified id.
	 *
	 * @param id
	 *            id of the screenshot
	 * @return the command.
	 */
	public static OverrideScreenshotCommand with(String id) {
		OverrideScreenshotCommand command = new OverrideScreenshotCommand();
		command.id = id;
		return command;
	}

	@Override
	protected Class<Display> getMyType() {
		return Display.class;
	}
}
