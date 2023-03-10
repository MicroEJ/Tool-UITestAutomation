/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import ej.fp.widget.Menu;

/**
 * Handles creation of files and stores the last file.
 */
public class FileHelper {
	private static final Logger LOGGER = Logger.getLogger(FileHelper.class.getName());
	private static final String EMPTY_TEXT = ""; //$NON-NLS-1$
	private static final String T_TEXT = "T"; //$NON-NLS-1$
	private static final String DASH_SEPARATOR = "-"; //$NON-NLS-1$
	private static final String TWO_POINT_SEPARATOR = ":"; //$NON-NLS-1$
	private static final String POINT = "."; //$NON-NLS-1$
	private static String lastReportFileID = EMPTY_TEXT;
	private static String lastCommandFileID = EMPTY_TEXT;
	private static String subfolder = EMPTY_TEXT;
	private static String lastReportFilePath;

	private FileHelper() {
	}

	/**
	 * Gets the ID of the last generated report file.
	 *
	 * @return last generated report file.
	 */
	public static String getLastReportFileID() {
		return lastReportFileID;
	}

	/**
	 * Gets the file path of the last report.
	 *
	 * @return last generated report file path or null if none was created
	 */
	public static String getLastReportFilePath() {
		return lastReportFilePath;
	}

	/**
	 * Creates a new report file.
	 */
	public static void createNewReportFile() {
		String timestamp = LocalDateTime.now().toString();
		timestamp = timestamp.replace(DASH_SEPARATOR, EMPTY_TEXT).replace(T_TEXT, EMPTY_TEXT)
				.replace(TWO_POINT_SEPARATOR, EMPTY_TEXT);
		timestamp = timestamp.substring(0, timestamp.indexOf(POINT));
		File file = Paths.get(Menu.getInstance().getFolder(), subfolder, "test" + timestamp + ".report").toFile(); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			if (!file.exists() && !new File(file.getParent()).exists()) {
				new File(file.getParent()).mkdirs();
			}
			Files.createFile(file.toPath());
			lastReportFilePath = file.getAbsolutePath();
			lastReportFileID = timestamp;
		} catch (IOException e) {
			String msg = "Failed to create report file ..." + e.getMessage(); //$NON-NLS-1$
			LOGGER.log(Level.FINE, msg, e);
		}
	}

	/**
	 * Gets the ID of the last generated scenario file to store commands.
	 *
	 * @return last generated commands file.
	 */
	public static String getLastCommandFileID() {
		return lastCommandFileID;
	}

	/**
	 * Gets a default scenario name generated form the current timestamp.
	 *
	 * @return the scenario name generated.
	 */
	public static String getDefaultScenarioName() {
		String timestamp = LocalDateTime.now().toString();
		timestamp = timestamp.replace(DASH_SEPARATOR, EMPTY_TEXT).replace(T_TEXT, EMPTY_TEXT)
				.replace(TWO_POINT_SEPARATOR, EMPTY_TEXT);
		timestamp = timestamp.substring(0, timestamp.indexOf(POINT));
		return timestamp;
	}

	/**
	 * Create a new scenario file to store commands.
	 *
	 * @param scenarioName
	 *            the scenario folder name.
	 * @throws IOException
	 *             if it fails to create the file.
	 */
	public static void createNewCommandFile(String scenarioName) throws IOException {

		if (scenarioName != null && !scenarioName.isEmpty()) {
			subfolder = scenarioName;
		} else {
			subfolder = getDefaultScenarioName();
		}
		File file = Paths.get(Menu.getInstance().getFolder(), subfolder, "scenario.steps").toFile(); //$NON-NLS-1$
		if (!file.exists() && !new File(file.getParent()).exists()) {
			new File(file.getParent()).mkdirs();
		}
		Files.createFile(file.toPath());
	}

	/**
	 * Subfolder to store the scenario, screenshots and report.
	 *
	 * @return the subfolder's name.
	 */
	public static String getSubfolder() {
		return subfolder;
	}

	/**
	 * Sets the subfolder to store the scenario, screenshots and report.
	 *
	 * @param subfolderName
	 *            the new subfolder's name.
	 */
	public static void setSubfolder(String subfolderName) {
		FileHelper.subfolder = subfolderName;
	}

}
