/*
 * Java
 *
 * Copyright 2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget;

/**
 * Provides constants for actions performed.
 */
public class ActionState {
	/**
	 * Constant given when no action is performed.
	 */
	public static final int IDLE = 0;
	/**
	 * Constant for updating action.
	 */
	public static final int UPDATE = 1;
	/**
	 * Constant for playing action.
	 */
	public static final int PLAY = 2;
	/**
	 * Constant for recording action.
	 */
	public static final int RECORD = 3;

	// Prevents initialization.
	private ActionState() {
	}
}
