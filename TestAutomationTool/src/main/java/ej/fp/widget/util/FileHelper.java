/*
 * Java
 *
 * Copyright 2021 MicroEJ Corp. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break MicroEJ Corp. warranties on the whole library.
 */
package ej.fp.widget.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import ej.fp.widget.Menu;

/**
 * Handles creation of files and stores the last file.
 */
public class FileHelper {

	private static String lastReportFileID = ""; //$NON-NLS-1$
	private static String lastCommandFileID = ""; //$NON-NLS-1$
	private static String subfolder = ""; //$NON-NLS-1$
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
		timestamp = timestamp.replace("-", "").replace("T", "").replace(":", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		timestamp = timestamp.substring(0, timestamp.indexOf(".")); //$NON-NLS-1$
		File file = Paths.get(Menu.getInstance().getFolder(), subfolder, "test" + timestamp + ".report").toFile(); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			if (!file.exists() && !new File(file.getParent()).exists()) {
				new File(file.getParent()).mkdirs();
			}
			Files.createFile(file.toPath());
			lastReportFilePath = file.getAbsolutePath();
			lastReportFileID = timestamp;
		} catch (IOException e) {
			Logger.getAnonymousLogger().warning(e.getMessage());
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
	 * Create a new scenario file to store commands.
	 *
	 * @throws IOException
	 *             if it fails to create the file.
	 */
	public static void createNewCommandFile() throws IOException {
		String timestamp = LocalDateTime.now().toString();
		timestamp = timestamp.replace("-", "").replace("T", "").replace(":", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		timestamp = timestamp.substring(0, timestamp.indexOf(".")); //$NON-NLS-1$
		subfolder = timestamp;
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
