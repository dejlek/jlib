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

    public SmallArrayException() {
        reason = "Reason: unknown";
    }

    public SmallArrayException(String argReason) {
        reason = argReason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String argReason) {
        this.reason = argReason;
    }

} // SmallArrayException class

// $Id$
