/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget.command;

/**
 * Base interface for recording widgets.
 *
 * @param <T>
 *            type that implements Command.Listener.
 */
public interface CommandRecorder<T extends Command.Listener> {

	/**
	 * Retrieves the listener for this recorder.
	 *
	 * @return the listener
	 */
	T getRecorderListener();
}
