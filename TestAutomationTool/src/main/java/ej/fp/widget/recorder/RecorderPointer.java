/*
 * Java
 *
 * Copyright 2021-2022 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget.recorder;

import ej.fp.Widget.WidgetAttribute;
import ej.fp.Widget.WidgetDescription;
import ej.fp.widget.Menu;
import ej.fp.widget.Pointer;
import ej.fp.widget.command.Command;
import ej.fp.widget.command.CommandRecorder;
import ej.fp.widget.command.PointerCommand;
import ej.fp.widget.recorder.RecorderPointer.RecorderListener;

/**
 * Pointer to record and reproduce activities.
 */
@WidgetDescription(attributes = { @WidgetAttribute(name = "label", isOptional = true), @WidgetAttribute(name = "x"),
		@WidgetAttribute(name = "y"), @WidgetAttribute(name = "width"), @WidgetAttribute(name = "height"),
		@WidgetAttribute(name = "filter", isOptional = true), @WidgetAttribute(name = "areaWidth", isOptional = true),
		@WidgetAttribute(name = "areaHeight", isOptional = true), @WidgetAttribute(name = "touch", isOptional = true),
		@WidgetAttribute(name = "listenerClass", isOptional = true) })
public class RecorderPointer extends Pointer implements CommandRecorder<RecorderListener> {
	private RecorderListener listener;

	/**
	 * Recorder Listener class.
	 */
	public class RecorderListener implements PointerListener, Command.Listener {

		private final PointerListener originalListener;

		private final Pointer pointer;

		/**
		 * Listener to record the activities.
		 *
		 * @param pointer
		 *            the actual listener.
		 * @param originalListener
		 *            the original event listener.
		 */
		public RecorderListener(Pointer pointer, PointerListener originalListener) {
			this.pointer = pointer;
			this.originalListener = originalListener;
		}

		@Override
		public void press(Pointer widget, int x, int y, MouseButton button) {
			Menu.getInstance().saveCommand(PointerCommand.with(PointerCommand.Press.class, x, y));
			this.originalListener.press(widget == null ? this.pointer : widget, x, y, button);
		}

		@Override
		public void release(Pointer widget, MouseButton button) {
			Menu.getInstance().saveCommand(PointerCommand.with(PointerCommand.Release.class, 0, 0));
			this.originalListener.release(widget == null ? this.pointer : widget, button);
		}

		@Override
		public void move(Pointer widget, int x, int y) {
			Menu.getInstance().saveCommand(PointerCommand.with(PointerCommand.Move.class, x, y));
			this.originalListener.move(widget == null ? this.pointer : widget, x, y);
		}

	}

	@Override
	public void start() {
		super.start();
		Menu.getInstance();
	}

	@Override
	protected synchronized <T> T newListener(Class<T> expectedType) {
		T aListener = super.newListener(expectedType);
		this.listener = new RecorderListener(this, (PointerListener) aListener);
		return expectedType.cast(this.listener);
	}

	@Override
	public RecorderListener getRecorderListener() {
		return this.listener;
	}
}
