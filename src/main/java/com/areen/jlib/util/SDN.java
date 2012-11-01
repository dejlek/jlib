/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.util;

/**
 * SDN:
 *   Structured Data Notation
 *   Simple Data Notation
 * 
 * History:
 * I have used this notation long before I discovered s-expression notation. 
 * 
 * @author Dejan Lekic - http://dejan.lekic.org
 */
public class SDN {
    
    public static String indent(String argSDN) {
        StringBuilder sb = new StringBuilder();
        String eol = System.lineSeparator();
        int depth = 0;
        int start = 0;
        for (int i = 0; i < argSDN.length(); i++) {
            char ch = argSDN.charAt(i);
            switch (ch) {
                case '(':
                    sb.append(" ");
                    sb.append(eol);
                    sb.append(strdup("  ", depth));
                    sb.append(ch);
                    start = i;
                    ++depth;
                    break;
                    
                case ')':
                    --depth;
                    sb.append(eol);
                    sb.append(strdup("  ", depth));
                    sb.append(ch);
                    break;
                    
                default:
                    sb.append(ch);
            } // switch
        } // for
        return sb.toString();
    } // indent() method
    
    /**
     * Use this method when you need a String that is created by duplicating argString argTimes times.
     * @param argString
     * @param argTimes
     * @return 
     */
    private static String strdup(String argString, int argTimes) {
        StringBuilder sb = new StringBuilder();
        if (argTimes == 0) {
            return "";
        } else {
            for (int i = 0; i < argTimes; i++) {
                sb.append(argString);
            } // for
        } // else
        return sb.toString();
    } // strdup() method
            
    public static void main(String[] args) {
        String sdn = "(colman ( (cm_preset `Default` `Contains all available columns` `any-type` ((cg "
                + "`default` `Default group - contains all available columns.` true ` ( ) )) )(cm_preset "
                + "`Current` `Contains what is currently in the table` `any-type` ((cg `default` "
                + "`Default group - contains all available columns.` true ` ( ) )) )(cm_preset `Simple` "
                + "`Contains all available columns` `any_table_type` ((cg `default` "
                + "`Default group - contains all available columns.` true ` ( ) )(cg `Important` "
                + "`Columns we actually need` false ` ( ) )) )) )";
        System.out.println(SDN.indent(sdn));
    } // main() method
} // SDN class

// $Id$
