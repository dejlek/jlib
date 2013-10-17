/**
 * Project: jlib 
 * Version: $Id$ 
 * License: SPL
 *
 * This file is best viewed with 110 columns.
 * 34567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
 * 
 * Authors (in chronological order): 
 *   -
 * Contributors (in chronological order): 
 *   Dejan Lekic , http://dejan.lekic.org
 */
package com.areen.jlib.util;

import java.io.*;

/**
 * Code taken from http://www.javapractices.com/topic/TopicAction.do?Id=78
 * 
 * Simple utilities to return the stack trace of an exception as a String.
 */
public final class StackTraceUtil {

    public static String getStackTrace(Throwable aThrowable) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

    /**
     * Defines a custom format for the stack trace as String.
     */
    public static String getCustomStackTrace(final String argHeader, Throwable aThrowable) {
        //add the class name and any message passed to constructor
        StringBuilder result = new StringBuilder(argHeader);
        result.append(aThrowable.toString());
        String newLine = System.getProperty("line.separator");
        result.append(newLine);

        //add each element of the stack trace
        for (StackTraceElement element : aThrowable.getStackTrace()) {
            result.append(element);
            result.append(newLine);
        }
        return result.toString();
    }

    /**
     * Demonstrate output.
     */
    public static void main(String... aArguments) {
        Throwable throwable = new IllegalArgumentException("Blah");
        System.out.println(getStackTrace(throwable));
        System.out.println(getCustomStackTrace("MEEP MEEP: ", throwable));
    }
} // StackTraceUtil class

// $Id$
