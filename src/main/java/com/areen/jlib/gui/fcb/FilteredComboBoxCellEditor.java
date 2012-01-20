/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.gui.fcb;

import com.areen.jlib.gui.WideComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author dejan
 */
public class FilteredComboBoxCellEditor extends AbstractCellEditor 
        implements ActionListener, TableCellEditor, Serializable {
    
    private JComboBox comboBox;
    private FilteredComboBoxModel comboBoxModel;
    private ComboBoxFilter cbFilter;
    
    public FilteredComboBoxCellEditor(JComboBox argComboBox) {
        comboBox = argComboBox;
        comboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        // hitting enter in the combo box should stop cellediting (see below)
        comboBox.addActionListener(this);
        // remove the editor's border - the cell itself already has one
        ((JComponent) comboBox.getEditor().getEditorComponent()).setBorder(null);
        // TODO: check if we actually do have this kind of model
        comboBoxModel = (FilteredComboBoxModel) comboBox.getModel();
        comboBox.setRenderer(new FilteredComboBoxCellRenderer(comboBoxModel));
        cbFilter = new ComboBoxFilter(comboBox, comboBoxModel);
    }
    
    /**
     * A convenient constructor that does almost all we have to do. Just give it a model, and it will do
     * everything for you.
     * 
     * @param argModel 
     */
    public FilteredComboBoxCellEditor(FilteredComboBoxModel argModel) {
        comboBox = new WideComboBox();
        comboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        // remove the editor's border - the cell itself already has one
        ((JComponent) comboBox.getEditor().getEditorComponent()).setBorder(null);
        comboBoxModel = argModel;
        comboBox.setRenderer(new FilteredComboBoxCellRenderer(comboBoxModel));
        cbFilter = new ComboBoxFilter(comboBox, comboBoxModel);
        // hitting enter in the combo box should stop cellediting (see below)
        comboBox.addActionListener(this);
    } // FilteredComboBoxCellEditor constructor
    
    private void setValue(Object value) {
        cbFilter.prepare(value);
        //comboBox.setSelectedItem(value);
    }
    
    /**
     * {@inheritDoc}
     * @param e 
     */
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        // Selecting an item results in an actioncommand "comboBoxChanged".
        // We should ignore these ones, but unfortunately we must allow user to finish the editing by picking
        // an item with the mouse...
        //if (comboBoxModel.isReadyToFinish() && "comboBoxChanged".equals(e.getActionCommand())) {
        //    stopCellEditing();
        //    return;
        //}
        
        // Hitting enter results in an actioncommand "comboBoxEdited"
        if (e.getActionCommand().equals("comboBoxEdited")) {
            stopCellEditing();
        }
    } // actionPerformed() method
    
    // Implementing CellEditor
    @Override
    public Object getCellEditorValue() {
        //System.out.println("FilteredComboBoxCellEditor.getCellEditorValue()");
        //System.out.println(" `--> " + comboBox.getSelectedItem().getClass().getCanonicalName());
        Object obj = comboBox.getSelectedItem();
        if (obj.getClass().isPrimitive() || (obj.getClass() == String.class)) {
            return obj;
        } else {
            Object key = comboBoxModel.getKeyOfTheSelectedItem();
            return key;
        } // else
    } // getCellEditorValue() method
    
    @Override
    public boolean stopCellEditing() {
        if (comboBox.isEditable()) {
            // Notify the combo box that editing has stopped (e.g. User pressed F2)
            comboBox.actionPerformed(new ActionEvent(this, 0, ""));
        }
        fireEditingStopped();
        return true;
    }
    
    // Implementing TableCellEditor
    @Override
    public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table, 
            Object value, 
            boolean isSelected, 
            int row, 
            int column) {
        setValue(value);
        return comboBox;
    } // getTableCellEditorComponent() method
    
} // FilteredComboBoxCellEditor

// $Id$
