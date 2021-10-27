/*
 * Java
 *
 * Copyright 2021 MicroEJ Corp. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break MicroEJ Corp. warranties on the whole library.
 */
package ej.fp.widget.command;

import ej.fp.Device;
import ej.fp.Widget;

/**
 * Command that represents a user interaction
 *
 * @param <T>
 *            a widget extension
 */
public abstract class Command<T extends Widget> {

	/**
	 * Base type for listeners executable by Command.
	 */
	public static interface Listener {
		// Just for this type information
	}

	/**
	 * Parses commands generated by RecorderButton or RecorderPointer.
	 *
	 * @param args
	 *            commands.
	 */
	/* package */ void parse(String... args) {
		// Default implementation does nothing
	}

	/**
	 * Serializes the command to be stored e.g. a press command at x: 30 and y: 30 would be serialized to press,30,30.
	 *
	 * @return the serialized command.
	 */
	/* package */ abstract String serialize();

	/**
	 * Executes the command on the {@code PointerListener} passed.
	 *
	 * @throws CommandExecutionException
	 *             when executing {@code CompareScreenshotCommand} and it fails the test.
	 */
	public abstract void execute() throws CommandExecutionException;

	/**
	 * Returns the actual type of T
	 *
	 * @return actual class of type T
	 */
	protected abstract Class<T> getMyType();

	/**
	 * Retrieves a widget of type T and label if specified, or the first widget of type T if label is null.
	 *
	 * @param label
	 *            widget identifier
	 * @return the expected widget or null if it could not be found
	 */
	protected T getWidget(String label) {
		return Device.getDevice().getWidget(getMyType(), label);
	}
}