/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget.command;

/**
 * Exception to be thrown when a command fails to be executed.
 */
public class CommandExecutionException extends Exception {

	private static final long serialVersionUID = -6815774418249649200L;

	/**
	 * Protected constructor to allow subclasses to be created only with a message.
	 *
	 * @param message
	 *            the detail message.
	 */
	protected CommandExecutionException(String message) {
		super(message);
	}

	/**
	 * Creates a new exception with the given message and cause.
	 *
	 * @param message
	 *            the detail message.
	 * @param cause
	 *            the cause of the exception.
	 */
	public CommandExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new exception with the given cause.
	 *
	 * @param cause
	 *            the cause of the exception.
	 */
	public CommandExecutionException(Throwable cause) {
		super(cause);
	}
}
