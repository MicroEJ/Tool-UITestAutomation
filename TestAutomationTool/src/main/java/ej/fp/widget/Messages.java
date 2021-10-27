/*
 * Java
 *
 * Copyright 2021 MicroEJ Corp. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break MicroEJ Corp. warranties on the whole library.
 */
package ej.fp.widget;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Handles the Test Automation Tool's ej.fp.widget package messages strings.
 */
/* package */ class Messages {
	private static final String BUNDLE_NAME = "widgetMessages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * Text for the title of the tool.
	 */
	/* package */ static final String RECORDER = "Menu.0"; //$NON-NLS-1$
	/**
	 * Message for when the playing has stopped.
	 */
	/* package */ static final String PLAYING_FINISHED = "Menu.1"; //$NON-NLS-1$
	/**
	 * Message for when the playing has started.
	 */
	/* package */ static final String PLAYING_STARTED = "Menu.2"; //$NON-NLS-1$
	/**
	 * Text for the scenarios list.
	 */
	/* package */ static final String SCENARIO_LIST = "Menu.3"; //$NON-NLS-1$
	/**
	 * Text for the actions report list.
	 */
	/* package */ static final String ACTION_REPORT = "Menu.4"; //$NON-NLS-1$
	/**
	 * Message for when the recording has stopped.
	 */
	/* package */ static final String RECORDING_STOPPED = "Menu.5"; //$NON-NLS-1$
	/**
	 * Message for when the recording has started
	 */
	/* package */ static final String RECORDING_STARTED = "Menu.6"; //$NON-NLS-1$
	/**
	 * Message for when the screenshot is taken.
	 */
	/* package */ static final String SCREENSHOT_TAKEN = "Menu.7"; //$NON-NLS-1$
	/**
	 * Message for when failing to load application icon.
	 */
	/* package */ static final String ICON_LOADING_FAILED = "Menu.8"; //$NON-NLS-1$
	/**
	 * Message for when failing to execute scenario file.
	 */
	/* package */ static final String SCENARIOS_EXECUTION_FAILED = "Menu.9"; //$NON-NLS-1$
	/**
	 * Message for when failing to save file.
	 */
	/* package */ static final String SAVE_FILE_ERROR = "Menu.10"; //$NON-NLS-1$
	/**
	 * Message for when failing to read the scenario file.
	 */
	/* package */ static final String READ_SCENARIO_FILE_ERROR = "Menu.11"; //$NON-NLS-1$

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
