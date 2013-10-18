/**
 * Project: jlib
 * Version: $Id$ 
 * License: (see below block, or the LICENSE file)
 **************************************************************************************** LICENSE BEGIN *****
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and 
 * associated documentation files (the "Software"), to deal in the Software without restriction, including 
 * without limitation the rights to use, copy, modify, merge, publish, distribute, distribute with 
 * modifications, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software 
 * is furnished to do so, subject to the following conditions:
 * 
 * 1) The above copyright notice and this permission notice shall be included in all copies or substantial 
 * portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO 
 * EVENT SHALL THE ABOVE COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN 
 * AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * Except as contained in this notice, the name(s) of the above copyright holders shall not be used in 
 * advertising or otherwise to promote the sale, use or other dealings in this Software without prior written 
 * authorisation.
 ****************************************************************************************** LICENSE END *****
 * 
 * This file is best viewed with 110 columns.
12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 * 
 * In chronological order
 * Author(s):
 *   Dejan Lekic , http://dejan.lekic.org
 * Contributor(s):
 *   -
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
