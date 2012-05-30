/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.exceptions;

/**
 *
 * @author dejan
 */
public class SmallArrayException extends Exception {
    private String reason;

    /**
     * 
     */
    public SmallArrayException() {
        reason = "Reason: unknown";
    } // SmallArrayException() method

    /**
     * 
     * @param argReason
     */
    public SmallArrayException(final String argReason) {
        reason = argReason;
    } // SmallArrayException() method

    /**
     * 
     * @return
     */
    public String getReason() {
        return reason;
    } // getReason() method

    /**
     * 
     * @param argReason
     */
    public void setReason(final String argReason) {
        this.reason = argReason;
    } // setReason() method

} // SmallArrayException class

// $Id$
