/*
 * Java
 *
 * Copyright 2022-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget.recorder;

import ej.fp.Widget.WidgetAttribute;
import ej.fp.Widget.WidgetDescription;
import ej.fp.widget.Joystick;
import ej.fp.widget.Menu;
import ej.fp.widget.command.Command.Listener;
import ej.fp.widget.command.CommandRecorder;
import ej.fp.widget.command.JoystickCommand;

/**
 * Joystick events recorder.
 */
@WidgetDescription(attributes = { @WidgetAttribute(name = "label", isOptional = true), @WidgetAttribute(name = "x"),
		@WidgetAttribute(name = "y"), @WidgetAttribute(name = "skin"),
		@WidgetAttribute(name = "filter", isOptional = true), @WidgetAttribute(name = "upSkin"),
		@WidgetAttribute(name = "downSkin"), @WidgetAttribute(name = "leftSkin"), @WidgetAttribute(name = "rightSkin"),
		@WidgetAttribute(name = "enterSkin", isOptional = true),
		@WidgetAttribute(name = "disableEnter", isOptional = true),
		@WidgetAttribute(name = "repeatPeriod", isOptional = true),
		@WidgetAttribute(name = "listenerClass", isOptional = true) })
public class RecorderJoystick extends Joystick implements CommandRecorder<RecorderJoystick.JoystickListener> {

	private static final int NO_MOUSE_DIRECTION = -1;
	private static final int MOUSE_LEFT = 1;
	private static final int MOUSE_RIGHT = 2;
	private static final int MOUSE_UP = 3;
	private static final int MOUSE_DOWN = 4;
	private JoystickListener listener;

	@Override
	public void mousePressed(int x, int y, MouseButton button) {
		int buttonPressedId = getButtonId(x, y, button);

		switch (buttonPressedId) {
		case JoystickButtonIds.UP:
			Menu.getInstance().saveCommand(JoystickCommand.build(JoystickCommand.UpPress.class, this.getLabel(), x, y));
			break;
		case JoystickButtonIds.RIGHT:
			Menu.getInstance()
					.saveCommand(JoystickCommand.build(JoystickCommand.RightPress.class, this.getLabel(), x, y));
			break;
		case JoystickButtonIds.DOWN:
			Menu.getInstance()
					.saveCommand(JoystickCommand.build(JoystickCommand.DownPress.class, this.getLabel(), x, y));
			break;
		case JoystickButtonIds.LEFT:
			Menu.getInstance()
					.saveCommand(JoystickCommand.build(JoystickCommand.LeftPress.class, this.getLabel(), x, y));
			break;
		case JoystickButtonIds.OK:
			Menu.getInstance()
					.saveCommand(JoystickCommand.build(JoystickCommand.EnterPress.class, this.getLabel(), x, y));
			break;

		default:
			break;
		}
		super.mousePressed(x, y, button);
	}

	@Override
	public void mouseReleased(int x, int y, MouseButton button) {
		int buttonReleasedId = getButtonId(x, y, button);

		switch (buttonReleasedId) {
		case JoystickButtonIds.UP:
			Menu.getInstance()
					.saveCommand(JoystickCommand.build(JoystickCommand.UpRelease.class, this.getLabel(), x, y));
			break;
		case JoystickButtonIds.RIGHT:
			Menu.getInstance()
					.saveCommand(JoystickCommand.build(JoystickCommand.RightRelease.class, this.getLabel(), x, y));
			break;
		case JoystickButtonIds.DOWN:
			Menu.getInstance()
					.saveCommand(JoystickCommand.build(JoystickCommand.DownRelease.class, this.getLabel(), x, y));
			break;
		case JoystickButtonIds.LEFT:
			Menu.getInstance()
					.saveCommand(JoystickCommand.build(JoystickCommand.LeftRelease.class, this.getLabel(), x, y));
			break;
		case JoystickButtonIds.OK:
			Menu.getInstance()
					.saveCommand(JoystickCommand.build(JoystickCommand.EnterRelease.class, this.getLabel(), x, y));
			break;

		default:
			break;
		}

		super.mouseReleased(x, y, button);
	}

	private int getButtonId(int x, int y, MouseButton button) {
		if (button == MouseButton.FIRST_BUTTON) {
			int relativeX = (getWidth() / 2) - x;
			int relativeY = (getHeight() / 2) - y;
			// get mouse direction
			boolean mouseEquals = relativeX == relativeY || relativeX == -relativeY;
			if (!mouseEquals) {
				return getButtonIdByMouseDirection(relativeX, relativeY);
			}
		} else if (button == MouseButton.THIRD_BUTTON) {
			return JoystickButtonIds.OK;
		}
		return 0;
	}

	private int getButtonIdByMouseDirection(int relativeX, int relativeY) {

		if (getMouseHorizontalDirection(relativeX, relativeY) == MOUSE_LEFT) {
			return JoystickButtonIds.LEFT;
		} else if (getMouseHorizontalDirection(relativeX, relativeY) == MOUSE_RIGHT) {
			return JoystickButtonIds.RIGHT;
		} else if (getMouseverticalDirection(relativeX, relativeY) == MOUSE_UP) {
			return JoystickButtonIds.UP;
		} else if (getMouseverticalDirection(relativeX, relativeY) == MOUSE_DOWN) {
			return JoystickButtonIds.DOWN;
		}
		return 0;
	}

	private int getMouseHorizontalDirection(int relativeX, int relativeY) {
		// get left or right mouse direction.
		boolean mouseRight = relativeX < 0 && relativeY < -relativeX && relativeY > relativeX;
		boolean mouseLeft = relativeX > 0 && relativeY < relativeX && relativeY > -relativeX;
		if (mouseRight) {
			return MOUSE_LEFT;
		} else if (mouseLeft) {
			return MOUSE_RIGHT;
		}
		return NO_MOUSE_DIRECTION;
	}

	private int getMouseverticalDirection(int relativeX, int relativeY) {
		// get up or down mouse direction.
		boolean mouseUp = relativeY > 0 && relativeX < relativeY && relativeX > -relativeY;
		boolean mouseDown = relativeY < 0 && relativeX < -relativeY && relativeX > relativeY;

		if (mouseUp) {
			return MOUSE_UP;
		} else if (mouseDown) {
			return MOUSE_DOWN;
		}
		return NO_MOUSE_DIRECTION;
	}

	/**
	 * Joystick up button pressed.
	 *
	 * @param x
	 *            the x coordinate relative to joystick up button position.
	 * @param y
	 *            the y coordinate relative to joystick up button position.
	 */
	public void upPressed(int x, int y) {
		mousePressed(x, y, MouseButton.FIRST_BUTTON);
	}

	/**
	 * Joystick up button released.
	 *
	 * @param x
	 *            the x coordinate relative to joystick up button position.
	 * @param y
	 *            the y coordinate relative to joystick up button position.
	 */
	public void upReleased(int x, int y) {
		mouseReleased(x, y, MouseButton.FIRST_BUTTON);
	}

	/**
	 * Joystick down button pressed.
	 *
	 * @param x
	 *            the x coordinate relative to joystick down button position.
	 * @param y
	 *            the y coordinate relative to joystick down button position.
	 */
	public void downPressed(int x, int y) {
		mousePressed(x, y, MouseButton.FIRST_BUTTON);
	}

	/**
	 * Joystick down button released.
	 *
	 * @param x
	 *            the x coordinate relative to joystick down button position.
	 * @param y
	 *            the y coordinate relative to joystick down button position.
	 */
	public void downReleased(int x, int y) {
		mouseReleased(x, y, MouseButton.FIRST_BUTTON);
	}

	/**
	 * Joystick left button pressed.
	 *
	 * @param x
	 *            the x coordinate relative to joystick left button position.
	 * @param y
	 *            the y coordinate relative to joystick left button position.
	 */
	public void leftPressed(int x, int y) {
		mousePressed(x, y, MouseButton.FIRST_BUTTON);
	}

	/**
	 * Joystick left button released.
	 *
	 * @param x
	 *            the x coordinate relative to joystick left button position.
	 * @param y
	 *            the y coordinate relative to joystick left button position.
	 */
	public void leftReleased(int x, int y) {
		mouseReleased(x, y, MouseButton.FIRST_BUTTON);
	}

	/**
	 * Joystick right button pressed.
	 *
	 * @param x
	 *            the x coordinate relative to joystick right button position.
	 * @param y
	 *            the y coordinate relative to joystick right button position.
	 */
	public void rightPressed(int x, int y) {
		mousePressed(x, y, MouseButton.FIRST_BUTTON);
	}

	/**
	 * Joystick right button released.
	 *
	 * @param x
	 *            the x coordinate relative to joystick right button position.
	 * @param y
	 *            the y coordinate relative to joystick right button position.
	 */
	public void rightReleased(int x, int y) {
		mouseReleased(x, y, MouseButton.FIRST_BUTTON);
	}

	/**
	 * Joystick enter button pressed.
	 *
	 * @param x
	 *            the x coordinate relative to joystick enter button position.
	 * @param y
	 *            the y coordinate relative to joystick enter button position.
	 */
	public void enterPressed(int x, int y) {
		mousePressed(x, y, MouseButton.THIRD_BUTTON);
	}

	/**
	 * Joystick enter button released.
	 *
	 * @param x
	 *            the x coordinate relative to joystick enter button position.
	 * @param y
	 *            the y coordinate relative to joystick enter button position.
	 */
	public void enterReleased(int x, int y) {
		mouseReleased(x, y, MouseButton.THIRD_BUTTON);
	}

	/**
	 * Joystick recorder listener.
	 */
	public static class JoystickListener implements Listener {
		RecorderJoystick joystick;

		JoystickListener(RecorderJoystick joystick) {
			this.joystick = joystick;
		}

		/**
		 * Executes a joystick up press.
		 *
		 * @param x
		 *            the x coordinate relative to joystick up button position.
		 * @param y
		 *            the y coordinate relative to joystick up button position.
		 */
		public void pressUp(int x, int y) {
			this.joystick.upPressed(x, y);
		}

		/**
		 * Executes a joystick down press.
		 *
		 * @param x
		 *            the x coordinate relative to joystick down button position.
		 * @param y
		 *            the y coordinate relative to joystick down button position.
		 */
		public void pressDown(int x, int y) {
			this.joystick.downPressed(x, y);
		}

		/**
		 * Executes a joystick left press.
		 *
		 * @param x
		 *            the x coordinate relative to joystick left button position.
		 * @param y
		 *            the y coordinate relative to joystick left button position.
		 */
		public void pressLeft(int x, int y) {
			this.joystick.leftPressed(x, y);
		}

		/**
		 * Executes a joystick right press.
		 *
		 * @param x
		 *            the x coordinate relative to joystick right button position.
		 * @param y
		 *            the y coordinate relative to joystick right button position.
		 */
		public void pressRight(int x, int y) {
			this.joystick.rightPressed(x, y);
		}

		/**
		 * Executes a joystick enter press.
		 *
		 * @param x
		 *            the x coordinate relative to joystick enter button position.
		 * @param y
		 *            the y coordinate relative to joystick enter button position.
		 */
		public void pressEnter(int x, int y) {
			this.joystick.enterPressed(x, y);
		}

		/**
		 * Executes a joystick up release.
		 *
		 * @param x
		 *            the x coordinate relative to joystick up button position.
		 * @param y
		 *            the y coordinate relative to joystick up button position.
		 */
		public void releaseUp(int x, int y) {
			this.joystick.upReleased(x, y);
		}

		/**
		 * Executes a joystick down release.
		 *
		 * @param x
		 *            the x coordinate relative to joystick down button position.
		 * @param y
		 *            the y coordinate relative to joystick down button position.
		 */
		public void releaseDown(int x, int y) {
			this.joystick.downReleased(x, y);
		}

		/**
		 * Executes a joystick left release.
		 *
		 * @param x
		 *            the x coordinate relative to joystick left button position.
		 * @param y
		 *            the y coordinate relative to joystick left button position.
		 */
		public void releaseLeft(int x, int y) {
			this.joystick.leftReleased(x, y);
		}

		/**
		 * Executes a joystick right release.
		 *
		 * @param x
		 *            the x coordinate relative to joystick right button position.
		 * @param y
		 *            the y coordinate relative to joystick right button position.
		 */
		public void releaseRight(int x, int y) {
			this.joystick.rightReleased(x, y);
		}

		/**
		 * Executes a joystick enter release.
		 *
		 * @param x
		 *            the x coordinate relative to joystick enter button position.
		 * @param y
		 *            the y coordinate relative to joystick enter button position.
		 */
		public void releaseEnter(int x, int y) {
			this.joystick.enterReleased(x, y);
		}
	}

	/**
	 * The different stick buttons of the joystick.
	 */
	protected static class JoystickButtonIds {
		/**
		 * Stick up button id.
		 */
		public static final int UP = 1;
		/**
		 * Stick right button id.
		 */
		public static final int RIGHT = 2;
		/**
		 * Stick down button id.
		 */
		public static final int DOWN = 3;
		/**
		 * Stick left button id.
		 */
		public static final int LEFT = 4;
		/**
		 * Stick center button id.
		 */
		public static final int OK = 5;

		private JoystickButtonIds() {
		}
	}

	@Override
	public JoystickListener getRecorderListener() {
		if (this.listener == null) {
			this.listener = new JoystickListener(this);
		}
		return this.listener;
	}

	@Override
	public void start() {
		super.start();
		Menu.getInstance();

	}

}
