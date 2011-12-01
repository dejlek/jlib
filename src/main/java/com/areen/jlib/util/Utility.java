/**
 * $Id$
 *
 * Copyright (c) 2009-2010 Areen Design Services Ltd
 * 23 Eyot Gardens; London; W6 9TR
 * http://www.areen.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Areen Design Services Ltd ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Areen Design Services Ltd.
 *
 * This file is best viewed with 110 columns.
12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 *
 * Author(s) in chronological order:
 *   Dejan Lekic , http://dejan.lekic.org
 * Contributor(s):
 *   -
 */
package com.areen.jlib.util;

import com.areen.jlib.model.SimpleObject;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

/**
 * Various utility functions.
 * 
 * @author dejan
 */
public class Utility {
    
    /**
     * A helper constructor to prevents calls from asubclass.
     */
    protected Utility() { 
        throw new UnsupportedOperationException(); 
    } // Utility constructor
    
    /**
     * A convenience function which should be used when we need a line number where the exception has been
     * thrown.
     * 
     * @return String File name, plus the line number where the exception has been thrown.
     */
    public static String getLineInfo() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getFileName() + ": Line " + ste.getLineNumber();
    } // getLineInfo() method

    /**
     * Function to export csv file.
     * 
     * TODO: (Dejan) we should remove Iterator(s), and use foreach loops instead.
     * 
     * @param file A String containing the file-name information (with path).
     * @param h1 A String object with the first header line.
     * @param h2 A String object with the second header line.
     * @param headings A Vector with column headings as String objects.
     * @param data A Vector of vectors to be exported to the CSV file.
     */
    public static void exportToCSV(final String file, final String h1, final String h2,
                                   final Vector<String> headings, final Vector<Vector<String>> data) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            PrintStream ps = new PrintStream(out);

            ps.println(h1);
            ps.println(h2);
            ps.println();

            //print headings
            Iterator<String> hi = headings.iterator();

            int i = 0;
            while (hi.hasNext()) {
                ps.print(hi.next());

                if (i < headings.size() - 1) {
                    ps.print(",");
                } else {
                    ps.println();
                } // else
                i++;
            } // while

            // print the data
            Iterator<Vector<String>> di = data.iterator();
            Iterator<String> si = null;

            while (di.hasNext()) {
                // get iterator
                si = di.next().iterator();
                i = 0;
                while (si.hasNext()) {
                    ps.print(si.next());

                    if (i < headings.size() - 1) {
                        ps.print(",");
                    } else {
                        ps.println();
                    } // else

                    i++;
                } // while
            } // while

            ps.close();
        } catch (IOException e) {
            e.printStackTrace();
        } // catch
    } // exportToCSV() method

    /**
     * Utility function which should be used when developer has a value as a String, and has to assign
     * the value to a field of a SimpleObject argSo. As you know all SimpleObject members are statically
     * typed, so argValue has to be converted to appropriate type.
     * 
     * @param argSo SimpleObject which field we have to set.
     * @param argValue The value as a String.
     * @param argIndex Index of the SO field to which we assign the value argValue.
     */
    public static void set(final SimpleObject argSo, final String argValue, final int argIndex) {
        try {
            if (argSo.getFieldClass(argIndex) == Double.class) {
                argSo.set(argIndex, Double.parseDouble(argValue));
            } else if (argSo.getFieldClass(argIndex) == String.class) {
                argSo.set(argIndex, argValue.toString());
            } else if (argSo.getFieldClass(argIndex) == Integer.class) {
                argSo.set(argIndex, Integer.parseInt(argValue));
            } else if (argSo.getFieldClass(argIndex) == Float.class) {
                argSo.set(argIndex, Float.parseFloat(argValue));
            } //end of try
        } catch (NumberFormatException numberFormat) {
            System.out.println("DEBUG: NumberFormat Error");
             if (argSo.getFieldClass(argIndex) == Double.class
                || argSo.getFieldClass(argIndex) == Float.class) {
                argSo.set(argIndex, 0.0);
            } else if (argSo.getFieldClass(argIndex) == Integer.class) {
                argSo.set(argIndex, 0);
            }
        } // end catch
    } // set() method
    
    /**
     * A utility method to compare two objects, both of type argClass and see if they have equal content or not.
     * @param argFirst First Object.
     * @param argSecond Second Object.
     * @param argClass The type.
     * @return 0 if they are equal, some other value otherwise.
     */
    public static int compare(Object argFirst, Object argSecond, Class argClass) {
        // if argFirst and argSecond references refer to the same object, return 0
        if (argFirst == argSecond)
            return 0;
        
        // else, make comparison for each type...
        if (argClass == Byte.class) {
            Byte a = (Byte)argFirst;
            Byte b = (Byte)argSecond;
            return a.compareTo(b);
        }
        if (argClass == Short.class) {
            Short a = (Short)argFirst;
            Short b = (Short)argSecond;
            return a.compareTo(b);
        }
        if (argClass == Integer.class) {
            Integer a = (Integer)argFirst;
            Integer b = (Integer)argSecond;
            return a.compareTo(b);
        }
        if (argClass == Float.class) {
            Float a = (Float)argFirst;
            Float b = (Float)argSecond;
            return a.compareTo(b);
        }
        if (argClass == Double.class) {
            Double a = (Double)argFirst;
            Double b = (Double)argSecond;
            return a.compareTo(b);
        }
        if (argClass == Date.class) {
            Date a = (Date)argFirst;
            Date b = (Date)argSecond;
            return a.compareTo(b);
        }
        
        // fall back to using Strings...
        return argFirst.toString().compareTo(argSecond.toString());
    } // compare() method
    
} // Utility class

// $Id$
