/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget.command;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Helper class for parse and serialization of commands.
 */
public class CommandBuilder {

	private static BiMap<String, Class<? extends Command<?>>> mappedCommands;
	private static final String COMMA = ","; //$NON-NLS-1$
	static {
		mappedCommands = new BiMap<>();
		register("pause", WaitCommand.class); //$NON-NLS-1$
		register("press", PointerCommand.Press.class); //$NON-NLS-1$
		register("move", PointerCommand.Move.class); //$NON-NLS-1$
		register("release", PointerCommand.Release.class); //$NON-NLS-1$
		register("compareScreenshot", CompareScreenshotCommand.class); //$NON-NLS-1$
		register("buttonPress", ButtonCommand.Press.class); //$NON-NLS-1$
		register("buttonRelease", ButtonCommand.Release.class); //$NON-NLS-1$
		register("buttonLongPress", LongButtonCommand.Press.class); //$NON-NLS-1$
		register("buttonLongRelease", LongButtonCommand.Release.class); //$NON-NLS-1$
		register("JoystickUpPress", JoystickCommand.UpPress.class); //$NON-NLS-1$
		register("JoystickUpRelease", JoystickCommand.UpRelease.class); //$NON-NLS-1$
		register("JoystickDownPress", JoystickCommand.DownPress.class); //$NON-NLS-1$
		register("JoystickDownRelease", JoystickCommand.DownRelease.class); //$NON-NLS-1$
		register("JoystickLeftPress", JoystickCommand.LeftPress.class); //$NON-NLS-1$
		register("JoystickLeftRelease", JoystickCommand.LeftRelease.class); //$NON-NLS-1$
		register("JoystickRightPress", JoystickCommand.RightPress.class); //$NON-NLS-1$
		register("JoystickRightRelease", JoystickCommand.RightRelease.class); //$NON-NLS-1$
		register("JoystickEnterPress", JoystickCommand.EnterPress.class); //$NON-NLS-1$
		register("JoystickEnterRelease", JoystickCommand.EnterRelease.class); //$NON-NLS-1$
		register("overrideScreenshot", OverrideScreenshotCommand.class); //$NON-NLS-1$
	}

	// Prevents initialization.
	private CommandBuilder() {
	}

	/**
	 * Registers the given command in the builder associated with the new tag.
	 *
	 * @param tag
	 *            key for serialization.
	 * @param command
	 *            the command associated with the key.
	 */
	public static void register(String tag, Class<? extends Command<?>> command) {
		mappedCommands.put(tag, command);
	}

	/**
	 * Creates a command from its serialization.
	 *
	 * @param <T>
	 *            actual type of the serialized command.
	 * @param s
	 *            serialized command.
	 * @return a command
	 * @throws InstantiationException
	 *             failure to use newInstance
	 * @throws IllegalAccessException
	 *             failure to use reflection
	 */
	public static <T extends Command<?>> T parse(String s) throws InstantiationException, IllegalAccessException {
		String[] tokens = s.split(COMMA);
		@SuppressWarnings("unchecked") // this cast is safe
		Class<T> clazz = (Class<T>) mappedCommands.get(tokens[0]);
		T command = clazz.newInstance();
		command.parse(tokens);
		return command;
	}

	/**
	 * Serializes a command.
	 *
	 * @param <T>
	 *            actual type of the serialized command.
	 * @param command
	 *            command to be serialized.
	 * @return string containing the command serialized representation
	 */
	public static <T extends Command<?>> String serialize(T command) {
		@SuppressWarnings("unchecked") // this cast is safe
		String key = mappedCommands.getKey((Class<? extends Command<?>>) command.getClass());

		return key + COMMA + command.serialize();
	}

	private static class BiMap<K extends Serializable, V extends Serializable> extends HashMap<K, V> {

		private static final long serialVersionUID = 4833279823714839115L;

		private final HashMap<V, K> reverseMap = new HashMap<>();

		@Override
		public V put(K key, V value) {
			this.reverseMap.put(value, key);
			return super.put(key, value);
		}

		public K getKey(V value) {
			return this.reverseMap.get(value);
		}

		@Override
		public boolean equals(Object object) {
			if (object != null && this.getClass().isAssignableFrom(object.getClass()) && super.equals(object)) {
				@SuppressWarnings("unchecked")
				BiMap<K, V> other = this.getClass().cast(object);
				return this.reverseMap.equals(other.reverseMap);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return super.hashCode() + this.reverseMap.hashCode();
		}
	}
}
