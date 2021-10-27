/*
 * Java
 *
 * Copyright 2021 MicroEJ Corp. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break MicroEJ Corp. warranties on the whole library.
 */
package ej.fp.widget.command;

/**
 * Base interface for recording widgets
 *
 * @param <T>
 *            type that implements Command.Listener
 */
public interface CommandRecorder<T extends Command.Listener> {

	/**
	 * Retrieves the listener for this recorder.
	 *
	 * @return the listener
	 */
	T getRecorderListener();
}
