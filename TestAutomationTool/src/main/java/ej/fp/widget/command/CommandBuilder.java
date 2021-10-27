/*
 * Java
 *
 * Copyright 2021 MicroEJ Corp. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break MicroEJ Corp. warranties on the whole library.
 */
package ej.fp.widget.command;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Helper class for parse and serialization of commands
 */
public class CommandBuilder {

	private static BiMap<String, Class<? extends Command<?>>> mappedCommands;

	static {
		mappedCommands = new BiMap<>();
		register("pause", WaitCommand.class); //$NON-NLS-1$
		register("press", PointerCommand.Press.class); //$NON-NLS-1$
		register("move", PointerCommand.Move.class); //$NON-NLS-1$
		register("release", PointerCommand.Release.class); //$NON-NLS-1$
		register("compareScreenshot", CompareScreenshotCommand.class); //$NON-NLS-1$
		register("buttonPress", ButtonCommand.Press.class); //$NON-NLS-1$
		register("buttonRelease", ButtonCommand.Release.class); //$NON-NLS-1$
	}

	/**
	 * Registers the given command in the builder associated with the new tag
	 *
	 * @param tag
	 *            key for serialization
	 * @param command
	 *            the command associated with the key
	 */
	public static void register(String tag, Class<? extends Command<?>> command) {
		mappedCommands.put(tag, command);
	}

	/**
	 * Creates a command from its serialization
	 *
	 * @param <T>
	 *            actual type of the serialized command
	 * @param s
	 *            serialized command
	 * @return a command
	 * @throws InstantiationException
	 *             failure to use newInstance
	 * @throws IllegalAccessException
	 *             failure to use reflection
	 */
	public static <T extends Command<?>> T parse(String s) throws InstantiationException, IllegalAccessException {
		String[] tokens = s.split(","); //$NON-NLS-1$
		@SuppressWarnings("unchecked") // this cast is safe
		Class<T> clazz = (Class<T>) mappedCommands.get(tokens[0]);
		T command = clazz.newInstance();
		command.parse(tokens);
		return command;
	}

	/**
	 * Serializes a command
	 *
	 * @param <T>
	 *            actual type of the serialized command
	 * @param command
	 *            command to be serialized
	 * @return string containing the command serialized representation
	 */
	public static <T extends Command<?>> String serialize(T command) {
		@SuppressWarnings("unchecked") // this cast is safe
		String key = mappedCommands.getKey((Class<? extends Command<?>>) command.getClass());

		return key + "," + command.serialize(); //$NON-NLS-1$
	}

	private CommandBuilder() {
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
