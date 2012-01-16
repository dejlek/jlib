/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.gui.fcb;

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
    
    public FilteredComboBoxCellEditor(JComboBox argComboBox) {
        comboBox = argComboBox;
        comboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        // hitting enter in the combo box should stop cellediting (see below)
        comboBox.addActionListener(this);
        // remove the editor's border - the cell itself already has one
        ((JComponent) comboBox.getEditor().getEditorComponent()).setBorder(null);
    }
    
    private void setValue(Object value) {
        comboBox.setSelectedItem(value);
    }
    
    // Implementing ActionListener
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        // Selecting an item results in an actioncommand "comboBoxChanged".
        // We should ignore these ones.
        
        // Hitting enter results in an actioncommand "comboBoxEdited"
        if (e.getActionCommand().equals("comboBoxEdited")) {
            stopCellEditing();
        }
    }
    
    // Implementing CellEditor
    @Override
    public Object getCellEditorValue() {
        System.out.println("FilteredComboBoxCellEditor.getCellEditorValue()");
        System.out.println(" `--> " + comboBox.getSelectedItem().getClass().getCanonicalName());
        return comboBox.getSelectedItem();
    }
    
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