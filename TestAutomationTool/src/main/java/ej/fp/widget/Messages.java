/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the Test Automation Tool's ej.fp.widget package messages strings.
 */
/* package */ class Messages {
	private static final String BUNDLE_NAME = "widgetMessages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * The main logger of this package.
	 */
	protected static final Logger LOGGER = Logger.getLogger(Messages.class.getName());

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
	/**
	 * Text for the browse button.
	 */
	/* package */ static final String BROWSE = "Menu.12"; //$NON-NLS-1$
	/**
	 * Text for scenario path label.
	 */
	/* package */ static final String SCENARIO_PATH = "Menu.13"; //$NON-NLS-1$
	/**
	 * Text for mask edit label.
	 */
	/* package */ static final String MASK_EDIT = "Menu.14"; //$NON-NLS-1$
	/**
	 * Text for enable mask label.
	 */
	/* package */ static final String ENABLE_MASK = "Menu.15"; //$NON-NLS-1$
	/**
	 * Text for x coordinate mask label.
	 */
	/* package */ static final String MASK_X = "Menu.16"; //$NON-NLS-1$
	/**
	 * Text for y coordinate mask label.
	 */
	/* package */ static final String MASK_Y = "Menu.17"; //$NON-NLS-1$
	/**
	 * Text for mask width label.
	 */
	/* package */ static final String MASK_WIDTH = "Menu.18"; //$NON-NLS-1$
	/**
	 * Text for mask height label.
	 */
	/* package */ static final String MASK_HEIGHT = "Menu.19"; //$NON-NLS-1$
	/**
	 * Message for when the update has stopped.
	 */
	/* package */ static final String UPDATE_FINISHED = "Menu.20"; //$NON-NLS-1$
	/**
	 * Message for when the update has started.
	 */
	/* package */ static final String UPDATE_STARTED = "Menu.21"; //$NON-NLS-1$
	/**
	 * Text for compare option label.
	 */
	/* package */ static final String COMPARE_OPTION = "Menu.22"; //$NON-NLS-1$
	/**
	 * Text for show compare screenshots label.
	 */
	/* package */ static final String SHOW_COMPARE_SCREENSHOTS = "Menu.23"; //$NON-NLS-1$
	/**
	 * Text for compare mask label.
	 */
	/* package */ static final String COMPARE_MASK = "Menu.24"; //$NON-NLS-1$
	/**
	 * Text for current screenshot label.
	 */
	/* package */ static final String CURRENT_SCREENSHOT = "Menu.25"; //$NON-NLS-1$
	/**
	 * Text for current screenshot label.
	 */
	/* package */ static final String IMAGE_TO_COMPARE = "Menu.26"; //$NON-NLS-1$
	/**
	 * Text for current screenshot label.
	 */
	/* package */ static final String SHOW_MASK = "Menu.27"; //$NON-NLS-1$
	/**
	 * Text for reload image button.
	 */
	/* package */ static final String RELOAD_IMAGE = "Menu.28"; //$NON-NLS-1$
	/**
	 * Message for when the playing has pause.
	 */
	/* package */ static final String PAUSE_TIME = "Menu.29"; //$NON-NLS-1$
	/**
	 * Message for when the playing has resume.
	 */
	/* package */ static final String RESUME_TIME = "Menu.30"; //$NON-NLS-1$
	/**
	 * Text for the rename button.
	 */
	/* package */ static final String RENAME = "Menu.31"; //$NON-NLS-1$

	/**
	 * Text for Settings JPanel.
	 */
	/* package */ static final String SETTINGS = "Menu.32"; //$NON-NLS-1$
	/**
	 * /** Text for Mask JPanel.
	 */
	/* package */ static final String MASK = "Menu.33"; //$NON-NLS-1$
	/**
	 * /** Text for Screenshot comparison JPanel.
	 */
	/* package */ static final String SCREENSHOT_COMPARAISON = "Menu.34"; //$NON-NLS-1$
	/**
	 * Message for when failing there is a compare screenshot exception.
	 */
	/* package */ static final String COMPARE_SCREENSHOT_EXCEPTION = "Menu.35"; //$NON-NLS-1$
	/**
	 * Message for when failing there is a compare screenshot exception.
	 */
	/* package */ static final String COLORS = "Menu.36"; //$NON-NLS-1$

	/**
	 * /** Text for mask list label.
	 */
	/* package */ static final String MASK_LIST = "Menu.37"; //$NON-NLS-1$
	/**
	 * Text for the delete button.
	 */
	/* package */ static final String DELETE = "Menu.38"; //$NON-NLS-1$
	/**
	 * Text for the add new button.
	 */
	/* package */ static final String ADD_NEW = "Menu.39"; //$NON-NLS-1$
	/**
	 * Text for the rename button.
	 */
	/* package */ static final String RENAME_BUTTON = "Menu.40"; //$NON-NLS-1$
	/**
	 * Text for Show all mask label.
	 */
	/* package */ static final String SHOW_ALL_MASKS = "Menu.41"; //$NON-NLS-1$

	/**
	 * Creates messages.
	 */
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