
/*
 * Java
 *
 * Copyright 2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget.util;

/*
Copyright (c) 2002 JSON.org
Copyright 2016-2019 MicroEJ Corp. This file has been modified by MicroEJ Corp.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

The Software shall be used for Good, not Evil.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A JSONObject is an unordered collection of name/value pairs. Its external form is a string wrapped in curly braces
 * with colons between the names and values, and commas between the values and names. The internal form is an object
 * having <code>get</code> and <code>opt</code> methods for accessing the values by name, and <code>put</code> methods
 * for adding or replacing values by name. The values can be any of these types: <code>Boolean</code>,
 * <code>JSONArray</code>, <code>JSONObject</code>, <code>Number</code>, <code>String</code>, or the
 * <code>JSONObject.NULL</code> object. A JSONObject constructor can be used to convert an external form JSON text into
 * an internal form whose values can be retrieved with the <code>get</code> and <code>opt</code> methods, or to convert
 * values into a JSON text using the <code>put</code> and <code>toString</code> methods. A <code>get</code> method
 * returns a value if one can be found, and throws an exception if one cannot be found. An <code>opt</code> method
 * returns a default value instead of throwing an exception, and so is useful for obtaining optional values.
 * <p>
 * The generic <code>get()</code> and <code>opt()</code> methods return an object, which you can cast or query for type.
 * There are also typed <code>get</code> and <code>opt</code> methods that do type checking and type coersion for you.
 * <p>
 * The <code>put</code> methods adds values to an object. For example,
 *
 * <pre>
 * myString = new JSONObject().put("JSON", "Hello, World!").toString();
 * </pre>
 *
 * produces the string <code>{"JSON": "Hello, World"}</code>.
 * <p>
 * The texts produced by the <code>toString</code> methods strictly conform to the JSON sysntax rules. The constructors
 * are more forgiving in the texts they will accept:
 * <ul>
 * <li>An extra <code>,</code>&nbsp;<small>(comma)</small> may appear just before the closing brace.</li>
 * <li>Strings may be quoted with <code>'</code>&nbsp;<small>(single quote)</small>.</li>
 * <li>Strings do not need to be quoted at all if they do not begin with a quote or single quote, and if they do not
 * contain leading or trailing spaces, and if they do not contain any of these characters:
 * <code>{ } [ ] / \ : , = ; #</code> and if they do not look like numbers and if they are not the reserved words
 * <code>true</code>, <code>false</code>, or <code>null</code>.</li>
 * <li>Keys can be followed by <code>=</code> or <code>=&gt;</code> as well as by <code>:</code>.</li>
 * <li>Values can be followed by <code>;</code> <small>(semicolon)</small> as well as by <code>,</code>
 * <small>(comma)</small>.</li>
 * <li>Numbers may have the <code>0-</code> <small>(octal)</small> or <code>0x-</code> <small>(hex)</small> prefix.</li>
 * <li>Comments written in the slashshlash, slashstar, and hash conventions will be ignored.</li>
 * </ul>
 *
 * @author JSON.org
 * @version 2
 */
public class JSONObject {

	private static final String EXPECTED_A = "Expected a '";
	private static final String A_JSON_OBJECT_TEXT_MUST = "A JSONObject text must ";
	private static final String JSON_OBJECT = "JSONObject[";
	private static final String NULL_KEY = "Null key.";
	private static final String NULL_POINTER = "Null pointer";
	private static final String IS_NOT = "is not a ";
	private static final String IS_NOT_A_STRING = IS_NOT + "string";
	private static final String IS_NOT_A_LONG = IS_NOT + "long";
	private static final String IS_NOT_A_JSON_OBJECT = IS_NOT + "JSONObject";
	private static final String IS_NOT_AN_INT = IS_NOT + "int";
	private static final String IS_NOT_A_NUMBER = IS_NOT + "number";
	private static final String IS_NOT_A_BOOLEAN = IS_NOT + "Boolean";
	private static final String NOT_FOUND = "not found";
	private static final String IS_NOT_A_JSON_ARRAY = IS_NOT + "JSONArray";
	private static final String DUPLICATE_KEY = "Duplicate key \"";
	private static final String EMPTY_JSON = "{}";
	private static final String BAD_VALUE_FROM_TO_JSON_STRING = "Bad value from toJSONString: ";
	private static final String EXPECTED_A_OR = EXPECTED_A + ",' or '}'";
	private static final String EXPECTED_A_AFTER_A_KEY = EXPECTED_A + ":' after a key";
	private static final String A_JSON_OBJECT_TEXT_MUST_END_WITH = A_JSON_OBJECT_TEXT_MUST + "end with '}'";
	private static final String A_JSON_OBJECT_TEXT_MUST_BEGIN_WITH = A_JSON_OBJECT_TEXT_MUST + "begin with '{'";
	private static final String JSON_DOES_NOT_ALLOW_NON_FINITE_NUMBERS = "JSON does not allow non-finite numbers";
	private static final String NULL_STRING = "null";
	private static final String FALSE = "false";
	private static final String TRUE = "true";

	/**
	 * JSONObject.NULL is equivalent to the value that JavaScript calls null, whilst Java's null is equivalent to the
	 * value that JavaScript calls undefined.
	 */
	private static final class Null {

		/**
		 * There is only intended to be a single instance of the NULL object, so the clone method returns itself.
		 *
		 * @return NULL.
		 */
		@Override
		protected final Object clone() {
			return this;
		}

		/**
		 * A Null object is equal to the null value and to itself.
		 *
		 * @param object
		 *            An object to test for nullness.
		 * @return true if the object parameter is the JSONObject.NULL object or null.
		 */
		@Override
		public boolean equals(Object object) {
			return object == null || object == this;
		}

		/**
		 * Get the "null" string value.
		 *
		 * @return The string "null".
		 */
		@Override
		public String toString() {
			return NULL_STRING;
		}

		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return super.hashCode();
		}
	}

	/**
	 * The hash map where the JSONObject's properties are kept.
	 */
	private final Map<String, Object> map;

	/**
	 * It is sometimes more convenient and less ambiguous to have a <code>NULL</code> object than to use Java's
	 * <code>null</code> value. <code>JSONObject.NULL.equals(null)</code> returns <code>true</code>.
	 * <code>JSONObject.NULL.toString()</code> returns <code>"null"</code>.
	 */
	public static final Object NULL = new Null();

	/**
	 * Construct an empty JSONObject.
	 */
	public JSONObject() {
		this.map = new HashMap<>();
	}

	/**
	 * Construct a JSONObject from a subset of another JSONObject. An array of strings is used to identify the keys that
	 * should be copied. Missing keys are ignored.
	 *
	 * @param jo
	 *            A JSONObject.
	 * @param sa
	 *            An array of strings.
	 * @exception JSONException
	 *                If a value is a non-finite number.
	 */
	public JSONObject(JSONObject jo, String[] sa) throws JSONException {
		this();
		for (int i = 0; i < sa.length; i += 1) {
			putOpt(sa[i], jo.opt(sa[i]));
		}
	}

	/**
	 * Construct a JSONObject from a JSONTokener.
	 *
	 * @param x
	 *            A JSONTokener object containing the source string.
	 * @throws JSONException
	 *             If there is a syntax error in the source string or a duplicated key.
	 */
	public JSONObject(JSONTokener x) throws JSONException {
		this();
		char c;
		String key;

		if (x.nextClean() != '{') {
			throw x.syntaxError(A_JSON_OBJECT_TEXT_MUST_BEGIN_WITH);
		}
		for (;;) {
			c = x.nextClean();
			switch (c) {
			case 0:
				throw x.syntaxError(A_JSON_OBJECT_TEXT_MUST_END_WITH);
			case '}':
				return;
			default:
				x.back();
				key = x.nextValue().toString();
			}

			// The key is followed by ':'.

			c = x.nextClean();
			if (c != ':') {
				throw x.syntaxError(EXPECTED_A_AFTER_A_KEY);
			}
			this.putOnce(key, x.nextValue());

			// Pairs are separated by ','.

			switch (x.nextClean()) {
			case ';':
			case ',':
				if (x.nextClean() == '}') {
					return;
				}
				x.back();
				break;
			case '}':
				return;
			default:
				throw x.syntaxError(EXPECTED_A_OR);
			}
		}
	}

	/**
	 * Try to convert a string into a number, boolean, or null. If the string can't be converted, return the string.
	 *
	 * @param string
	 *            A String.
	 * @return A simple JSON value.
	 */
	public static Object stringToValue(String string) {
		if (string.equals("")) {
			return string;
		}
		if (string.equalsIgnoreCase(TRUE)) {
			return Boolean.TRUE;
		}
		if (string.equalsIgnoreCase(FALSE)) {
			return Boolean.FALSE;
		}
		if (string.equalsIgnoreCase(NULL_STRING)) {
			return JSONObject.NULL;
		}

		/*
		 * If it might be a number, try converting it. If a number cannot be produced, then the value will just be a
		 * string.
		 */

		char initial = string.charAt(0);
		if ((initial >= '0' && initial <= '9') || initial == '-') {
			try {
				if (string.indexOf('.') > -1 || string.indexOf('e') > -1 || string.indexOf('E') > -1
						|| "-0".equals(string)) {
					Double d = Double.valueOf(string);
					if (!d.isInfinite() && !d.isNaN()) {
						return d;
					}
				} else {
					Long myLong = new Long(string);
					if (string.equals(myLong.toString())) {
						if (myLong.longValue() == myLong.intValue()) {
							return Integer.valueOf(myLong.intValue());
						}
						return myLong;
					}
				}
			} catch (Exception ignore) {
			}
		}
		return string;
	}

	/**
	 * Construct a JSONObject from a Map.
	 *
	 * @param map
	 *            A map object that can be used to initialize the contents of the JSONObject.
	 */
	public JSONObject(Map<?, ?> map) {
		this.map = new HashMap<String, Object>();
		if (map != null) {
			for (final Entry<?, ?> e : map.entrySet()) {
				final Object value = e.getValue();
				if (value != null) {
					this.map.put(String.valueOf(e.getKey()), value);
				}
			}
		}
	}

	/**
	 * Construct a JSONObject from a string. This is the most commonly used JSONObject constructor.
	 *
	 * @param string
	 *            A string beginning with <code>{</code>&nbsp;<small>(left brace)</small> and ending with <code>}</code>
	 *            &nbsp;<small>(right brace)</small>.
	 * @exception JSONException
	 *                If there is a syntax error in the source string.
	 */
	public JSONObject(String string) throws JSONException {
		this(new JSONTokener(string));
	}

	/**
	 * Accumulate values under a key. It is similar to the put method except that if there is already an object stored
	 * under the key then a JSONArray is stored under the key to hold all of the accumulated values. If there is already
	 * a JSONArray, then the new value is appended to it. In contrast, the put method replaces the previous value.
	 *
	 * @param key
	 *            A key string.
	 * @param value
	 *            An object to be accumulated under the key.
	 * @return this.
	 * @throws JSONException
	 *             If the value is an invalid number or if the key is null.
	 */
	public JSONObject accumulate(String key, Object value) throws JSONException {
		testValidity(value);
		Object o = opt(key);
		if (o == null) {
			put(key, value);
		} else if (o instanceof JSONArray) {
			((JSONArray) o).put(value);
		} else {
			put(key, new JSONArray().put(o).put(value));
		}
		return this;
	}

	/**
	 * Append values to the array under a key. If the key does not exist in the JSONObject, then the key is put in the
	 * JSONObject with its value being a JSONArray containing the value parameter. If the key was already associated
	 * with a JSONArray, then the value parameter is appended to it.
	 *
	 * @param key
	 *            A key string.
	 * @param value
	 *            An object to be accumulated under the key.
	 * @return this.
	 * @throws JSONException
	 *             If the key is null or if the current value associated with the key is not a JSONArray.
	 */
	public JSONObject append(String key, Object value) throws JSONException {
		testValidity(value);
		Object object = this.opt(key);
		if (object == null) {
			this.put(key, new JSONArray().put(value));
		} else if (object instanceof JSONArray) {
			this.put(key, ((JSONArray) object).put(value));
		} else {
			throw new JSONException(jsonObjectError(key, IS_NOT_A_JSON_ARRAY));
		}
		return this;
	}

	/**
	 * Produce a string from a double. The string "null" will be returned if the number is not finite.
	 *
	 * @param d
	 *            A double.
	 * @return A String.
	 */
	public static String doubleToString(double d) {
		if (Double.isInfinite(d) || Double.isNaN(d)) {
			return NULL_STRING;
		}

		// Shave off trailing zeros and decimal point, if possible.

		String s = Double.toString(d);

		if (s.indexOf('.') > 0 && s.indexOf('e') < 0 && s.indexOf('E') < 0) {
			while (s.endsWith("0")) {
				s = s.substring(0, s.length() - 1);
			}
			if (s.endsWith(".")) {
				s = s.substring(0, s.length() - 1);
			}
		}
		return s;
	}

	/**
	 * Get the value object associated with a key.
	 *
	 * @param key
	 *            A key string.
	 * @return The object associated with the key.
	 * @throws JSONException
	 *             if the key is not found.
	 */
	public Object get(String key) throws JSONException {
		if (key == null) {
			throw new JSONException(NULL_KEY);
		}
		Object object = this.opt(key);
		if (object == null) {
			throw new JSONException(jsonObjectError(quote(key), NOT_FOUND));
		}
		return object;
	}

	/**
	 * Get the boolean value associated with a key.
	 *
	 * @param key
	 *            A key string.
	 * @return The truth.
	 * @throws JSONException
	 *             if the value is not a Boolean or the String "true" or "false".
	 */
	public boolean getBoolean(String key) throws JSONException {
		Object object = this.get(key);
		if (object.equals(Boolean.FALSE) || (object instanceof String && ((String) object).equalsIgnoreCase(FALSE))) {
			return false;
		} else if (object.equals(Boolean.TRUE)
				|| (object instanceof String && ((String) object).equalsIgnoreCase(TRUE))) {
			return true;
		}
		throw new JSONException(jsonObjectError(quote(key), IS_NOT_A_BOOLEAN));
	}

	/**
	 * Get the double value associated with a key.
	 *
	 * @param key
	 *            A key string.
	 * @return The numeric value.
	 * @throws JSONException
	 *             if the key is not found or if the value is not a Number object and cannot be converted to a number.
	 */
	public double getDouble(String key) throws JSONException {
		Object object = this.get(key);
		try {
			return object instanceof Number ? ((Number) object).doubleValue() : Double.parseDouble((String) object);
		} catch (Exception e) {
			throw new JSONException(jsonObjectError(quote(key), IS_NOT_A_NUMBER));
		}
	}

	/**
	 * Get the int value associated with a key. If the number value is too large for an int, it will be clipped.
	 *
	 * @param key
	 *            A key string.
	 * @return The integer value.
	 * @throws JSONException
	 *             if the key is not found or if the value cannot be converted to an integer.
	 */
	public int getInt(String key) throws JSONException {
		Object object = this.get(key);
		try {
			return object instanceof Number ? ((Number) object).intValue() : Integer.parseInt((String) object);
		} catch (Exception e) {
			throw new JSONException(jsonObjectError(quote(key), IS_NOT_AN_INT));
		}
	}

	/**
	 * Get the JSONArray value associated with a key.
	 *
	 * @param key
	 *            A key string.
	 * @return A JSONArray which is the value.
	 * @throws JSONException
	 *             if the key is not found or if the value is not a JSONArray.
	 */
	public JSONArray getJSONArray(String key) throws JSONException {
		Object o = get(key);
		if (o instanceof JSONArray) {
			return (JSONArray) o;
		}
		throw new JSONException(jsonObjectError(quote(key), IS_NOT_A_JSON_ARRAY));
	}

	/**
	 * Get the JSONObject value associated with a key.
	 *
	 * @param key
	 *            A key string.
	 * @return A JSONObject which is the value.
	 * @throws JSONException
	 *             if the key is not found or if the value is not a JSONObject.
	 */
	public JSONObject getJSONObject(String key) throws JSONException {
		Object o = get(key);
		if (o instanceof JSONObject) {
			return (JSONObject) o;
		}
		throw new JSONException(jsonObjectError(quote(key), IS_NOT_A_JSON_OBJECT));
	}

	/**
	 * Get the long value associated with a key. If the number value is too long for a long, it will be clipped.
	 *
	 * @param key
	 *            A key string.
	 * @return The long value.
	 * @throws JSONException
	 *             if the key is not found or if the value cannot be converted to a long.
	 */
	public long getLong(String key) throws JSONException {
		Object object = this.get(key);
		try {
			return object instanceof Number ? ((Number) object).longValue() : Long.parseLong((String) object);
		} catch (Exception e) {
			throw new JSONException(jsonObjectError(quote(key), IS_NOT_A_LONG));
		}
	}

	/**
	 * Get the string associated with a key.
	 *
	 * @param key
	 *            A key string.
	 * @return A string which is the value.
	 * @throws JSONException
	 *             if the key is not found.
	 */
	public String getString(String key) throws JSONException {
		Object object = this.get(key);
		if (object instanceof String) {
			return (String) object;
		}
		throw new JSONException(jsonObjectError(quote(key), IS_NOT_A_STRING));
	}

	/**
	 * Determine if the JSONObject contains a specific key.
	 *
	 * @param key
	 *            A key string.
	 * @return true if the key exists in the JSONObject.
	 */
	public boolean has(String key) {
		return this.map.containsKey(key);
	}

	/**
	 * Determine if the value associated with the key is null or if there is no value.
	 *
	 * @param key
	 *            A key string.
	 * @return true if there is no value associated with the key or if the value is the JSONObject.NULL object.
	 */
	public boolean isNull(String key) {
		return JSONObject.NULL.equals(opt(key));
	}

	/**
	 * Get an enumeration of the keys of the JSONObject.
	 *
	 * @return An iterator of the keys.
	 */
	// TODO return Iterator instead of Enumeration.
	public Enumeration keys() {
		return new EnumerationIterator<>(this.keySet().iterator());
	}

	/**
	 * Get a set of keys of the JSONObject.
	 *
	 * @return A keySet.
	 */
	public Set<String> keySet() {
		return this.map.keySet();
	}

	/**
	 * Get the number of keys stored in the JSONObject.
	 *
	 * @return The number of keys in the JSONObject.
	 */
	public int length() {
		return this.map.size();
	}

	/**
	 * Produce a JSONArray containing the names of the elements of this JSONObject.
	 *
	 * @return A JSONArray containing the key strings, or null if the JSONObject is empty.
	 */
	public JSONArray names() {
		JSONArray ja = new JSONArray();
		Iterator<String> keys = this.keySet().iterator();
		while (keys.hasNext()) {
			ja.put(keys.next());
		}
		return ja.length() == 0 ? null : ja;
	}

	/**
	 * Shave off trailing zeros and decimal point, if possible.
	 */
	public static String trimNumber(String s) {
		if (s.indexOf('.') > 0 && s.indexOf('e') < 0 && s.indexOf('E') < 0) {
			while (s.endsWith("0")) {
				s = s.substring(0, s.length() - 1);
			}
			if (s.endsWith(".")) {
				s = s.substring(0, s.length() - 1);
			}
		}
		return s;
	}

	/**
	 * Produce a string from a Number.
	 *
	 * @param n
	 *            A Number
	 * @return A String.
	 * @throws JSONException
	 *             If n is a non-finite number.
	 */
	public static String numberToString(Object n) throws JSONException {
		if (n == null) {
			throw new JSONException(NULL_POINTER);
		}
		testValidity(n);
		return trimNumber(n.toString());
	}

	/**
	 * Get an optional value associated with a key.
	 *
	 * @param key
	 *            A key string.
	 * @return An object which is the value, or null if there is no value.
	 */
	public Object opt(String key) {
		return key == null ? null : this.map.get(key);
	}

	/**
	 * Get an optional boolean associated with a key. It returns false if there is no such key, or if the value is not
	 * Boolean.TRUE or the String "true".
	 *
	 * @param key
	 *            A key string.
	 * @return The truth.
	 */
	public boolean optBoolean(String key) {
		return optBoolean(key, false);
	}

	/**
	 * Get an optional boolean associated with a key. It returns the defaultValue if there is no such key, or if it is
	 * not a Boolean or the String "true" or "false" (case insensitive).
	 *
	 * @param key
	 *            A key string.
	 * @param defaultValue
	 *            The default.
	 * @return The truth.
	 */
	public boolean optBoolean(String key, boolean defaultValue) {
		try {
			return getBoolean(key);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Get an optional double associated with a key, or NaN if there is no such key or if its value is not a number. If
	 * the value is a string, an attempt will be made to evaluate it as a number.
	 *
	 * @param key
	 *            A string which is the key.
	 * @return An object which is the value.
	 */
	public double optDouble(String key) {
		return optDouble(key, Double.NaN);
	}

	/**
	 * Get an optional double associated with a key, or the defaultValue if there is no such key or if its value is not
	 * a number. If the value is a string, an attempt will be made to evaluate it as a number.
	 *
	 * @param key
	 *            A key string.
	 * @param defaultValue
	 *            The default.
	 * @return An object which is the value.
	 */
	public double optDouble(String key, double defaultValue) {
		try {
			return this.getDouble(key);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Get an optional int value associated with a key, or zero if there is no such key or if the value is not a number.
	 * If the value is a string, an attempt will be made to evaluate it as a number.
	 *
	 * @param key
	 *            A key string.
	 * @return An object which is the value.
	 */
	public int optInt(String key) {
		return optInt(key, 0);
	}

	/**
	 * Get an optional int value associated with a key, or the default if there is no such key or if the value is not a
	 * number. If the value is a string, an attempt will be made to evaluate it as a number.
	 *
	 * @param key
	 *            A key string.
	 * @param defaultValue
	 *            The default.
	 * @return An object which is the value.
	 */
	public int optInt(String key, int defaultValue) {
		try {
			return getInt(key);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Get an optional JSONArray associated with a key. It returns null if there is no such key, or if its value is not
	 * a JSONArray.
	 *
	 * @param key
	 *            A key string.
	 * @return A JSONArray which is the value.
	 */
	public JSONArray optJSONArray(String key) {
		Object o = opt(key);
		return o instanceof JSONArray ? (JSONArray) o : null;
	}

	/**
	 * Get an optional JSONObject associated with a key. It returns null if there is no such key, or if its value is not
	 * a JSONObject.
	 *
	 * @param key
	 *            A key string.
	 * @return A JSONObject which is the value.
	 */
	public JSONObject optJSONObject(String key) {
		Object o = opt(key);
		return o instanceof JSONObject ? (JSONObject) o : null;
	}

	/**
	 * Get an optional long value associated with a key, or zero if there is no such key or if the value is not a
	 * number. If the value is a string, an attempt will be made to evaluate it as a number.
	 *
	 * @param key
	 *            A key string.
	 * @return An object which is the value.
	 */
	public long optLong(String key) {
		return optLong(key, 0);
	}

	/**
	 * Get an optional long value associated with a key, or the default if there is no such key or if the value is not a
	 * number. If the value is a string, an attempt will be made to evaluate it as a number.
	 *
	 * @param key
	 *            A key string.
	 * @param defaultValue
	 *            The default.
	 * @return An object which is the value.
	 */
	public long optLong(String key, long defaultValue) {
		try {
			return getLong(key);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Get an optional string associated with a key. It returns an empty string if there is no such key. If the value is
	 * not a string and is not null, then it is coverted to a string.
	 *
	 * @param key
	 *            A key string.
	 * @return A string which is the value.
	 */
	public String optString(String key) {
		return optString(key, "");
	}

	/**
	 * Get an optional string associated with a key. It returns the defaultValue if there is no such key.
	 *
	 * @param key
	 *            A key string.
	 * @param defaultValue
	 *            The default.
	 * @return A string which is the value.
	 */
	public String optString(String key, String defaultValue) {
		Object object = this.opt(key);
		return NULL.equals(object) ? defaultValue : object.toString();
	}

	/**
	 * Put a key/boolean pair in the JSONObject.
	 *
	 * @param key
	 *            A key string.
	 * @param value
	 *            A boolean which is the value.
	 * @return this.
	 * @throws JSONException
	 *             If the key is null.
	 */
	public JSONObject put(String key, boolean value) throws JSONException {
		put(key, value ? Boolean.TRUE : Boolean.FALSE);
		return this;
	}

	/**
	 * Put a key/double pair in the JSONObject.
	 *
	 * @param key
	 *            A key string.
	 * @param value
	 *            A double which is the value.
	 * @return this.
	 * @throws JSONException
	 *             If the key is null or if the number is invalid.
	 */
	public JSONObject put(String key, double value) throws JSONException {
		put(key, new Double(value));
		return this;
	}

	/**
	 * Put a key/int pair in the JSONObject.
	 *
	 * @param key
	 *            A key string.
	 * @param value
	 *            An int which is the value.
	 * @return this.
	 * @throws JSONException
	 *             If the key is null.
	 */
	public JSONObject put(String key, int value) throws JSONException {
		put(key, new Integer(value));
		return this;
	}

	/**
	 * Put a key/long pair in the JSONObject.
	 *
	 * @param key
	 *            A key string.
	 * @param value
	 *            A long which is the value.
	 * @return this.
	 * @throws JSONException
	 *             If the key is null.
	 */
	public JSONObject put(String key, long value) throws JSONException {
		put(key, new Long(value));
		return this;
	}

	/**
	 * Put a key/value pair in the JSONObject, where the value will be a JSONObject which is produced from a Map.
	 *
	 * @param key
	 *            A key string.
	 * @param value
	 *            A Map value.
	 * @return this.
	 * @throws JSONException
	 */
	public JSONObject put(String key, Map<?, ?> value) throws JSONException {
		this.put(key, new JSONObject(value));
		return this;
	}

	/**
	 * Put a key/value pair in the JSONObject. If the value is null, then the key will be removed from the JSONObject if
	 * it is present.
	 *
	 * @param key
	 *            A key string.
	 * @param value
	 *            An object which is the value. It should be of one of these types: Boolean, Double, Integer, JSONArray,
	 *            JSONObject, Long, String, or the JSONObject.NULL object.
	 * @return this.
	 * @throws JSONException
	 *             If the value is non-finite number or if the key is null.
	 */
	public JSONObject put(String key, Object value) throws JSONException {
		if (key == null) {
			throw new JSONException(NULL_KEY);
		}
		if (value != null) {
			testValidity(value);
			this.map.put(key, value);
		} else {
			remove(key);
		}
		return this;
	}

	/**
	 * Put a key/value pair in the JSONObject, but only if the key and the value are both non-null, and only if there is
	 * not already a member with that name.
	 *
	 * @param key
	 *            string
	 * @param value
	 *            object
	 * @return this.
	 * @throws JSONException
	 *             if the key is a duplicate
	 */
	public JSONObject putOnce(String key, Object value) throws JSONException {
		if (key != null && value != null) {
			if (this.opt(key) != null) {
				throw new JSONException(DUPLICATE_KEY + key + "\"");
			}
			this.put(key, value);
		}
		return this;
	}

	/**
	 * Put a key/value pair in the JSONObject, but only if the key and the value are both non-null.
	 *
	 * @param key
	 *            A key string.
	 * @param value
	 *            An object which is the value. It should be of one of these types: Boolean, Double, Integer, JSONArray,
	 *            JSONObject, Long, String, or the JSONObject.NULL object.
	 * @return this.
	 * @throws JSONException
	 *             If the value is a non-finite number.
	 */
	public JSONObject putOpt(String key, Object value) throws JSONException {
		if (key != null && value != null) {
			put(key, value);
		}
		return this;
	}

	/**
	 * Produce a string in double quotes with backslash sequences in all the right places. A backslash will be inserted
	 * within &lt;/, allowing JSON text to be delivered in HTML. In JSON text, a string cannot contain a control
	 * character or an unescaped quote or backslash.
	 *
	 * @param string
	 *            A String
	 * @return A String correctly formatted for insertion in a JSON text.
	 */
	public static String quote(String string) {
		StringWriter sw = new StringWriter();
		try {
			return quote(string, sw).toString();
		} catch (IOException ignored) {
			// will never happen - we are writing to a string writer
			return "";
		}
	}

	private static Writer quote(String string, Writer w) throws IOException {
		if (string == null || string.length() == 0) {
			w.write("\"\"");
			return w;
		}

		char b;
		char c = 0;
		String hhhh;
		int i;
		int len = string.length();

		w.write('"');

		for (i = 0; i < len; i += 1) {
			b = c;
			c = string.charAt(i);
			switch (c) {
			case '\\':
			case '"':
				w.write('\\');
				w.write(c);
				break;
			case '/':
				if (b == '<') {
					w.write('\\');
				}
				w.write(c);
				break;
			case '\b':
				w.write("\\b");
				break;
			case '\t':
				w.write("\\t");
				break;
			case '\n':
				w.write("\\n");
				break;
			case '\f':
				w.write("\\f");
				break;
			case '\r':
				w.write("\\r");
				break;
			default:
				if (c < ' ' || (c >= '\u0080' && c < '\u00a0') || (c >= '\u2000' && c < '\u2100')) {
					w.write("\\u");
					hhhh = Integer.toHexString(c);
					w.write("0000", 0, 4 - hhhh.length());
					w.write(hhhh);
				} else {
					w.write(c);
				}
			}
		}

		w.write('"');
		return w;
	}

	/**
	 * Remove a name and its value, if present.
	 *
	 * @param key
	 *            The name to be removed.
	 * @return The value that was associated with the name, or null if there was no value.
	 */
	public Object remove(String key) {
		return this.map.remove(key);
	}

	/**
	 * Throw an exception if the object is an NaN or infinite number.
	 *
	 * @param o
	 *            The object to test.
	 * @throws JSONException
	 *             If o is a non-finite number.
	 */
	static void testValidity(Object o) throws JSONException {
		if (o != null) {
			if (o instanceof Double) {
				if (((Double) o).isInfinite() || ((Double) o).isNaN()) {
					throw new JSONException(JSON_DOES_NOT_ALLOW_NON_FINITE_NUMBERS);
				}
			} else if (o instanceof Float) {
				if (((Float) o).isInfinite() || ((Float) o).isNaN()) {
					throw new JSONException(JSON_DOES_NOT_ALLOW_NON_FINITE_NUMBERS);
				}
			}
		}
	}

	/**
	 * Produce a JSONArray containing the values of the members of this JSONObject.
	 *
	 * @param names
	 *            A JSONArray containing a list of key strings. This determines the sequence of the values in the
	 *            result.
	 * @return A JSONArray of values.
	 * @throws JSONException
	 *             If any of the values are non-finite numbers.
	 */
	public JSONArray toJSONArray(JSONArray names) throws JSONException {
		if (names == null || names.length() == 0) {
			return null;
		}
		JSONArray ja = new JSONArray();
		for (int i = 0; i < names.length(); i += 1) {
			ja.put(this.opt(names.getString(i)));
		}
		return ja;
	}

	/**
	 * Make a JSON text of this JSONObject. For compactness, no whitespace is added. If this would not result in a
	 * syntactically correct JSON text, then null will be returned instead.
	 * <p>
	 * Warning: This method assumes that the data structure is acyclical.
	 *
	 * @return a printable, displayable, portable, transmittable representation of the object, beginning with
	 *         <code>{</code>&nbsp;<small>(left brace)</small> and ending with <code>}</code>&nbsp;<small>(right
	 *         brace)</small>.
	 */
	@Override
	public String toString() {
		try {
			Iterator<String> keys = this.keySet().iterator();
			StringBuffer sb = new StringBuffer("{");

			while (keys.hasNext()) {
				if (sb.length() > 1) {
					sb.append(',');
				}
				Object o = keys.next();
				sb.append(quote(o.toString()));
				sb.append(':');
				sb.append(valueToString(this.map.get(o)));
			}
			sb.append('}');
			return sb.toString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Make a prettyprinted JSON text of this JSONObject.
	 * <p>
	 * Warning: This method assumes that the data structure is acyclical.
	 *
	 * @param indentFactor
	 *            The number of spaces to add to each level of indentation.
	 * @return a printable, displayable, portable, transmittable representation of the object, beginning with
	 *         <code>{</code>&nbsp;<small>(left brace)</small> and ending with <code>}</code>&nbsp;<small>(right
	 *         brace)</small>.
	 * @throws JSONException
	 *             If the object contains an invalid number.
	 */
	public String toString(int indentFactor) throws JSONException {
		return toString(indentFactor, 0);
	}

	/**
	 * Wrap an object, if necessary. If the object is null, return the NULL object. If it is an array or collection,
	 * wrap it in a JSONArray. If it is a map, wrap it in a JSONObject. If it is a standard property (Double, String, et
	 * al) then it is already wrapped. Otherwise, if it comes from one of the java packages, turn it into a string. And
	 * if it doesn't, try to wrap it in a JSONObject. If the wrapping fails, then null is returned.
	 *
	 * @param object
	 *            The object to wrap
	 * @return The wrapped value
	 */
	public static Object wrap(Object object) {
		try {
			if (object == null) {
				return NULL;
			}
			if (object instanceof JSONObject || object instanceof JSONArray || NULL.equals(object)
					|| object instanceof JSONString || object instanceof Byte || object instanceof Character
					|| object instanceof Short || object instanceof Integer || object instanceof Long
					|| object instanceof Boolean || object instanceof Float || object instanceof Double
					|| object instanceof String) {
				return object;
			}

			if (object instanceof Collection) {
				Collection<?> coll = (Collection<?>) object;
				return new JSONArray(coll);
			}

			if (object.getClass().isArray()) {
				return Util.buildJSONArrayFromArray(object);
			}

			if (object instanceof Map) {
				Map<?, ?> map = (Map<?, ?>) object;
				return new JSONObject(map);
			}

			return null;
		} catch (Exception exception) {
			return null;
		}
	}

	/**
	 * Make a prettyprinted JSON text of this JSONObject.
	 * <p>
	 * Warning: This method assumes that the data structure is acyclical.
	 *
	 * @param indentFactor
	 *            The number of spaces to add to each level of indentation.
	 * @param indent
	 *            The indentation of the top level.
	 * @return a printable, displayable, transmittable representation of the object, beginning with <code>{</code>&nbsp;
	 *         <small>(left brace)</small> and ending with <code>}</code>&nbsp;<small>(right brace)</small>.
	 * @throws JSONException
	 *             If the object contains an invalid number.
	 */
	String toString(int indentFactor, int indent) throws JSONException {
		int i;
		int n = length();
		if (n == 0) {
			return EMPTY_JSON;
		}
		Iterator<String> keys = this.keySet().iterator();
		StringBuffer sb = new StringBuffer("{");
		int newindent = indent + indentFactor;
		Object o;
		if (n == 1) {
			o = keys.next();
			sb.append(quote(o.toString()));
			sb.append(": ");
			sb.append(valueToString(this.map.get(o), indentFactor, indent));
		} else {
			while (keys.hasNext()) {
				o = keys.next();
				if (sb.length() > 1) {
					sb.append(",\n");
				} else {
					sb.append('\n');
				}
				for (i = 0; i < newindent; i += 1) {
					sb.append(' ');
				}
				sb.append(quote(o.toString()));
				sb.append(": ");
				sb.append(valueToString(this.map.get(o), indentFactor, newindent));
			}
			if (sb.length() > 1) {
				sb.append('\n');
				for (i = 0; i < indent; i += 1) {
					sb.append(' ');
				}
			}
		}
		sb.append('}');
		return sb.toString();
	}

	/**
	 * Make a JSON text of an Object value. If the object has an value.toJSONString() method, then that method will be
	 * used to produce the JSON text. The method is required to produce a strictly conforming text. If the object does
	 * not contain a toJSONString method (which is the most common case), then a text will be produced by the rules.
	 * <p>
	 * Warning: This method assumes that the data structure is acyclical.
	 *
	 * @param value
	 *            The value to be serialized.
	 * @return a printable, displayable, transmittable representation of the object, beginning with <code>{</code>&nbsp;
	 *         <small>(left brace)</small> and ending with <code>}</code>&nbsp;<small>(right brace)</small>.
	 * @throws JSONException
	 *             If the value is or contains an invalid number.
	 */
	static String valueToString(Object value) throws JSONException {
		if (value == null || value.equals(null)) {
			return NULL_STRING;
		}
		if (value instanceof JSONString) {
			Object o;
			try {
				o = ((JSONString) value).toJSONString();
			} catch (Exception e) {
				throw new JSONException(e);
			}
			if (o instanceof String) {
				return (String) o;
			}
			throw new JSONException(BAD_VALUE_FROM_TO_JSON_STRING + o);
		}
		if (value instanceof Float || value instanceof Double || value instanceof Byte || value instanceof Short
				|| value instanceof Integer || value instanceof Long) {
			return numberToString(value);
		}
		if (value instanceof Boolean || value instanceof JSONObject || value instanceof JSONArray) {
			return value.toString();
		}
		if (value instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) value;
			return new JSONObject(map).toString();
		}
		if (value instanceof Collection) {
			Collection<?> coll = (Collection<?>) value;
			return new JSONArray(coll).toString();
		}
		if (value.getClass().isArray()) {
			return Util.buildJSONArrayFromArray(value).toString();
		}
		return quote(value.toString());
	}

	/**
	 * Make a prettyprinted JSON text of an object value.
	 * <p>
	 * Warning: This method assumes that the data structure is acyclical.
	 *
	 * @param value
	 *            The value to be serialized.
	 * @param indentFactor
	 *            The number of spaces to add to each level of indentation.
	 * @param indent
	 *            The indentation of the top level.
	 * @return a printable, displayable, transmittable representation of the object, beginning with <code>{</code>&nbsp;
	 *         <small>(left brace)</small> and ending with <code>}</code>&nbsp;<small>(right brace)</small>.
	 * @throws JSONException
	 *             If the object contains an invalid number.
	 */
	static String valueToString(Object value, int indentFactor, int indent) throws JSONException {
		if (value == null || value.equals(null)) {
			return NULL_STRING;
		}
		try {
			if (value instanceof JSONString) {
				Object o = ((JSONString) value).toJSONString();
				if (o instanceof String) {
					return (String) o;
				}
			}
		} catch (Exception e) {
			/* forget about it */
		}
		if (value instanceof Float || value instanceof Double || value instanceof Byte || value instanceof Short
				|| value instanceof Integer || value instanceof Long) {
			return numberToString(value);
		}
		if (value instanceof Boolean) {
			return value.toString();
		}
		if (value instanceof JSONObject) {
			return ((JSONObject) value).toString(indentFactor, indent);
		}
		if (value instanceof JSONArray) {
			return ((JSONArray) value).toString(indentFactor, indent);
		}
		return quote(value.toString());
	}

	/**
	 * Write the contents of the JSONObject as JSON text to a writer. For compactness, no whitespace is added.
	 * <p>
	 * Warning: This method assumes that the data structure is acyclical.
	 *
	 * @return The writer.
	 * @throws JSONException
	 */
	public Writer write(Writer writer) throws JSONException {
		try {
			boolean b = false;
			Iterator<String> keys = this.keySet().iterator();
			writer.write('{');

			while (keys.hasNext()) {
				if (b) {
					writer.write(',');
				}
				Object k = keys.next();
				writer.write(quote(k.toString()));
				writer.write(':');
				Object v = this.map.get(k);
				if (v instanceof JSONObject) {
					((JSONObject) v).write(writer);
				} else if (v instanceof JSONArray) {
					((JSONArray) v).write(writer);
				} else {
					writer.write(valueToString(v));
				}
				b = true;
			}
			writer.write('}');
			return writer;
		} catch (IOException e) {
			throw new JSONException(e);
		}
	}

	private String jsonObjectError(String key, String end) {
		return JSON_OBJECT + key + "] " + end + ".";
	}

	public static class Util {
		public static JSONArray buildJSONArrayFromArray(Object value) {
			List<Object> list = new ArrayList<>();
			if (value instanceof Object[]) {
				Object[] array = (Object[]) value;
				for (Object o : array) {
					list.add(o);
				}
			} else if (value instanceof int[]) {
				Integer[] array = (Integer[]) value;

				for (Object o : array) {
					list.add(o);
				}

			} else if (value instanceof char[]) {
				Character[] array = (Character[]) value;
				for (Object o : array) {
					list.add(o);
				}
			} else if (value instanceof double[]) {
				Double[] array = (Double[]) value;
				for (Object o : array) {
					list.add(o);
				}
			} else if (value instanceof long[]) {
				Long[] array = (Long[]) value;
				for (Object o : array) {
					list.add(o);
				}
			} else if (value instanceof float[]) {
				Float[] array = (Float[]) value;
				for (Object o : array) {
					list.add(o);
				}
			} else if (value instanceof short[]) {
				Short[] array = (Short[]) value;
				for (Object o : array) {
					list.add(o);
				}
			} else if (value instanceof byte[]) {
				Byte[] array = (Byte[]) value;
				for (Object o : array) {
					list.add(o);
				}
			} else if (value instanceof boolean[]) {
				Boolean[] array = (Boolean[]) value;
				for (Object o : array) {
					list.add(o);
				}
			}
			return new JSONArray(list);
		}
	}

	private static class EnumerationIterator<E> implements Enumeration<E> {
		private final Iterator<E> iterator;

		/**
		 * Instantiates an {@link EnumerationIterator}.
		 *
		 * @param iterator
		 *            the iterator to use.
		 */
		public EnumerationIterator(Iterator<E> iterator) {
			this.iterator = iterator;
		}

		@Override
		public boolean hasMoreElements() {
			return this.iterator.hasNext();
		}

		@Override
		public E nextElement() {
			return this.iterator.next();
		}

	}
}