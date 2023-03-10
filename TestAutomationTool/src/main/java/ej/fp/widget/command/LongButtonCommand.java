/*
 * Java
 *
 * Copyright 2022-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import ej.fp.widget.Menu;
import ej.fp.widget.recorder.RecorderLongButton;

/**
 * Represents a button press command.
 */
public abstract class LongButtonCommand extends Command<RecorderLongButton> {

	/**
	 * Id of the recorded widget.
	 */
	protected String label;

	@Override
	String serialize() {
		return this.label;
	}

	@Override
	void parse(String... args) {
		this.label = args[1];
	}

	@Override
	protected Class<RecorderLongButton> getMyType() {
		return RecorderLongButton.class;
	}

	/**
	 * Creates a button command with the specified label (id).
	 *
	 * @param <T>
	 *            type which extends ButtonCommand.
	 * @param clazz
	 *            actual type of the command to be created.
	 * @param label
	 *            widget's id.
	 * @return the command
	 */
	public static <T extends LongButtonCommand> T build(Class<T> clazz, String label) {
		T command = null;
		try {
			command = clazz.newInstance();
			command.label = label;
		} catch (InstantiationException | IllegalAccessException e) {
			Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage(), e);
			Menu.getInstance().toggleRecordState(null);
		}
		return command;
	}

	/**
	 * Represents a button press command.
	 */
	public static class Press extends LongButtonCommand {

		@Override
		public void execute() {
			getWidget(this.label).getRecorderListener().press();
		}
	}

	/**
	 * Represents a button release command.
	 */
	public static class Release extends LongButtonCommand {

		@Override
		public void execute() {
			getWidget(this.label).getRecorderListener().release();
		}

	}

}
