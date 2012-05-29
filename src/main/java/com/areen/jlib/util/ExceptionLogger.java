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
import javax.swing.JFrame;
import org.apache.log4j.Logger;

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
    private Logger logger;
    private ExceptionInfoSender exceptionInfoSender;

    public ExceptionLogger(String argPackage) {
        if (argPackage == null) {
            throw new NullPointerException("Package argument to the ExceptionLogger is a NULL.");
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
            
            ExceptionDialog ed = new ExceptionDialog(frame, true);
            String exceptionString = ex + eof + ":::::::::::::::::::::::::::::::::" + eof;
            String lineInfo = argThrowable.toString() + " Thread: " + argThread.getName();
            ed.setInfo(lineInfo);
            ed.setExceptionText(exceptionString + ExceptionUtility.getStackTrace(ex.getStackTrace()));
            ed.setLocationRelativeTo(frame);
            ed.setExceptionInfoSender(exceptionInfoSender);
            ed.setVisible(true);
            
            if (logger.isDebugEnabled()) {
                logger.debug(header + ExceptionUtility.getStackTrace(ex.getStackTrace()));
            }
        }
    } // uncaughtException() method
    
} // ExceptionLogger class

// $Id$
