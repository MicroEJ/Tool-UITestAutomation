/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget;

import javax.swing.text.JTextComponent;

/**
 * Log past activities made by the user when recording.
 */
public class ActivityLogger {
	private static final int ACTIVITIES_LENGTH = 6;

	private final String[] pastActivities = new String[ACTIVITIES_LENGTH];
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
		this.pastActivities[Math.abs(this.pointer % this.pastActivities.length)] = activity;
		this.pointer = this.pointer + 1;
		StringBuilder activitiesTextBuilder = new StringBuilder();
		for (int i = 0; i < this.pastActivities.length; i++) {
			activitiesTextBuilder.append(this.pastActivities[Math.abs((i + this.pointer) % this.pastActivities.length)]
					+ System.lineSeparator());
		}
		this.jTextPane.setText(activitiesTextBuilder.toString());
	}

}
