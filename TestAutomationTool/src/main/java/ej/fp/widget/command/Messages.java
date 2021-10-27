/*
 * Java
 *
 * Copyright 2021 MicroEJ Corp. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break MicroEJ Corp. warranties on the whole library.
 */
package ej.fp.widget.command;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Handles the Test Automation Tool's ej.fp.widget.command package messages strings.
 */
/* package */ class Messages {
	private static final String BUNDLE_NAME = "commandMessages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

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
			return '!' + key + '!';
		}
	}
}
