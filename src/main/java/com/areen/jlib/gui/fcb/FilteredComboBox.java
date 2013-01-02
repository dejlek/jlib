/**
 * $Id$
 *
 * Copyright (c) 2009-2013 Areen Design Services Ltd
 * 23 Eyot Gardens; London; W6 9TR
 * http://www.areen.com
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of
 * Areen Design Services Ltd ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Areen Design Services Ltd.
 * 
 * This file is best viewed with 110 columns.
12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 * 
 * Author(s) in chronological order:
 *   Dejan Lekic , http://dejan.lekic.org
 * Contributor(s):
 *   -
 */

package com.areen.jlib.gui.fcb;

import com.areen.jlib.gui.WideComboBox;

/**
 * This is a specialised WideComboBox that deals only with FilteredComboBoxModel(s). It also installs
 * the ComboBoxFilter so developers does not have to do it (her/him)self.
 * 
 * @author dejan
 */
public class FilteredComboBox extends WideComboBox {
    
    // ====================================================================================================
    // ==== Variables =====================================================================================
    // ====================================================================================================
    
    // :::::: PRIVATE/PROTECTED ::::::
    private FilteredComboBoxModel comboBoxModel;
    private ComboBoxFilter comboBoxFilter;
   
    // ====================================================================================================
    // ==== Constructors ==================================================================================
    // ====================================================================================================
    
    /**
     * 
     * @param argModel
     */
    public FilteredComboBox(FilteredComboBoxModel argModel) {
        super(argModel);
        comboBoxModel = argModel;
        comboBoxFilter = new ComboBoxFilter(this, comboBoxModel);
    } // FilteredComboBox() method
    
    // ====================================================================================================
    // ==== Interface/Superclass ==========================================================================
    // ====================================================================================================

    /**
     * Use this method to specify (programmatically) what Item is going to be the picked item.
     * @param argObject 
     */
    @Override
    public void setSelectedItem(Object argObject) {
        if (comboBoxModel.isReadyToPick()) {
            comboBoxFilter.pickItem(argObject);
            
            // DO NOT FORGET to return to the FALSE state here, otherwise combo-box will keep picking items!
            comboBoxModel.setReadyToPick(false);
        } // if
        super.setSelectedItem(argObject);
    } // setSelectedItem() method
    
    /**
     * Use this method to specify (programmatically) what Item is going to be the picked item.
     * @param argIndex 
     */
    @Override
    public void setSelectedIndex(int argIndex) {
        super.setSelectedIndex(argIndex);
        if (comboBoxModel.isReadyToPick()) {
            comboBoxFilter.pickItem(comboBoxModel.getSelectedItem());
            
            // DO NOT FORGET to return to the FALSE state here, otherwise combo-box will keep picking items!
            comboBoxModel.setReadyToPick(false);
        } // if
    } // setSelectedItem() method
    
    // Throws null pointer exception, comboboxfilter is  NULL.
    // DL && SN discussed and commented.
    /**
    @Override
    public void setModel(ComboBoxModel argModel) {
        super.setModel(argModel);
        if (argModel instanceof FilteredComboBoxModel) {
            comboBoxFilter.setModel((FilteredComboBoxModel) argModel);
            System.out.println("sdsdsdsd");
        }
    }
    **/
    // ====================================================================================================
    // ==== Public Methods ================================================================================
    // ====================================================================================================

    /**
     * 
     * @return
     */
    public Object[] getSelectedItems() {
        Object[] ret = null;
        if (isMultiSelectionAllowed() 
                && (getModel() instanceof FilteredComboBoxModel)) {
            FilteredComboBoxModel fcbm = (FilteredComboBoxModel) getModel();
            return fcbm.getMatchingItems();
        } // if
        return ret;
    } // getSelectedItems() method

    /**
     * Use this method to get an array of matching items. In the case 
     * @return 
     */
    public Object[] getMatchingItems() {
        return comboBoxModel.getMatchingItems();
    } // getMatchingItems() method
    
    // ====================================================================================================
    // ==== Accessors =====================================================================================
    // ====================================================================================================

    /**
     * 
     * @return
     */
    public boolean isMultiSelectionAllowed() {
        return comboBoxModel.isMultiSelectionAllowed();
    } // isMultiSelectionAllowed() method

    /**
     * Set to TRUE when we want to trigger multiple matching. In this case FilteredComboBox model will 
     * maintain an internal list of matching items that can be obtained with getMatchingItems().
     * @param argMultiSelectionAllowed 
     * @see getMatchingItems()
     */
    public void setMultiSelectionAllowed(boolean argMultiSelectionAllowed) {
        comboBoxModel.setMultiSelectionAllowed(argMultiSelectionAllowed);
    } // setMultiSelectionAllowed() method

    /**
     * 
     * @return
     */
    public String getLastPattern() {
        return comboBoxModel.getLastPattern();
    } // getLastPattern() method

    /**
     * 
     * @return
     */
    public boolean isAnyPatternAllowed() {
        return comboBoxModel.isAnyPatternAllowed();
    } // isAnyPatternAllowed() method

    /**
     * Set to TRUE when we want ComboBoxFilter to retain the entered string even if we found no matches.
     * @param argAnyPatternAllowed 
     */
    public void setAnyPatternAllowed(boolean argAnyPatternAllowed) {
        comboBoxModel.setAnyPatternAllowed(argAnyPatternAllowed);
    } // setAnyPatternAllowed() method
    
    /**
     * 
     * @return
     */
    public Object getPickedItem() {
        return comboBoxModel.getPickedItem();
    } // getPickedItem() method
    
    /**
     * 
     * @param argPickedItem
     */
    public void setPickedItem(Object argPickedItem) {
        comboBoxModel.setPickedItem(argPickedItem);
    } // setPickedItem() method

    /**
     * 
     * @return
     */
    public Object getPickedKey() {
        return comboBoxModel.getPickedKey();
    } // getPickedKey() method

    /**
     * 
     * @param argPickedKey
     */
    public void setPickedKey(Object argPickedKey) {
        comboBoxModel.setPickedKey(argPickedKey);
    } // setPickedKey() method
    
    /**
     * 
     * @return comboBoxFilter.
     */
    public ComboBoxFilter getComboBoxFilter() {
        return comboBoxFilter;
    } // getComboBoxFilter().
    
    
    // ====================================================================================================
    // ==== Private Methods ===============================================================================
    // ====================================================================================================
    
    // None

} // FilteredComboBox class

// $Id$
