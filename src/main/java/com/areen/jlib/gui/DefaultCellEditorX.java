/**
 * Project: jlib
 * Version: $Id$
 * License: SPL
 * 
 * Authors (in chronological order):
 *   Dejan Lekic - http://dejan.lekic.org
 * 
 * Contributors (in chronological order):
 *   -
 */
package com.areen.jlib.gui;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * This class is taken from the following thread:
 * http://stackoverflow.com/questions/7873525
 * /what-is-the-best-way-to-trigger-a-combo-box-cell-editor-by-typing-in-a-jtable-ce
 * 
 * @author kleopatra
 */
public class DefaultCellEditorX extends DefaultCellEditor {
    private AncestorListener ancestorListener;
    private PropertyChangeListener focusPropertyListener;
    JComboBox comboBox;

    /**
     * 
     * @param argComboBox
     */
    public DefaultCellEditorX(JComboBox argComboBox) {
        super(argComboBox);
        comboBox = argComboBox;
        comboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        comboBox.setEditable(true);
        setClickCountToStart(2);
        getComponent().setName("Table.editor");
    } // DefaultCellEditorX() method

    /** 
     * Overridden to install an appriate listener which opens the
     * popup when actually starting an edit.
     * 
     * @inherited <p>
     */
    @Override
    public Component getTableCellEditorComponent(JTable table,
            Object value, boolean isSelected, int row, int column) {
        super.getTableCellEditorComponent(table, value, isSelected, row, column);
        installListener(table);
        return getComponent();
    } // getTableCellEditorComponent() method

    /**
     * Shows popup.
     */
    protected void showPopup() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                getComponent().setPopupVisible(true);
            } // run() method
        });
    } // showPopup() method


    /**
     * Dynamically install self-uninstalling listener, depending on JComboBox
     * and JTable state. 
     * @param table
     */
    private void installListener(JTable table) {
        if (getComponent().isEditable() && table.getSurrendersFocusOnKeystroke()) {
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
                    getComponent().removeAncestorListener(ancestorListener);
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
        getComponent().addAncestorListener(ancestorListener);
    } // installAncestorListener() method

    private void installKeyboardFocusListener() {
        if (focusPropertyListener == null) {
            focusPropertyListener = new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (focusManager().getPermanentFocusOwner() 
                            != getComponent().getEditor().getEditorComponent()) { 
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
     * Convenience for type cast.
     * @inherited <p>
     */
    @Override
    public JComboBox getComponent() {
        return (JComboBox) super.getComponent();
    } // getComponent() method

} // DefaultCellEditorX class

// $Id$
