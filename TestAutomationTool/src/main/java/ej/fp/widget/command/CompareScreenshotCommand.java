/*
 * Java
 *
 * Copyright 2021-2022 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget.command;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import ej.fp.Image;
import ej.fp.widget.Display;
import ej.fp.widget.Menu;
import ej.fp.widget.util.FileHelper;
import ej.fp.widget.util.ScreenshotComparator;

/**
 * Represents the compare screenshot command.
 */
public class CompareScreenshotCommand extends Command<Display> {

	private String id;
	private static final Logger LOGGER = Logger.getLogger(CompareScreenshotCommand.class.getName());

	/**
	 * Constructor.
	 */
	public CompareScreenshotCommand() {
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
		Display display = getWidget(null);
		Image displayBuffer = Menu.getVisibleBuffer(display);
		int[] imageBuffer = new int[displayBuffer.getWidth() * displayBuffer.getHeight()];
		displayBuffer.getPixels(imageBuffer);
		byte[] buffer = new byte[imageBuffer.length * 4];
		String screenTested = "screenshot" + this.id + ".raw"; //$NON-NLS-1$ //$NON-NLS-2$
		File screenshotFile = Paths.get(Menu.getInstance().getFolder(), FileHelper.getSubfolder(), screenTested)
				.toFile();

		File reportFile = Paths.get(Menu.getInstance().getFolder(), FileHelper.getSubfolder(),
				"test" + FileHelper.getLastReportFileID() + ".report").toFile(); //$NON-NLS-1$ //$NON-NLS-2$
		try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(reportFile, true))) {
			testImage(screenshotFile, buffer, imageBuffer, dataOutputStream, screenTested);
		} catch (IOException e) {
			throw new CommandExecutionException(e);
		}
	}

	private void testImage(File screenshotFile, byte[] buffer, int[] imageBuffer, DataOutputStream dataOutputStream,
			String screenTested) throws IOException, CompareScreenshotException {
		boolean success = false;
		try (DataInputStream dataInputStream = new DataInputStream(
				new BufferedInputStream(new FileInputStream(screenshotFile)))) {
			if (dataInputStream.read(buffer) == imageBuffer.length * 4) {
				success = ScreenshotComparator.compare(buffer, imageBuffer);
				LOGGER.info("Result of Comparison of '" + screenTested + "': " + success); //$NON-NLS-1$ //$NON-NLS-2$
			}

			if (success) {
				dataOutputStream.writeBytes(Messages.getString(Messages.SCREEN_TEST_START) + this.id
						+ Messages.getString(Messages.SCREEN_TEST_SUCCESS_END));
			} else {
				dataOutputStream.writeBytes(Messages.getString(Messages.SCREEN_TEST_START) + this.id
						+ Messages.getString(Messages.SCREEN_TEST_FAILED_END));
				throw new CompareScreenshotException(screenTested);
			}
		} catch (FileNotFoundException e) {
			dataOutputStream.writeBytes(Messages.getString(Messages.SCREENSHOT_FILE_NOT_FOUND_FIRST_PART) + screenTested
					+ Messages.getString(Messages.SCREENSHOT_FILE_NOT_FOUND_SECOND_PART));
			throw e;
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
	 * Creates a screenshot command with the specified id.
	 *
	 * @param id
	 *            id of the screenshot
	 * @return the command.
	 */
	public static CompareScreenshotCommand with(String id) {
		CompareScreenshotCommand command = new CompareScreenshotCommand();
		command.id = id;
		return command;
	}

	@Override
	protected Class<Display> getMyType() {
		return Display.class;
	}
}
