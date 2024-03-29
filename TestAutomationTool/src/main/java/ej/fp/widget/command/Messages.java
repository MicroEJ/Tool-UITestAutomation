/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget.command;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the Test Automation Tool's ej.fp.widget.command package messages strings.
 */
/* package */ class Messages {
	private static final String BUNDLE_NAME = "commandMessages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * The main logger of this package.
	 */
	protected static final Logger LOGGER = Logger.getLogger(Messages.class.getName());

	/**
	 * Start of the message of screen test successful.
	 */
	/* package */ static final String SCREEN_TEST_START = "CompareScreenshotCommand.1"; //$NON-NLS-1$
	/**
	 * End of the message of screen test successful.
	 */
	/* package */ static final String SCREEN_TEST_SUCCESS_END = "CompareScreenshotCommand.2"; //$NON-NLS-1$
	/**
	 * End of the message of screen test failed.
	 */
	/* package */ static final String SCREEN_TEST_FAILED_END = "CompareScreenshotCommand.3"; //$NON-NLS-1$
	/**
	 * Message thrown when screen test fails.
	 */
	/* package */ static final String SCREEN_TEST_FAILED_MESSAGE = "CompareScreenshotCommand.4"; //$NON-NLS-1$
	/**
	 * Start of the message of screenshot file not found.
	 */
	/* package */ static final String SCREENSHOT_FILE_NOT_FOUND_FIRST_PART = "CompareScreenshotCommand.5"; //$NON-NLS-1$
	/**
	 * End of the message of screenshot file not found.
	 */
	/* package */ static final String SCREENSHOT_FILE_NOT_FOUND_SECOND_PART = "CompareScreenshotCommand.6"; //$NON-NLS-1$
	/**
	 * Message for the exception when the test fails.
	 */
	/* package */ static final String COMPARISON_FAILED_MESSAGE = "WrongScreenException.0"; //$NON-NLS-1$

	private Messages() {
	}

	/**
	 * Gets the message from the key.
	 *
	 * @param key
	 *            to the message saved.
	 * @return the message String.
	 */
	/* package */ static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			String msg = "MissingResourceException with key : " + key; //$NON-NLS-1$
			LOGGER.log(Level.WARNING, msg, e);
			return '!' + key + '!';
		}
	}
}
