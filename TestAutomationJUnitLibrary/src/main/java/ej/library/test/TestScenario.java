/*
 * Java
 *
 * Copyright 2021-2022 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.library.test;

import ej.bon.Immortals;

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

	static {

		Immortals.run(new Runnable() {

			@Override
			public void run() {
				inputBuffer = new char[32];
				outputBuffer = new char[256];
				buffersLength = new int[2];
			}
		});
		init(inputBuffer, outputBuffer, buffersLength);
	}

	/** if the test was successful */
	public final boolean success;

	/** path to the generated report */
	public final String reportPath;

	/** message with the cause of the failure in case success is false */
	public final String causeOfFailure;

	private TestScenario(boolean success, String reportPath, String causeOfFailure) {
		this.success = success;
		this.reportPath = reportPath;
		this.causeOfFailure = causeOfFailure;
	}

	/**
	 * Runs the scenario, this will generate a report file named {@code testTIMESTAMP.report}, the TIMESTAMP is the ID
	 * of the file and it is the time which the file was generated.
	 *
	 * @param filename
	 *            name of the scenario file
	 * @return a TestScenario object containing information about the execution
	 * @throws Exception
	 *             if the execution failed or couldn't be finished
	 */
	public static TestScenario fromFile(String filename) throws Exception {
		boolean success = false;
		String causeOfFailure = null;
		try {
			filename.getChars(0, filename.length(), inputBuffer, 0);
			buffersLength[0] = filename.length();
			fromFile();
			success = true;
		} catch (Exception e) {
			Throwable cause = e.getCause();
			if (cause != null) {
				throw (Exception) cause;
			}
			causeOfFailure = e.getMessage();
		}
		return new TestScenario(success, new String(outputBuffer, 0, buffersLength[1]), causeOfFailure);
	}

	/**
	 * Internal function to run the scenario.
	 *
	 * @throws Exception
	 *             if an exception happens.
	 */
	public static native void fromFile() throws Exception;

	/**
	 * Initializes the communications channel with the mock.
	 *
	 * @param inputText
	 *            input array to be shared with the mock.
	 * @param outputText
	 *            output array to be shared with the mock.
	 * @param buffersLength
	 *            length of the strings currently in the buffers, index 0 for input and 1 for output
	 */
	public static native void init(char[] inputText, char[] outputText, int[] buffersLength);
}
