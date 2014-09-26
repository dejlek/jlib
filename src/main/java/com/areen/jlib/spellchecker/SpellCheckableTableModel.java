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
 * Interface which add functionality to the table model so we can set/get table columns which 
 * are to be spell checked.
 * 
 * @author Matthew
 */
public interface SpellCheckableTableModel {
    /**
     * Set column indexes to be spell checked in ASCENDING ORDER!
     */
    void setSpellCheckedColumns(int[] argColumnIndexes);
    
    /**
     * Get model indexes of the columns to be spell checked
     * @return 
     */
    int[] getSpellCheckedColumns();
    
    /**
     * Check if the column is to be spell checked (by column model index)
     * @param argColumnModelIndex
     * @return 
     */
    boolean isColumnSpellChecked(int argColumnModelIndex);
    
    /**
     * Check if the current row has any errors in spell checked columns
     * @param rowModel
     * @return 
     */
    boolean hasErrors(int rowModel);
    
    /**
     * Set spell check info for a cell (model indexes)
     * @param rowModelIdx
     * @param columnModelIdx
     * @param info
     * @return 
     */
    void setSpellCheckInfo(int rowModelIdx, int columnModelIdx, SpellCheckInfo info);
    
    /**
     * Get spell check info for particular cell
     * @param rowModelIdx
     * @param columnModelIdx
     * @return 
     */
    boolean getSpellCheckInfo(int rowModelIdx, int columnModelIdx);
}
