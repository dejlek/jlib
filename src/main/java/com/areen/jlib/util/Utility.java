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
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author dejan
 */
public class Utility {

    public static String getLineInfo() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getFileName() + ": Line " + ste.getLineNumber();
    } // getLineInfo() method

    /**
     * Function to export csv file
     * @param file
     * @param h1
     * @param h2
     * @param headings
     * @param data
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
                }
                i++;
            }

            //print the data
            Iterator<Vector<String>> di = data.iterator();
            Iterator<String> si = null;

            while (di.hasNext()) {
                //get iterator
                si = di.next().iterator();
                i = 0;
                while (si.hasNext()) {
                    ps.print(si.next());

                    if (i < headings.size() - 1) {
                        ps.print(",");
                    } else {
                        ps.println();
                    }

                    i++;
                }
            }

            ps.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // exportToCSV() method

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
} // Utility class

// $Id$
