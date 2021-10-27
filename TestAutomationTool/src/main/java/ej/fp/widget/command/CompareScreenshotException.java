/*
 * Java
 *
 * Copyright 2021 MicroEJ Corp. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break MicroEJ Corp. warranties on the whole library.
 */
package ej.fp.widget.command;

/**
 * Exception to be thrown when a screenshot comparison fails.
 */
public class CompareScreenshotException extends CommandExecutionException {

	private static final long serialVersionUID = -6815774418249649200L;

	/**
	 * Thrown when {@code ScreenshotComparator.compare} returns false.
	 *
	 * @param screenTested
	 *            the screenshot's filename
	 */
	public CompareScreenshotException(String screenTested) {
		super(Messages.getString(Messages.SCREEN_TEST_FAILED_MESSAGE) + screenTested);
	}
}
