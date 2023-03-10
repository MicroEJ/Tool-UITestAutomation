/*
 * Java
 *
 * Copyright 2022-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import ej.fp.widget.Menu;
import ej.fp.widget.recorder.RecorderJoystick;

/**
 * Represents a joystick press or release command.
 */
public abstract class JoystickCommand extends Command<RecorderJoystick> {

	private static final String COMMA = ","; //$NON-NLS-1$
	private static final int COMMAND_X = 2;
	private static final int COMMAND_Y = 3;

	/**
	 * Id of the recorded widget.
	 */
	protected String label;

	/**
	 * the x coordinate relative to joystick recorded button position.
	 */
	protected int x;

	/**
	 * the y coordinate relative to joystick recorded button position.
	 */
	protected int y;

	@Override
	String serialize() {
		return this.label + COMMA + this.x + COMMA + this.y;
	}

	@Override
	void parse(String... args) {
		this.label = args[1];
		this.x = Integer.parseInt(args[COMMAND_X]);
		this.y = Integer.parseInt(args[COMMAND_Y]);
	}

	@Override
	protected Class<RecorderJoystick> getMyType() {
		return RecorderJoystick.class;
	}

	/**
	 * Creates a joystick button command with the specified label (id) with x and y.
	 *
	 * @param <T>
	 *            type which extends ButtonCommand.
	 * @param clazz
	 *            actual type of the command to be created.
	 * @param label
	 *            widget's id.
	 * @param x
	 *            the x coordinate relative to joystick button position.
	 * @param y
	 *            the y coordinate relative to joystick button position.
	 * @return the command
	 */
	public static <T extends JoystickCommand> T build(Class<T> clazz, String label, int x, int y) {
		T command = null;
		try {
			command = clazz.newInstance();
			command.label = label;
			command.x = x;
			command.y = y;
		} catch (InstantiationException | IllegalAccessException e) {
			Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage(), e);
			Menu.getInstance().toggleRecordState(null);
		}
		return command;
	}

	/**
	 * Represents a joystick up press command.
	 */
	public static class UpPress extends JoystickCommand {

		@Override
		public void execute() {
			getWidget(this.label).getRecorderListener().pressUp(this.x, this.y);
		}
	}

	/**
	 * Represents a joystick up release command.
	 */
	public static class UpRelease extends JoystickCommand {

		@Override
		public void execute() {
			getWidget(this.label).getRecorderListener().releaseUp(this.x, this.y);
		}
	}

	/**
	 * Represents a joystick down press command.
	 */
	public static class DownPress extends JoystickCommand {

		@Override
		public void execute() {
			getWidget(this.label).getRecorderListener().pressDown(this.x, this.y);
		}
	}

	/**
	 * Represents a joystick down release command.
	 */
	public static class DownRelease extends JoystickCommand {

		@Override
		public void execute() {
			getWidget(this.label).getRecorderListener().releaseDown(this.x, this.y);
		}
	}

	/**
	 * Represents a joystick left press command.
	 */
	public static class LeftPress extends JoystickCommand {

		@Override
		public void execute() {
			getWidget(this.label).getRecorderListener().pressLeft(this.x, this.y);
		}
	}

	/**
	 * Represents a joystick left release command.
	 */
	public static class LeftRelease extends JoystickCommand {

		@Override
		public void execute() {
			getWidget(this.label).getRecorderListener().releaseLeft(this.x, this.y);
		}
	}

	/**
	 * Represents a joystick right press command.
	 */
	public static class RightPress extends JoystickCommand {

		@Override
		public void execute() {
			getWidget(this.label).getRecorderListener().pressRight(this.x, this.y);
		}
	}

	/**
	 * Represents a joystick right release command.
	 */
	public static class RightRelease extends JoystickCommand {

		@Override
		public void execute() {
			getWidget(this.label).getRecorderListener().releaseRight(this.x, this.y);
		}
	}

	/**
	 * Represents a joystick enter press command.
	 */
	public static class EnterPress extends JoystickCommand {

		@Override
		public void execute() {
			getWidget(this.label).getRecorderListener().pressEnter(this.x, this.y);
		}
	}

	/**
	 * Represents a joystick enter release command.
	 */
	public static class EnterRelease extends JoystickCommand {

		@Override
		public void execute() {
			getWidget(this.label).getRecorderListener().releaseEnter(this.x, this.y);
		}
	}

}
