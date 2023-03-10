/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget.command;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import ej.fp.widget.DeviceHelper;
import ej.fp.widget.Display;
import ej.fp.widget.Mask;
import ej.fp.widget.Menu;
import ej.fp.widget.util.FileHelper;
import ej.fp.widget.util.ImageHelper;
import ej.fp.widget.util.JSONArray;
import ej.fp.widget.util.JSONException;
import ej.fp.widget.util.JSONObject;
import ej.fp.widget.util.ScreenshotComparator;

/**
 * Represents the compare screenshot command.
 */
public class CompareScreenshotCommand extends Command<Display> {
	private static final String COMMA = ","; //$NON-NLS-1$
	private static final int COMMAND_ID = 1;
	private static final int COMMAND_JSON_MASKS = 2;
	private static final double HUNDRED_PERCENT = 100.0;
	private String id;

	/**
	 * The mask list.
	 */
	private List<Mask> maskList;

	/** PNG file type. */
	public static final String PNG = "PNG"; //$NON-NLS-1$
	/** The .png extension. */
	public static final String PNG_EXTENSION = ".png"; //$NON-NLS-1$
	private static final String REPORT_EXTENSION = ".report"; //$NON-NLS-1$
	private static final String TMP = "tmp"; //$NON-NLS-1$
	private static final String SCREENSHOT_WITH_MASK_TMP = "screnshot_with_mask_tmp"; //$NON-NLS-1$
	private static final String EMPTY_TITLE = "";//$NON-NLS-1$
	private static final String CONVERT_JSON_MASKS_EXCEPTION = "convertJsonMasks exception ";//$NON-NLS-1$
	private static final String IGNORE_EXCEPTION = " .Ignore exception and contine without masks.";//$NON-NLS-1$
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
		this.id = args[COMMAND_ID];
		String masksJsonString = jsonArgs(args);
		this.maskList = convertJsonMasks(masksJsonString);
		if (this.maskList.isEmpty()) {
			this.maskList.add(new Mask(0, EMPTY_TITLE, 0, 0, 0, 0));
		}
	}

	private String jsonArgs(String... args) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = COMMAND_JSON_MASKS; i < args.length; i++) {
			stringBuilder.append(args[i]);
			if (i < args.length - 1) {
				stringBuilder.append(COMMA);
			}
		}
		return stringBuilder.toString();
	}

	private ArrayList<Mask> convertJsonMasks(String jsonMasks) {
		ArrayList<Mask> masksList = new ArrayList<>();
		try {
			JSONArray masksJsonArray = new JSONArray(jsonMasks);

			for (int i = 0; i < masksJsonArray.length(); i++) {

				Mask mask = new Mask(i);
				JSONObject maskJsonObject = (JSONObject) masksJsonArray.get(i);
				mask.fromJsonObject(maskJsonObject);
				masksList.add(mask);
			}

		} catch (JSONException e) {
			String message = CONVERT_JSON_MASKS_EXCEPTION + e.getMessage() + IGNORE_EXCEPTION;
			LOGGER.info(message);
		}

		return masksList;
	}

	@Override
	public void execute() throws CommandExecutionException {
		BufferedImage currentRawImage = DeviceHelper.getDeviceRawImage();
		String screenTested = "screenshot" + this.id + PNG_EXTENSION; //$NON-NLS-1$
		File screenshotFile = Paths.get(Menu.getInstance().getFolder(), FileHelper.getSubfolder(), screenTested)
				.toFile();

		File reportFile = Paths.get(Menu.getInstance().getFolder(), FileHelper.getSubfolder(),
				"test" + FileHelper.getLastReportFileID() + REPORT_EXTENSION).toFile(); //$NON-NLS-1$
		try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(reportFile, true))) {
			// Create a temporary png image from current displayed image that will be used for screenshot comparison.
			File currentImageFile = Files.createTempFile(TMP, PNG_EXTENSION).toFile();
			// Add a mask to the current temp image. (If all parameters are equal to zero we keep the image without
			// mask).

			if (hasMask()) {
				File screenshotTmpImageFile = null;
				// Create a temporary png for saved screenshot and add a mask to it before comparison.

				screenshotTmpImageFile = Files.createTempFile(SCREENSHOT_WITH_MASK_TMP, PNG_EXTENSION).toFile();
				BufferedImage tmpCompareBufferedImage = ImageIO.read(screenshotFile);
				for (int i = 0; i < this.maskList.size(); i++) {

					Mask mask = this.maskList.get(i);
					ImageIO.write(
							ImageHelper.addMask(currentRawImage, mask.getX(), mask.getY(), mask.getWidth(),
									mask.getHeight(), Menu.getInstance().getCurrentSelectedColorMask()),
							PNG, currentImageFile);

					ImageIO.write(
							ImageHelper.addMask(tmpCompareBufferedImage, mask.getX(), mask.getY(), mask.getWidth(),
									mask.getHeight(), Menu.getInstance().getCurrentSelectedColorMask()),
							PNG, screenshotTmpImageFile);

				}

				if (screenshotTmpImageFile != null) {
					testImage(screenshotTmpImageFile, currentImageFile, dataOutputStream, screenTested);
				}
			} else {
				Mask firstMask = this.maskList.get(0);
				ImageIO.write(
						ImageHelper.addMask(currentRawImage, firstMask.getX(), firstMask.getY(), firstMask.getWidth(),
								firstMask.getHeight(), Menu.getInstance().getCurrentSelectedColorMask()),
						PNG, currentImageFile);

				testImage(screenshotFile, currentImageFile, dataOutputStream, screenTested);
			}

		} catch (IOException e) {
			throw new CommandExecutionException(e);
		}
	}

	private void testImage(File screenshotFile, File currentTmpFile, DataOutputStream dataOutputStream,
			String screenTested) throws IOException, CompareScreenshotException {
		boolean success = false;
		if (!screenshotFile.exists()) {
			dataOutputStream.writeBytes(Messages.getString(Messages.SCREENSHOT_FILE_NOT_FOUND_FIRST_PART) + screenTested
					+ Messages.getString(Messages.SCREENSHOT_FILE_NOT_FOUND_SECOND_PART));

			throw new FileNotFoundException();
		}
		float percent = ScreenshotComparator.compareImage(screenshotFile, currentTmpFile);
		Menu.getInstance().setCurrentImage(currentTmpFile);
		Menu.getInstance().setComparedImage(screenshotFile);
		Menu.getInstance().setComparePercent(percent);

		LOGGER.info("Compare percent: " + percent); //$NON-NLS-1$

		if (percent == HUNDRED_PERCENT) {
			success = true;
		}
		LOGGER.info("Result of Comparison of '" + screenTested + "': " + success); //$NON-NLS-1$ //$NON-NLS-2$
		currentTmpFile.deleteOnExit();
		if (screenshotFile.getName().contains(SCREENSHOT_WITH_MASK_TMP)) {
			screenshotFile.deleteOnExit();
		}

		if (success) {
			dataOutputStream.writeBytes(Messages.getString(Messages.SCREEN_TEST_START) + this.id
					+ Messages.getString(Messages.SCREEN_TEST_SUCCESS_END));
		} else {
			Menu.getInstance().resetButtonsAndState();
			dataOutputStream.writeBytes(Messages.getString(Messages.SCREEN_TEST_START) + this.id
					+ Messages.getString(Messages.SCREEN_TEST_FAILED_END));
			throw new CompareScreenshotException(screenTested);
		}
	}

	/**
	 * Serialize the screenshot id to the command.
	 *
	 * @return serialized id.
	 */
	@Override
	/* package */ String serialize() {

		return this.id + COMMA + serializeMaskArray();
	}

	private String serializeMaskArray() {

		JSONArray jsonArray = new JSONArray();

		for (int i = 0; i < this.maskList.size(); i++) {
			jsonArray.put(this.maskList.get(i).toJsonObject());
		}
		return jsonArray.toString();

	}

	/**
	 * Creates a screenshot command with the specified id.
	 *
	 * @param id
	 *            id of the screenshot.
	 * @param maskList
	 *            the mask list.
	 * @return the command.
	 */
	public static CompareScreenshotCommand with(String id, List<Mask> maskList) {
		CompareScreenshotCommand command = new CompareScreenshotCommand();
		command.id = id;
		command.maskList = new ArrayList<>();
		if (maskList != null && !maskList.isEmpty()) {
			for (int i = 0; i < maskList.size(); i++) {
				command.maskList.add(maskList.get(i));
			}

		} else {
			command.maskList.add(new Mask(0, EMPTY_TITLE, 0, 0, 0, 0));
		}
		return command;
	}

	@Override
	protected Class<Display> getMyType() {
		return Display.class;
	}

	private boolean hasMask() {
		boolean hasMask = false;

		if (this.maskList != null && !this.maskList.isEmpty()) {
			hasMask = true;
		}

		return hasMask;
	}
}
