/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.gui.fcb;

import com.areen.jlib.gui.WideComboBox;
import com.areen.jlib.util.Sise;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.EventObject;
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

    private int clickCountToStart = 2;
    
    /** 
     * Here we store the column index of the cell where user pressed a key. 
     */
    private int keyColumn = -1;

    /**
     * Similarly to the keyRow, we store the row index as well.
     * @see keyRow
     */
    private int keyRow = -1;

    /**
     * We have to store the key that user pressed so in the case the cell editor is combo box, we can
     * forward pressed key (character), so we can display it inside combo-box's editor component.
     * 
     * @see keyRow, keyColumn
     */
    private char pressedKey;
    
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
    }
    
    /**
     * {@inheritDoc}
     * @param e 
     */
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
            System.out.println(e.getActionCommand());
        // Selecting an item results in an actioncommand "comboBoxChanged".
        // We should ignore these ones, but unfortunately we must allow user to finish the editing by picking
        // an item with the mouse...
        if (comboBoxModel.isReadyToFinish() && "comboBoxChanged".equals(e.getActionCommand())) {
            stopCellEditing();
            return;
        }
        
        // Hitting enter results in an actioncommand "comboBoxEdited"
        if (e.getActionCommand().equals("comboBoxEdited")) {
            stopCellEditing();
            return;
        }

        if (comboBoxModel.isCancelled() && "comboBoxChanged".equals(e.getActionCommand())) {
            stopCellEditing();
            return;
        } // if

    } // actionPerformed() method
    
    // Implementing CellEditor
    @Override
    public Object getCellEditorValue() {
        /*
        System.out.println("FilteredComboBoxCellEditor.getCellEditorValue()");
        System.out.println(" `--> " + comboBox.getSelectedItem().getClass().getCanonicalName());
        System.out.println("    > " + comboBox.getSelectedItem());
        System.out.println("    > " + comboBox.getSelectedIndex());
        System.out.println("    > " + comboBoxModel.getSelectedItem());
        System.out.println("    : " + cbFilter.getPickedItem());
        System.out.println("    : " + cbFilter.getPickedKey());
        Object obj = comboBox.getSelectedItem();
        
        if ((comboBox.getSelectedIndex() >= 0)
                && (obj.getClass().isPrimitive() || (obj.getClass() == String.class))) {
            return obj;
        } else {
            Object key = comboBoxModel.getKeyOfTheSelectedItem();
            return key;
        } // else
        *
        */
        return cbFilter.getPickedKey();
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
        
        /*
         * The following block is not needed anymore - it is handled by WideComboBox itself 
         
        if (comboBox instanceof WideComboBox) {
            WideComboBox wcb = (WideComboBox) comboBox;
            wcb.setMaxWidth(table.getSize().width * 3 / 4);
         
            System.out.println(wcb.getMaxWidth());
        }
        * 
        */
        
        if ((row == keyRow)
                && column == keyColumn) {
            keyRow = -1;
            keyColumn = -1;
            cbFilter.setTriggeredByKeyPress(true);
            setValue(pressedKey + Sise.UNIT_SEPARATOR_STRING + value);
        } else {
            cbFilter.setTriggeredByKeyPress(false);
            setValue(value);
        }
        return comboBox;
    } // getTableCellEditorComponent() method
    
    /**
     * This method should be called by a KeyListener installed on a JTable object to inform the cell editor
     * where user pressed a key before the editing was triggered (with key press). By doing this, cell
     * editor can take that key, and include it in the combo-box's editing component, otherwise it is lost.
     * 
     * @param argRow
     * @param argColumn
     * @param argChar 
     */
    public void storeKeyInfo(int argRow, int argColumn, char argChar) {
        keyRow = argRow;
        keyColumn = argColumn;
        pressedKey = argChar;
    } // storeKeyInfo

    /**
     * Specifies the number of clicks needed to start editing.
     *
     * @param count  an int specifying the number of clicks needed to start editing
     * @see #getClickCountToStart
     */
    public void setClickCountToStart(int count) {
        clickCountToStart = count;
    }

    /**
     * Returns the number of clicks needed to start editing.
     * @return the number of clicks needed to start editing
     */
    public int getClickCountToStart() {
        return clickCountToStart;
    }

    @Override
    public boolean isCellEditable(EventObject eo) {
        // PS. eo may be null, but the following block will work anyway... :)
        if (eo instanceof MouseEvent) {
            MouseEvent me = (MouseEvent) eo;
            return (me.getClickCount() >= clickCountToStart) 
                    && super.isCellEditable(eo);
        } // if
        return super.isCellEditable(eo);
    } // isCellEditable() method
    
} // FilteredComboBoxCellEditor

// $Id$
