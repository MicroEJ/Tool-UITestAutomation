/*
 * Java
 *
 * Copyright 2021 MicroEJ Corp. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break MicroEJ Corp. warranties on the whole library.
 */
package ej.fp.widget.command;

/**
 * Exception to be thrown when a command fails to be executed.
 */
public class CommandExecutionException extends Exception {

	private static final long serialVersionUID = -6815774418249649200L;

	/**
	 * Protected constructor to allow subclasses to be created only with a message
	 *
	 * @param message
	 *            the detail message.
	 */
	protected CommandExecutionException(String message) {
		super(message);
	}

	/**
	 * Creates a new exception with the given message and cause
	 *
	 * @param message
	 *            the detail message.
	 * @param cause
	 *            the cause of the exception
	 */
	public CommandExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new exception with the given cause
	 *
	 * @param cause
	 *            the cause of the exception
	 */
	public CommandExecutionException(Throwable cause) {
		super(cause);
	}
}
