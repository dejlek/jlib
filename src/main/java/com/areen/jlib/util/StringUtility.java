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
     * @param argText Text to encode.
     * 
     * @return An encoded (safe URI) text.
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
    
     /**
     * Generate list of extensions to be used in file dialog as a filter.
     * It generates string *.ext1;*.ext2;(...)
     * @param extensions
     * @return 
     */
    public static String generateExtensionList(String[] extensions) {
        StringBuffer buffer = new StringBuffer();
        
        for (int i = 0; i < extensions.length; i++) {
            if (i > 0) {
                buffer.append(";");
            }
            buffer.append("*.");
            buffer.append(extensions[i]);
        }
      
        return new String(buffer);
    }
    
    /**
     * This method replaces strings $0 .. $9 with argValues[0] .. argValues[9].
     * 
     * If the argValues contains more than 10 elements, it ignores all elements with index greater than 9.
     * 
     * If one of argValues is null, it will be replaced with an empty set character ("\u2205").
     * 
     * @param argTemplate
     * @param argValues
     * @return 
     */
    public static String replaceVars(String argTemplate, Object... argValues) { 
        String ret = argTemplate;
        
        
        // we do it in reverse order so we do not get $18 replaced as $1 + 8 ...
        for (int i = argValues.length - 1; i >= 0; i--) {
            ret = ret.replaceAll("\\$" + i, argValues[i] == null ? "\u2205" : argValues[i].toString());
        } // for
        
        return ret;
    } // replaceVars() method
    
    /**
     * Dummy Rot13 encryption.
     * 
     * Example:
     * <pre>
     * System.out.println(rot13("helloTHERE123!")); // output: uryybGURER123!
     * System.out.println(rot13("uryybGURER123!")); // output: helloTHERE123!
     * </pre>
     * 
     * @param argInput
     * @return 
     */
    public static String rot13(String argInput) {
        String ret = "";
        for (int i = 0; i < argInput.length(); i++) {
            char ch = argInput.charAt(i);
            int dif = 0;
            if ((ch >= 'A' && ch <= 'M') || (ch >= 'a' && ch <= 'm')) {
                dif = 13;
            }
            if ((ch >= 'N' && ch <= 'Z') || (ch >= 'n' && ch <= 'z')) {
                dif = -13;
            }
            ch += dif;
            ret += ch;
        } // for
        return ret;
    } // rot13() method
    
    /**
     * Convert to camel-case string.
     * 
     * @param argString
     * @return 
     */
    public static String toCamelCase(String argString) {
        String[] parts = argString.split(" ");
        String camelCaseString = "";
        for (String part : parts) {
            String first = part.substring(0, 0).toUpperCase();
            String rest = "";
            if (part.length() > 1) {
                rest = part.substring(1, part.length() - 1);
            }
            String space = " ";
            if (camelCaseString.isEmpty()) {
                space = "";
            }
            camelCaseString += space + first + rest;
        } // foreach
        return camelCaseString;
    } // toCamelCase() method
    
    public static void main(String[] args) {
        //String docId = Integer.toString(12314, 36); // say document_id = 12314
        String docId = String.format("%08d", 12314);
        String major = String.format("%02d", 0);      // major = 0
        String minor = String.format("%03d", 12);     // minor = 12
        String docVer = docId + "." + major + "." + minor;
        System.out.println(docVer + "_" + safeUriPart("ORD/INV") + "_" 
                + safeUriPart("hello, john! /\\") + ".xls");
        System.out.println(replaceVars("$0 $1 $2 $3 $4 $5 $6 $7 $8 $9", 
                new Object[]{9, 8, 7, 6, 5, 4, 3, 2, 1, 0}));
        System.out.println(replaceVars("$0 $1 $2 $3 $4 $5 $6 $7 $8 $9", 
                new Integer(3)));
        System.out.println(replaceVars("$0 $1 $2 $3 $4 $5 $6 $7 $8 $9", 
                new Object[]{9, 8, 7, 6, 5, null, 3, 2, 1, 0}));
        System.out.println(replaceVars("$0 .. $17", 
                new Object[]{9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 8, 7, 6, 5, 4, 3, 2, 1, 0}));
        System.out.println();
        System.out.println(rot13("helloTHERE123!")); // output: uryybGURER123!
        System.out.println(rot13("uryybGURER123!")); // output: helloTHERE123!
        System.out.println(rot13("3vJs3Q6N!!"));
        System.out.println(rot13("StaffNetPrj"));
        System.out.println(rot13("FgnssArgCew"));
        System.out.println(rot13("3iWf3D6A!!"));
    }
    
} // StringUtility class

// $Id$
 