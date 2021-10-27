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

import ej.fp.Widget;

/**
 * Represents a wait/delay command with the interval the user took between interactions
 */
public class WaitCommand extends Command<Widget> {

	private static long lastEventTimestamp;
	private long millis;

	/**
	 * Creates a WaitCommand with the last event time.
	 */
	public WaitCommand() {
		this.millis = updateLastEventTime();
	}

	@Override
	public void parse(String... args) {
		this.millis = Long.parseLong(args[1]);
	}

	/**
	 * Updates the last event time.
	 *
	 * @return the last event time.
	 */
	public static long updateLastEventTime() {
		long currentTimeMillis = System.currentTimeMillis();
		long lastEventTime = currentTimeMillis - lastEventTimestamp;
		lastEventTimestamp = currentTimeMillis;
		return lastEventTime;
	}

	@Override
	public void execute() {
		try {
			Thread.sleep(this.millis);
		} catch (InterruptedException e) {
			Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage(), e);
			Thread.currentThread().interrupt();
		}
	}

	@Override
	/* package */ String serialize() {
		return Long.toString(this.millis);
	}

	@Override
	protected Class<Widget> getMyType() {
		return Widget.class;
	}

}
