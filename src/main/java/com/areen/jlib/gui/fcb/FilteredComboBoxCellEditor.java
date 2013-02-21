/**
 * Project: jlib
 * Version: $Id$
 * License: SPL
 *
 * This file is best viewed with 110 columns.
 * 34567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
 *
 * Authors (in chronological order):
 *   Dejan Lekic - http://dejan.lekic.org
 * Contributors (in chronological order):
 *   -
 */

package com.areen.jlib.gui.fcb;

import com.areen.jlib.gui.WideComboBox;
import com.areen.jlib.util.Sise;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.TableCellEditor;

/**
 * Part of this file is taken from the following thread:
 * http://stackoverflow.com/questions/7873525
 * /what-is-the-best-way-to-trigger-a-combo-box-cell-editor-by-typing-in-a-jtable-ce
 * 
 * Many thanks to kleopatra for the help.
 * 
 * @author dejan
 * @author kleopatra
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
    
    private AncestorListener ancestorListener;
    private PropertyChangeListener focusPropertyListener;
    private FilteredComboBoxCellRenderer listCellRenderer;
    private int config;
    
    /**
     * 
     * @param argComboBox
     */
    public FilteredComboBoxCellEditor(JComboBox argComboBox) {
        comboBox = argComboBox;
        comboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        // hitting enter in the combo box should stop cellediting (see below)
        comboBox.addActionListener(this);
        // remove the editor's border - the cell itself already has one
        ((JComponent) comboBox.getEditor().getEditorComponent()).setBorder(null);
        // TODO: check if we actually do have this kind of model
        comboBoxModel = (FilteredComboBoxModel) comboBox.getModel();
        listCellRenderer = new FilteredComboBoxCellRenderer(comboBoxModel);
        comboBox.setRenderer(listCellRenderer);
        cbFilter = new ComboBoxFilter(comboBox, comboBoxModel);
        comboBox.addActionListener(this);
        comboBox.setName("Table.editor");
    } // FilteredComboBoxCellEditor() method
    
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
        listCellRenderer = new FilteredComboBoxCellRenderer(comboBoxModel);
        comboBox.setRenderer(listCellRenderer);
        cbFilter = new ComboBoxFilter(comboBox, comboBoxModel);
        // hitting enter in the combo box should stop cellediting (see below)
        comboBox.addActionListener(this);
        comboBox.setName("Table.editor");
    } // FilteredComboBoxCellEditor constructor
    
    private void setValue(Object value) {
        cbFilter.prepare(value);
    } // setValue() method
    
    /**
     * {@inheritDoc}
     * @param e 
     */
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        System.out.println(e.getActionCommand());
        System.out.println(comboBoxModel.isReadyToFinish());
        System.out.println(comboBoxModel.isCancelled());
        // Selecting an item results in an actioncommand "comboBoxChanged".
        // We should ignore these ones, but unfortunately we must allow user to finish the editing by picking
        // an item with the mouse...
        if (comboBoxModel.isReadyToFinish() && "comboBoxChanged".equals(e.getActionCommand())) {
            stopCellEditing();
            return;
        } // if

        // Hitting enter results in an actioncommand "comboBoxEdited"
        if (e.getActionCommand().equals("comboBoxEdited")) {
            stopCellEditing();
            return;
        } // if

        if (comboBoxModel.isCancelled() && "comboBoxChanged".equals(e.getActionCommand())) {
            cancelCellEditing();
            //stopCellEditing();
            return;
        } // if

    } // actionPerformed() method
    
    // Implementing CellEditor
    @Override
    public Object getCellEditorValue() {
        return cbFilter.getPickedKey();
    } // getCellEditorValue() method
    
    @Override
    public boolean stopCellEditing() {
        if (comboBox.isEditable()) {
            // Notify the combo box that editing has stopped (e.g. User pressed F2)
            comboBox.actionPerformed(new ActionEvent(this, 0, ""));
        } // if
        fireEditingStopped();
        return true;
    } // stopCellEditing() method
    
    // Implementing TableCellEditor
    @Override
    public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table, 
            Object value, 
            boolean isSelected, 
            int row, 
            int column) {
        if ((row == keyRow)
                && column == keyColumn) {
            keyRow = -1;
            keyColumn = -1;
            cbFilter.setTriggeredByKeyPress(true);
            setValue(pressedKey + Sise.UNIT_SEPARATOR_STRING + value);
        } else {
            cbFilter.setTriggeredByKeyPress(false);
            setValue(value);
        } // else
        installListener(table);
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
    } // storeKeyInfo() method

    /**
     * Specifies the number of clicks needed to start editing.
     *
     * @param count  an int specifying the number of clicks needed to start editing
     * @see #getClickCountToStart
     */
    public void setClickCountToStart(int count) {
        clickCountToStart = count;
    } // setClickCountToStart() method

    /**
     * Returns the number of clicks needed to start editing.
     * @return the number of clicks needed to start editing
     */
    public int getClickCountToStart() {
        return clickCountToStart;
    } // getClickCountToStart() method

    public int getConfig() {
        return config;
    }

    public void setConfig(int argConfig) {
        config = argConfig;
    }

    public FilteredComboBoxCellRenderer getListCellRenderer() {
        return listCellRenderer;
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

    // == BEGIN ==============================================================================================
    // The following block of Java code is taken from the following thread:
    // http://stackoverflow.com/questions/7873525
    // /what-is-the-best-way-to-trigger-a-combo-box-cell-editor-by-typing-in-a-jtable-ce
    // The reason why we do this is to be able to drop-down the pop-up when user starts editing the cell.
    
    /**
     * Shows popup.
     */
    protected void showPopup() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (comboBox.isShowing()) {
                    comboBox.setPopupVisible(true);
                } // if
            } // run() method
        });
    } // showPopup() method

    /**
     * Dynamically install self-uninstalling listener, depending on JComboBox
     * and JTable state. 
     * @param table
     */
    private void installListener(JTable table) {
        if (comboBox.isEditable() && table.getSurrendersFocusOnKeystroke()) {
            installKeyboardFocusListener();
        } else {
            installAncestorListener();
        } // else
    } // installListener() method

    private void installAncestorListener() {
        if (ancestorListener == null) {
            ancestorListener = new AncestorListener() {

                @Override
                public void ancestorAdded(AncestorEvent event) {
                    comboBox.removeAncestorListener(ancestorListener);
                    showPopup();
                } // ancestorAdded() method

                @Override
                public void ancestorRemoved(AncestorEvent event) {
                } // ancestorRemoved() method

                @Override
                public void ancestorMoved(AncestorEvent event) {
                } // ancestorMoved() method

            };
        } // if
        comboBox.addAncestorListener(ancestorListener);
    } // installAncestorListener() method

    private void installKeyboardFocusListener() {
        if (focusPropertyListener == null) {
            focusPropertyListener = new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    //LOG.info("property: " + evt.getPropertyName());
                    System.out.println("property: " + evt.getPropertyName());
                    if (focusManager().getPermanentFocusOwner() 
                            != comboBox.getEditor().getEditorComponent()) { 
                        return;
                    } // if
                    focusManager().removePropertyChangeListener("permanentFocusOwner", focusPropertyListener);
                    showPopup();
                } // propertyChange() method
            };
        } // if
        focusManager().addPropertyChangeListener("permanentFocusOwner", focusPropertyListener);
    } // installKeyboardFocusListener() method

    /**
     * Convience for less typing.
     * @return
     */
    protected KeyboardFocusManager focusManager() {
        return KeyboardFocusManager.getCurrentKeyboardFocusManager();
    } // focusManager() method
    
    /**
     * Returns the Combobox Model.
     * 
     * @return comboBoxModel.
     */
    public ComboBoxModel getComboBoxModel() {
        return comboBoxModel;
    } // getComboBoxModel() method
    
    // ================================================================================================ END ==
    
} // FilteredComboBoxCellEditor

// $Id$
