/**
 * $Id$
 *
 * Copyright (c) 2009-2012 Areen Design Services Ltd
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
 * This file is best viewed with 128 columns.
1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
 *
 * Author(s):
 *   Dejan Lekic , http://dejan.lekic.org
 * Contributor(s):
 *   -
 */

package com.areen.jlib.gui.err;

import java.awt.Frame;


/**
 *
 * @author dejan
 */
public class ExceptionUtility {

    /**
     * Call it via:
     * <pre>ExceptionUtility.exceptionDialog(myFrame, myException, Utility.getLineInf());</pre>
     * @param argFrame
     * @param argException
     * @param argLineInfo
     */
    public static void exceptionDialog(final Frame argFrame, final Exception argException,
                                       final String argLineInfo) {
        final String eof = System.getProperty("line.separator");
        ExceptionDialog ed = new ExceptionDialog(argFrame, true);
        String exceptionString = argException + eof + ":::::::::::::::::::::::::::::::::" + eof;
        ed.setInfo(argLineInfo);
        ed.setExceptionText(exceptionString + ExceptionUtility.getStackTrace(argException.getStackTrace()));
        ed.setLocationRelativeTo(argFrame);
        ed.setVisible(true);
    } // exceptionDialog() method
    
    /**
     * Use this method to form a nice stack-trace text out of StackTraceElement objects.
     * 
     * @param argStackTraceElements
     * @return 
     */
    public static String getStackTrace(StackTraceElement[] argStackTraceElements) {
        String ret = "";
        final String eof = System.getProperty("line.separator");
        for (StackTraceElement el : argStackTraceElements) {
            ret += el.toString() + eof;
        }
        return ret;
    } // getStackTrace() method
    
} // ExceptionUtility class

// $Id$
