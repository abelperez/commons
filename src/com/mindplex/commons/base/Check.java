/**
 * Copyright (C) 2011 Mindplex Media, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.mindplex.commons.base;

/**
 * {@code Check} contains static methods that are useful for verifying that
 * arguments specified to a function are in the expected state. For example,
 * instead of explicitly writing code that checks an input parameter for null
 * and throws an exception, this class serves as a replacement and contains
 * methods that do the same check in one line of code.
 *
 * <p>Some of the functionality provided in this class was inspired from
 * Google's com.google.common.base.Preconditions class.
 * 
 * @author Abel Perez
 */
public final class Check
{
    private Check() {}

    /**
     * Throws an {@code IllegalArgumentException} if the specified string is
     * null or 0 length; otherwise returns the supplied string.
     * 
     * @param suspect the string to check.
     *
     * @return the supplied string if its not null or 0 length.
     *
     * @throws IllegalArgumentException if the specified string is null
     * or 0 length.
     */
    public static String notEmpty(String suspect) {
        if (suspect == null || suspect.length() <= 0) {
            throw new IllegalArgumentException();
        }
        return suspect;
    }

    /**
     * Throws an {@code IllegalArgumentException} if the specified string is
     * null or 0 length; otherwise returns the supplied string.
     *
     * @param suspect the string to check.
     * @param message the message to include if an exception is raised.
     *
     * @return the supplied string if its not null or 0 length.
     *
     * @throws IllegalArgumentException if the specified string is null
     * or 0 length.
     */
    public static String notEmpty(String suspect, String message) {
        if (suspect == null || suspect.length() <= 0) {
            throw new IllegalArgumentException(message);
        }
        return suspect;
    }

    /**
     * Checks if the specified string is null and or has 0 length.  If the
     * string consists purely of white spaces then this method will return
     * {@code false}.
     *
     * @param suspect the string to check
     *
     * @return {@code true} if the specified string is null or has 0 length;
     * otherwise {@code false}.
     */
    public static boolean isEmpty(String suspect) {
        return ! (suspect != null && suspect.length() > 0);
    }

    /**
     * Returns {@code true} if the specified string has text.  In other
     * words the specified string cannot be composed purely of whitespace.
     *
     * @param suspect the string to check
     *
     * @return <code>true</code> if the specified string is not <code>null</code>,
     * its length is greater than 0, and it does not contain whitespace only.
     */
    public static boolean hasText(String suspect) {
        if (! isEmpty(suspect)) {
            // no need to continue if the given
            // string is empty.
            return false;
        }
        int end = suspect.length();
        for (int i = 0; i < end; i++) {
            if (! Character.isWhitespace(suspect.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifies that the specified string is not null. If the specified string
     * is null, a null pointer exception is raised; otherwise the supplied
     * string is returned.
     * 
     * @param suspect the string to check.
     *
     * @return the specified string if its not null; otherwise throws a
     * exception.
     *
     * @throws NullPointerException if the specified string is null.
     */
    public static <T> T notNull(T suspect) {
        if (suspect == null) {
            throw new NullPointerException();
        }
        return suspect;
    }

    /**
     * Returns {@code true} if at least one of the specified boolean parameters
     * is true; otherwise an {@code IllegalArgumentException} exception is
     * raised.
     *
     * @param arguments the arguments to check.
     *
     * @return {@code true} if at least one of the specified boolean parameters
     * is true; otherwise an {@code IllegalArgumentException} exception is
     * raised.
     *
     * @throws IllegalArgumentException if none of the specified arguments are
     * true. 
     */
    public static boolean or(Boolean... arguments) {
        for (Boolean argument : arguments) {
            if (argument) {
                return true;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Returns {@code true} if at least one of the specified boolean
     * parameters is true; otherwise an {@code IllegalArgumentException}
     * exception is raised.
     *
     * @param message an error message that indicates why an exception was
     * raised.
     * @param arguments the arguments to check.
     *
     * @return {@code true} if at least one of the specified boolean parameters
     * is true; otherwise an {@code IllegalArgumentException} exception is
     * raised.
     *
     * @throws IllegalArgumentException if none of the specified arguments are
     * true.
     */
    public static boolean or(String message, Boolean... arguments) {
        for (Boolean argument : arguments) {
            if (argument) {
                return true;
            }
        }
        throw new IllegalArgumentException(message);
    }

    /**
     * Returns {@code true} if all of the specified boolean parameters
     * are true; otherwise an {@code IllegalArgumentException} exception is
     * raised.
     *
     * @param arguments the arguments to check.
     *
     * @return {@code true} if all of the specified boolean parameters
     * are true; otherwise an {@code IllegalArgumentException} exception is
     * raised.
     *
     * @throws IllegalArgumentException if one of the specified arguments is
     * false.
     */    
    public static boolean and(Boolean... arguments) {
        for (Boolean argument : arguments) {
            if (! argument) {
                throw new IllegalArgumentException();
            }
        }
        return true;
    }

    /**
     * Returns {@code true} if all of the specified boolean parameters
     * are true; otherwise an {@code IllegalArgumentException} exception is
     * raised.
     *
     * @param message an error message that indicates why an exception was
     * raised.
     * @param arguments the arguments to check.
     *
     * @return {@code true} if all of the specified boolean parameters
     * are true; otherwise an {@code IllegalArgumentException} exception is
     * raised.
     *
     * @throws IllegalArgumentException if one of the specified arguments is
     * false.
     */
    public static boolean and(String message, Boolean... arguments) {
        for (Boolean argument : arguments) {
            if (! argument) {
                throw new IllegalArgumentException(message);
            }
        }
        return true;
    }
}