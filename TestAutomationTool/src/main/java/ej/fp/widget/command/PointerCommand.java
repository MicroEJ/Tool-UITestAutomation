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
import ej.fp.widget.recorder.RecorderPointer;

/**
 * Base class for pointer related commands
 */
public abstract class PointerCommand extends Command<RecorderPointer> {

	/**
	 * coordinate x.
	 */
	protected int x;

	/**
	 * coordinate y.
	 */
	protected int y;

	@Override
	public void parse(String... args) {
		this.x = Integer.parseInt(args[1]);
		this.y = Integer.parseInt(args[2]);
	}

	@Override
	/* package */ String serialize() {
		return new StringBuilder().append(this.x).append(',').append(this.y).toString();
	}

	/**
	 * Creates a pointer command with the x an y specified.
	 *
	 * @param <T>
	 *            type which extends PointerCommand
	 * @param clazz
	 *            actual type of the command to be created
	 *
	 * @param x2
	 *            x of the new command.
	 * @param y2
	 *            y of the new command.
	 * @return the command.
	 */
	public static <T extends PointerCommand> T with(Class<T> clazz, int x2, int y2) {
		T command = null;
		try {
			command = clazz.newInstance();
			command.x = x2;
			command.y = y2;
		} catch (InstantiationException | IllegalAccessException e) {
			Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage(), e);
			Menu.getInstance().toggleRecordState();
		}
		return command;
	}

	@Override
	protected Class<RecorderPointer> getMyType() {
		return RecorderPointer.class;
	}

	/**
	 * Represents a pointer press command.
	 */
	public static class Press extends PointerCommand {

		@Override
		public void execute() {
			getWidget(null).getRecorderListener().press(getWidget(null), this.x, this.y, null);
		}

	}

	/**
	 * Represents a pointer move command.
	 */
	public static class Move extends PointerCommand {

		@Override
		public void execute() {
			getWidget(null).getRecorderListener().move(getWidget(null), this.x, this.y);
		}

	}

	/**
	 * Represents a pointer release command.
	 */
	public static class Release extends PointerCommand {

		@Override
		public void execute() {
			getWidget(null).getRecorderListener().release(getWidget(null), null);
		}

	}

}
