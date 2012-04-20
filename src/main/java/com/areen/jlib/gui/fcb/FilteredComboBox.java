/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.gui.fcb;

import com.areen.jlib.gui.WideComboBox;

/**
 * This is a specialised WideComboBox that deals only with FilteredComboBoxModel(s). It also installs
 * the ComboBoxFilter so developers does not have to do it (her/him)self.
 * @author dejan
 */
public class FilteredComboBox extends WideComboBox {
    private FilteredComboBoxModel comboBoxModel;
    private ComboBoxFilter comboBoxFilter;
    
    public FilteredComboBox(FilteredComboBoxModel argModel) {
        super(argModel);
        comboBoxModel = argModel;
        comboBoxFilter = new ComboBoxFilter(this, comboBoxModel);
    }
    
    public boolean isMultiSelectionAllowed() {
        return comboBoxModel.isMultiSelectionAllowed();
    }

    public void setMultiSelectionAllowed(boolean argMultiSelectionAllowed) {
        comboBoxModel.setMultiSelectionAllowed(argMultiSelectionAllowed);
    }
 
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
    
    public Object[] getSelectedItems() {
        Object[] ret = null;
        if (isMultiSelectionAllowed() 
                && (getModel() instanceof FilteredComboBoxModel)) {
            FilteredComboBoxModel fcbm = (FilteredComboBoxModel) getModel();
            return fcbm.getMatchingItems();
        } // else
        return ret;
    }
        
    public String getLastPattern() {
        return comboBoxModel.getLastPattern();
    }

    public boolean isAnyPatternAllowed() {
        return comboBoxModel.isAnyPatternAllowed();
    }

    /**
     * Set to TRUE when we want ComboBoxFilter to retain the entered string even if we found no matches.
     * @param argAnyPatternAllowed 
     */
    public void setAnyPatternAllowed(boolean argAnyPatternAllowed) {
        comboBoxModel.setAnyPatternAllowed(argAnyPatternAllowed);
    }

} // FilteredComboBox class

// $Id$
