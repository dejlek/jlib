/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.util;

/**
 *
 * @author Dejan
 */
public class MIMEUtil {
    private static MIMEType[] mimeTypes = {
        new MIMEType("application/3gpp-ims+xml", new String[]{}), 
        new MIMEType("application/activemessage", new String[]{}), 
        new MIMEType("application/andrew-inset", new String[]{"ez"})
    };
    
    public static MIMEType getMIMEType(String argMIMEType) {
        for (MIMEType mt : mimeTypes) {
            if (mt.type.equalsIgnoreCase(argMIMEType)) {
                return mt;
            }
        }
        return null;
    }
    
    public static String getIcon(MIMEType argMT, String argExtension) {
        String tmp = argMT.type.replace("/", "_");
        return tmp + "." + argExtension;
    }
    
    public static String getType(MIMEType argMT) {
        return argMT.type;
    }
    
    public static String[] getExtensions(MIMEType argMT) {
        return argMT.extensions;
    }
    
    public static class MIMEType {
        String type;
        String[] extensions;
        public MIMEType(String atype, String[] aexts) {
            type = atype;
            extensions = aexts;
        }
    } // MIMEType class
    
} // MIMEUtil class
