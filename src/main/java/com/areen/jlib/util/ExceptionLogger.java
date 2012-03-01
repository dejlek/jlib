/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.util;

import com.areen.jlib.gui.err.ExceptionUtility;
import org.apache.log4j.Logger;

/**
 *
 * @author dejan
 */
public class ExceptionLogger implements Thread.UncaughtExceptionHandler {
    private Logger logger;

    public ExceptionLogger(String argPackage) {
        if (argPackage == null) {
            throw new NullPointerException("Package argument to the ExceptionLogger is a NULL.");
        } else {
            logger = Logger.getLogger(argPackage);
        }
    } // ExceptionLogger constructor

    @Override
    public void uncaughtException(Thread argThread, Throwable argThrowable) {
        System.out.println("You crashed thread " + argThread.getName());
        System.out.println("Exception was: " + argThrowable.toString());
        
        /*
        ExceptionDialog ed = new ExceptionDialog(null, true);
        ed.setInfo(argThrowable.toString() + " Thread: " + argThread.getName());
        ed.setExceptionText(argThrowable.getMessage());
        ed.setVisible(true);
        * 
        */
        if (argThrowable instanceof Exception) {
            final String eof = System.getProperty("line.separator");
            Exception ex = (Exception) argThrowable;
            String header = ex + "  Thread: " + argThread.getName() + eof 
                        + ":::::::::::::::::::::::::::::::::" + eof;
            ExceptionUtility.exceptionDialog(null, ex, argThrowable.toString() + " Thread: " 
                + argThread.getName());
            
            if (logger.isDebugEnabled()) {
                logger.debug(header + ExceptionUtility.getStackTrace(ex.getStackTrace()));
            }
        }
    } // uncaughtException() method
    
} // ExceptionLogger class
