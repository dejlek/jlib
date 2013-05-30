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
    
    /**
     * This utility method is used to replace any character from the input string argText that is 
     * *not allowed* in URI with a tilde. Such resulting string may be used for building safe URIs, and/or
     * safe file-names that are human readable.
     * 
     * I've chosen tilde as replacement for any special character simply because we can easily detect that
     * there was some special character, and possibly fix it manually, if there is really the need for that.
     * 
     * The only exception is the space character that I decided to replace with an underscore. (Makes more
     * readable file-names).
     * 
     * To quote section 2.3 of RFC 3986:
     * 
     * "Characters that are allowed in a URI but do not have a reserved purpose are called unreserved. These 
     * include uppercase and lowercase letters, decimal digits, hyphen, period, underscore, and tilde."
     * 
     * Example:
     * safeUriPart("hello, john!") will return string "hello~_john~". We can safely create a file with
     * name, say "hello~_john~.dat"
     * 
     * @param argText
     * @return 
     */
    public static String safeUriPart(String argText) {
        String ret = "";
        
        for (char ch : argText.toCharArray()) {
            if (ch >= 'A' && ch <= 'Z') {
                ret += ch;
            } else if (ch >= 'a' && ch <= 'z') {
                ret += ch;
            } else if (ch >= '0' && ch <= '9') {
                ret += ch;
            } else if (ch == '.' || ch == '~' || ch == '-' || ch == '_') {
                ret += ch;
            } else if (ch == ' ') {
                ret += '_';
            } else {
                ret += '~';
            }
        } // foreach
        
        return ret;
    } // safeUriPart() method
    
    public static void main(String[] args) {
        //String docId = Integer.toString(12314, 36); // say document_id = 12314
        String docId = String.format("%08d", 12314);
        String major = String.format("%02d", 0);      // major = 0
        String minor = String.format("%03d", 12);     // minor = 12
        String docVer = docId + "." + major + "." + minor;
        System.out.println(docVer + "_" + safeUriPart("ORD/INV") + "_" 
                + safeUriPart("hello, john! /\\") + ".xls");
        System.out.println();
    }
    
} // StringUtility class

// $Id$
 