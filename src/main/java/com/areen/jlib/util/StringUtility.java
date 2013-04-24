/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.util;

/**
 * Various (static) methods for all kind of String manipulation.
 * 
 * @author Dejan
 */
public class StringUtility {
    
    /**
     * Get a "proper" String object that contains a commonly accepted format for user e-mail in the form
     * "Fname Sname <email@example.com>" .
     * 
     * @param argFullName
     * @param argEmail
     * @return 
     */
    public static String toEmailString(String argFullName, String argEmail) {
        return argFullName + " <" + argEmail + ">";
    } // toEmailString() method
    
    /**
     * Same as the method above, however here we expect argData to be in the form:
     * "Fname Sname, email@example.com"
     * 
     * @param argData
     * @return 
     */
    public static String toEmailString(String argData) {
        if (argData.contains(",")) {
            String[] parts = argData.split(",");
            return toEmailString(parts[0].trim(), parts[1].trim());
        } else {
            return argData;
        }
    } // toEmailString() method
    
} // StringUtility class

// $Id$
 