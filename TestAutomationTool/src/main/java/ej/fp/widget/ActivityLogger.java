/*
 * Java
 *
 * Copyright 2021 MicroEJ Corp. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break MicroEJ Corp. warranties on the whole library.
 */
package ej.fp.widget;

import javax.swing.text.JTextComponent;

/**
 * Log past activities made by the user when recording.
 */
public class ActivityLogger {
	private final String[] pastActivities = new String[6];
	private final JTextComponent jTextPane;

	private int pointer;

	/**
	 * Creates the {@code ActivityLogger} to log activities and update on a {@code JTextComponent}.
	 *
	 * @param jTextPane
	 *            component to be updated with the activities.
	 *
	 */
	public ActivityLogger(JTextComponent jTextPane) {
		for (int i = 0; i < this.pastActivities.length; i++) {
			this.pastActivities[i] = ""; //$NON-NLS-1$
		}
		this.jTextPane = jTextPane;
	}

	/**
	 * Pushes a String to the circular queue and sets the text of the activities list.
	 *
	 * @param activity
	 *            latest activity to be pushed.
	 *
	 */
	public void push(String activity) {
		this.pastActivities[Math.abs(this.pointer++ % this.pastActivities.length)] = activity;
		StringBuilder activitiesTextBuilder = new StringBuilder();
		for (int i = 0; i < this.pastActivities.length; i++) {
			activitiesTextBuilder.append(this.pastActivities[Math.abs((i + this.pointer) % this.pastActivities.length)]
					+ System.lineSeparator());
		}
		this.jTextPane.setText(activitiesTextBuilder.toString());
	}
}
