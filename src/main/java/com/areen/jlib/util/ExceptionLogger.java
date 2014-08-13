/**
 * Project: jlib 
 * Version: $Id$ 
 * License: SPL
 *
 * This file is best viewed with 110 columns.
 * 34567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
 * 
 * Authors (in chronological order): 
 *   Dejan Lekic - http://dejan.lekic.org
 * Contributors (in chronological order): 
 *   -
 */

package com.areen.jlib.util;

import com.areen.jlib.gui.err.ExceptionDialog;
import com.areen.jlib.gui.err.ExceptionUtility;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This simple implementation of the Thread.UncaughtExceptionHandler will popup a dialog whenever an
 * uncaught exception is thrown.
 * 
 * Example:
 **********

Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogger("com.areen.jlib"));

 **********
 * @author dejan
 */
public class ExceptionLogger implements Thread.UncaughtExceptionHandler {
    private final Logger logger;
    private ExceptionInfoSender exceptionInfoSender;

    public ExceptionLogger(String argPackage) {
        if (argPackage == null) {
            logger = Logger.getLogger(ExceptionLogger.class.getCanonicalName());
        } else {
            logger = Logger.getLogger(argPackage);
        }
    } // ExceptionLogger constructor

    public void setExceptionInfoSender(ExceptionInfoSender argExceptionInfoSender) {
        exceptionInfoSender = argExceptionInfoSender;
    }
    
    @Override
    public void uncaughtException(Thread argThread, Throwable argThrowable) {
        if (argThrowable instanceof Exception) {
            
            final String eof = System.getProperty("line.separator");
            Exception ex = (Exception) argThrowable;
            String header = ex + "  Thread: " + argThread.getName() + eof 
                        + ":::::::::::::::::::::::::::::::::" + eof;

            JFrame frame = null; // parent frame
            
            // First we show a dialog
            Object[] options = { "More...", "OK"};
            int ret = JOptionPane.showOptionDialog(frame, 
                    "Unexpected error. \nClick on `More...` for more information.", 
                    "APC error", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE, 
                    null, 
                    options, 
                    options[1]);
            
            if (ret == 0) {
                // User pressed "More..." 
                ExceptionDialog ed = new ExceptionDialog(frame, true);
                String exceptionString = ex + eof + ":::::::::::::::::::::::::::::::::" + eof;
                String lineInfo = argThrowable.toString() + " Thread: " + argThread.getName();
                ed.setInfo(lineInfo);
                ed.setExceptionText(exceptionString + ExceptionUtility.getStackTrace(ex.getStackTrace()));
                ed.setLocationRelativeTo(frame);
                ed.setExceptionInfoSender(exceptionInfoSender);
                ed.setVisible(true);

                logger.severe(header + ExceptionUtility.getStackTrace(ex.getStackTrace()));
            } // if
        } // if
    } // uncaughtException() method
    
} // ExceptionLogger class

// $Id$
