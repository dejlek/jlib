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
 
    public Object[] getSelectedItems() {
        Object[] ret = null;
        if (isMultiSelectionAllowed() 
                && (getModel() instanceof FilteredComboBoxModel)) {
            FilteredComboBoxModel fcbm = (FilteredComboBoxModel) getModel();
            return fcbm.getMatchingItems();
        } // else
        return ret;
    }
    
    public boolean isMultipleSelectionTriggered() {
        return comboBoxModel.hasWildcard();
    }
    
    public String getLastPattern() {
        return comboBoxModel.getLastPattern();
    }
    
} // FilteredComboBox class

// $Id$
