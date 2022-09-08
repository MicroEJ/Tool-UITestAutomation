/*
 * Java
 *
 * Copyright 2021-2022 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.library.test;

import com.is2t.hil.HIL;

import ej.fp.widget.Menu;
import ej.fp.widget.command.CompareScreenshotException;
import ej.fp.widget.util.FileHelper;

/**
 * A scenario is a recorded set of steps to reproduce a process that is described by those steps. {@code TestScenario}
 * is responsible for running such scenario and getting the report file.
 */
public class TestScenario {

	/** Buffer for SNI to send strings */
	public static char[] inputBuffer;

	/** Buffer for SNI to receive strings */
	public static char[] outputBuffer;

	/** Length of the strings written to the buffers, index 0 for the input and 1 for output */
	public static int[] buffersLength;

	private TestScenario() {

	}

	/**
	 * Initializes SNI buffers
	 *
	 * @param input
	 *            the input buffer
	 * @param output
	 *            the output buffer
	 * @param lengths
	 *            length of the strings currently in the buffers, index 0 for input and 1 for output
	 */
	public static void init(char[] input, char[] output, int[] lengths) {
		inputBuffer = input;
		outputBuffer = output;
		buffersLength = lengths;
	}

	/**
	 * Runs the scenario, this will generate a report file named {@code testTIMESTAMP.report}, the TIMESTAMP is the ID
	 * of the file and it is the time which the file was generated.
	 *
	 * @return <code>false</code> if screenshot comparison fails.
	 *
	 * @throws Exception
	 *             if the execution failed or couldn't be finished
	 */
	public static boolean fromFile() throws Exception {
		HIL.getInstance().refreshContent(inputBuffer);
		HIL.getInstance().refreshContent(buffersLength);
		final String foldername = new String(inputBuffer, 0, buffersLength[0]);
		try {
			Menu.getInstance().playFromFile(foldername);
		} catch (CompareScreenshotException e) {
			return false;
		} finally {
			final String output = FileHelper.getLastReportFilePath();
			output.getChars(0, output.length(), outputBuffer, 0);
			buffersLength[1] = output.length();
			HIL.getInstance().flushContent(outputBuffer);
			HIL.getInstance().flushContent(buffersLength);

		}
		return true;
	}
}
