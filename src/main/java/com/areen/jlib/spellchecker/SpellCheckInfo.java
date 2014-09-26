/**
 * Project: jlib
 * Version: $Id$
 * License: SPL
 *
 * This file is best viewed with 110 columns.
1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
 *
 * Authors (in chronological order):
 *  Mateusz Dykiert
 * Contributors (in chronological order):
 *   -
 */

package com.areen.jlib.spellchecker;

/**
 *
 * @author Matthew
 */
public class SpellCheckInfo {
    /**
     * Array to hold start and end indexes of mis-spelled words. Length of the array must be
     * even.
     */
    int[] wordPositions;
    
    SpellCheckInfo() { }
    
    /**
     * Set positions of misspelled words. Array must be in the format
     * START IDX, FINISH IDX, START IDX, FINISH IDX, (...)
     * @param argPositionsArray 
     */
    public void setPositions(int[] argPositionsArray) {
        wordPositions = argPositionsArray; 
    }
    
    /**
     * Retrieve array of misspelled word positions
     * @return 
     */
    public int[] getPositions() {
        return wordPositions;
    }
    
    /**
     * If it has errors then either word positions are null or the array is empty
     * @return 
     */
    public boolean hasErrors() {
        if (wordPositions == null) {
            return false;
        } else {
            return wordPositions.length == 0;
        }
    }
}