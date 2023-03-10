/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget.recorder;

import ej.fp.Widget.WidgetAttribute;
import ej.fp.Widget.WidgetDescription;
import ej.fp.widget.Button;
import ej.fp.widget.Menu;
import ej.fp.widget.command.ButtonCommand;
import ej.fp.widget.command.Command.Listener;
import ej.fp.widget.command.CommandRecorder;

/**
 * Button events recorder.
 */
@WidgetDescription(attributes = { @WidgetAttribute(name = "label"), @WidgetAttribute(name = "x"),
		@WidgetAttribute(name = "y"), @WidgetAttribute(name = "skin"), @WidgetAttribute(name = "pushedSkin"),
		@WidgetAttribute(name = "filter", isOptional = true),
		@WidgetAttribute(name = "listenerClass", isOptional = true) })
public class RecorderButton extends Button implements CommandRecorder<RecorderButton.ButtonListener> {

	private ButtonListener listener;

	@Override
	public void mousePressed(int x, int y, MouseButton button) {
		Menu.getInstance().saveCommand(ButtonCommand.build(ButtonCommand.Press.class, this.getLabel()));
		super.mousePressed(x, y, button);
	}

	@Override
	public void mouseReleased(int x, int y, MouseButton button) {
		Menu.getInstance().saveCommand(ButtonCommand.build(ButtonCommand.Release.class, this.getLabel()));
		super.mouseReleased(x, y, button);
	}

	/**
	 * Button recorder listener.
	 */
	public static class ButtonListener implements Listener {
		RecorderButton button;

		ButtonListener(RecorderButton button) {
			this.button = button;
		}

		/**
		 * Executes a button press.
		 */
		public void press() {
			this.button.mousePressed(0, 0, MouseButton.FIRST_BUTTON);
		}

		/**
		 * Executes a button release.
		 */
		public void release() {
			this.button.mouseReleased(0, 0, MouseButton.FIRST_BUTTON);
		}
	}

	@Override
	public ButtonListener getRecorderListener() {
		if (this.listener == null) {
			this.listener = new ButtonListener(this);
		}
		return this.listener;
	}

	@Override
	public void start() {
		super.start();
		Menu.getInstance();
	}
}