/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.util;

/**
 *
 * @author dejan
 */
public interface ExceptionInfoSender {
    void setDescription(String argDescription);
    void setSubject(String argSubject);
    void send();
} // ExceptionInformationSender interface

// $Id$
