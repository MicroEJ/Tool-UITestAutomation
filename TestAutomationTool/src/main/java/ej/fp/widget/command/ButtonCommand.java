/*
 * Java
 *
 * Copyright 2021 MicroEJ Corp. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break MicroEJ Corp. warranties on the whole library.
 */
package ej.fp.widget.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import ej.fp.widget.Menu;
import ej.fp.widget.recorder.RecorderButton;

/**
 * Represents a button press command.
 */
public abstract class ButtonCommand extends Command<RecorderButton> {

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
	protected Class<RecorderButton> getMyType() {
		return RecorderButton.class;
	}

	/**
	 * Creates a button command with the specified label (id).
	 *
	 * @param <T>
	 *            type which extends ButtonCommand
	 * @param clazz
	 *            actual type of the command to be created
	 * @param label
	 *            widget's id
	 * @return the command
	 */
	public static <T extends ButtonCommand> T build(Class<T> clazz, String label) {
		T command = null;
		try {
			command = clazz.newInstance();
			command.label = label;
		} catch (InstantiationException | IllegalAccessException e) {
			Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage(), e);
			Menu.getInstance().toggleRecordState();
		}
		return command;
	}

	/**
	 * Represents a button press command.
	 */
	public static class Press extends ButtonCommand {

		@Override
		public void execute() {
			getWidget(this.label).getRecorderListener().press();
		}
	}

	/**
	 * Represents a button release command.
	 */
	public static class Release extends ButtonCommand {

		@Override
		public void execute() {
			getWidget(this.label).getRecorderListener().release();
		}

	}

}
