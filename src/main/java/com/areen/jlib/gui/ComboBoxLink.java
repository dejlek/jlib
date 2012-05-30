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

package com.areen.jlib.gui;

import com.areen.jlib.tuple.Pair;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * Use this class to update a JComponent, typically a JLabel or JTextArea whenever use selects something
 * in a JComboBox object.
 * 
 * @author dejan
 */
public class ComboBoxLink implements ActionListener {
    enum Type {
        LABEL,
        TEXT_AREA
    } // Type enumeration
    private Type type;
    private JComponent valueComponent;
    private JComboBox comboBox;
    private Object selectedItem;
    private String text;

    /**
     * The constructor when we have a JLable to link to.
     * 
     * @param argComboBox
     * @param argLabel 
     */
    public ComboBoxLink(JComboBox argComboBox, JLabel argLabel) {
        comboBox = argComboBox;
        valueComponent = argLabel;
        type = Type.LABEL;
        selectedItem = comboBox.getSelectedItem();
        update();
        argLabel.setText(text);
        comboBox.addActionListener(this);
    } // ComboBoxLink constructor
    
    /**
     * 
     * @param argComboBox
     * @param argArea
     */
    public ComboBoxLink(JComboBox argComboBox, JTextArea argArea) {
        comboBox = argComboBox;
        valueComponent = argArea;
        type = Type.TEXT_AREA;
        selectedItem = comboBox.getSelectedItem();
        update();
        argArea.setText(text);
        comboBox.addActionListener(this);
    } // ComboBoxLink constructor
    
    /**
     * This method updates internal String "text" with the text that may be the text for the Label.
     * 
     * Note: when user presses enter, for some reason we always get a String instead of the actual object.
     * Therefore we use the selectedItem reference and modify it when we navigate through the list. When
     * user picks, then we use the object selectedItem refers to.
     */
    private void update() {
        // When user presses enter, the index of the selected item is -1...
        // However, when picks an item with the mouse, index is always greater or equal to 0.
        if (comboBox.getSelectedIndex() == -1) {
            return;
        } // if
        selectedItem = comboBox.getSelectedItem();
        System.out.println("update(): selected:" + selectedItem.toString());

        if (selectedItem instanceof Pair) {
            text = ((Pair) selectedItem).getSecond().toString();
        } else {
            text = selectedItem.toString();
        } // else
    } // update() method
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        System.out.println("---------------------------------------" + ae.getActionCommand());
        update();

        Boolean tmp = (Boolean) comboBox.getClientProperty("item-picked");
        System.out.println("VAL:" + tmp.booleanValue());
        boolean itemPicked = false;
        if (tmp == null) {
            itemPicked = false;
        } else {
            itemPicked = tmp.booleanValue();
        } // else
        
        if (ae.getActionCommand().equals("comboBoxEdited")) {
            switch(type) {
                case LABEL:
                    JLabel label = (JLabel) valueComponent;
                    label.setText(text);
                    
                    break;
                case TEXT_AREA:
                    JTextArea area = (JTextArea) valueComponent;
                    area.setText(text);
                    break;
                default:
                    // nothing
            } // switch
        } else if (ae.getActionCommand().equals("comboBoxChanged") && itemPicked) {
            switch(type) {
                case LABEL:
                    JLabel label = (JLabel) valueComponent;
                    label.setText(text);
                    label.repaint();
                    System.out.println("label changed: " + text);
                    break;
                case TEXT_AREA:
                    JTextArea area = (JTextArea) valueComponent;
                    area.setText(text);
                    break;
                default:
                    // nothing
            } // switch
        } // else if
    } // actionPerformed() method
    
} // ComboBoxLink class

// $Id$
