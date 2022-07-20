/*
 * Java
 *
 * Copyright 2021-2022 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
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
