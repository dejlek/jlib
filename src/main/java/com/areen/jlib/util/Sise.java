/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.util;

import java.util.Arrays;

/**
 * SImple SErializer.
 * 
 * A very simple separator which uses ancient ASII chars to separate files, groups, records and units.
 * 
 * File separator char:   0x1c
 * Group separator char:  0x1d
 * Record separator char: 0x1e
 * Unit separator char:   0x1f
 * 
 * Let's use a LISP notation to visually describe the format:
 * 
 * (file1 (group1 (record1 val1 US val2 US val3))) where "US" represents the unit separator character.
 * So, a record containing "UK", "United Kingdom", "GBP" values is stored as a String:
 * "UK\u001fUnitedKingdom\u001fGBP
 * 
 * For now we deal with Strings only. In the future we may switch to char[] arrays...
 * 
 * @author dejan
 */
public class Sise {
    public static final char   FS = 0x1c;
    public static final char   FILE_SEPARATOR = 0x1c;
    public static final String FS_STRING = "\u001c";
    public static final String FILE_SEPARATOR_STRING = "\u001c";
    
    public static final char   GS = 0x1d;
    public static final char   GROUP_SEPARATOR = 0x1d;
    public static final String GS_STRING = "\u001d";
    public static final String GROUP_SEPARATOR_STRING = "\u001d";
    
    public static final char   RS = 0x1e;
    public static final char   RECORD_SEPARATOR = 0x1e;
    public static final String RS_STRING = "\u001e";
    public static final String RECORD_SEPARATOR_STRING = "\u001e";
    
    public static final char   US = 0x1f;
    public static final char   UNIT_SEPARATOR = 0x1f;
    public static final String US_STRING = "\u001f";
    public static final String UNIT_SEPARATOR_STRING = "\u001f";
    
    
    /**
     * Make a RECORD String out of Object[] array.
     * 
     * NOTE: We assume none of the given objects won't have separator chars when converted to Strings...
     * 
     * @param argObjects
     * @return 
     */
    public static String record(Object[] argObjects) {
        if (argObjects.length == 1) {
            return argObjects[0].toString();
        }
        String ret = argObjects[0].toString();
        for (int i = 1; i < argObjects.length; i++) {
            ret = ret + UNIT_SEPARATOR + argObjects[i].toString();
        }
        return ret;
    } // record() method
    
    /**
     * Extract units from a given record argRecord.
     * 
     * @param argRecord An SISE string containing a record.
     * @return A String[] array containing fields of the record.
     */
    public static String[] units(String argRecord) {
        String[] ret = argRecord.split(UNIT_SEPARATOR_STRING);
        return ret;
    }
    
    public static void main(String[] args) {
        Object[] objs = new Object[] { "kilo", "hecto", "mega", "giga", "tera" };
        
        // record()
        String rec = Sise.record(objs);
        System.out.println(rec);
        
        // units()
        System.out.println(Arrays.toString(Sise.units(rec)));
    } // main() method
} // Sise class

// $Id$
