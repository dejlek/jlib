/**
 * Project: jlib
 * Version: $Id$
 * License: SPL
 * 
 * Authors (in chronological order):
 *   Dejan Lekic - http://dejan.lekic.org
 * Contributors (in chronological order):
 *   -
 */
package com.areen.jlib.sql;

import java.sql.ResultSet;
import java.sql.PreparedStatement;

/**
 *
 * @author Dejan
 */
public class SQLUtil {
    /**
     * Closes all give ResultSet objects.
     * @param argResultSets
     * @throws Exception 
     */
    public static void closeRS(ResultSet... argResultSets) throws Exception {
        for (ResultSet rs : argResultSets) {
            rs.close();
        } // foreach
    } // closeRS() method
    
    /**
     * Closes all given PreparedStatement objects.
     * 
     * @param argPreparedStatements
     * @throws Exception 
     */
    public static void closePS(PreparedStatement... argPreparedStatements) throws Exception {
        for (PreparedStatement ps : argPreparedStatements) {
            ps.close();
        } // foreach
    } // closePS() method
    
} // SQLUtil class

// $Id$
