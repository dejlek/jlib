/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author dejan
 */
public class ComboBoxLink implements ActionListener {
    enum Type {
        LABEL,
        TEXT_AREA
    }
    private Type type;
    private JComponent valueComponent;
    private JComboBox comboBox;
    private Object selectedItem;
    private String text;
    
    public ComboBoxLink(JComboBox argComboBox, JLabel argLabel) {
        comboBox = argComboBox;
        valueComponent = argLabel;
        type = Type.LABEL;
        selectedItem = comboBox.getSelectedItem();
        update();
        comboBox.addActionListener(this);
    } // ComboBoxLink constructor
    
    public ComboBoxLink(JComboBox argComboBox, JTextArea argArea) {
        comboBox = argComboBox;
        valueComponent = argArea;
        type = Type.TEXT_AREA;
        selectedItem = comboBox.getSelectedItem();
        update();
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
        if (comboBox.getSelectedIndex() == -1) {
            return;
        }
        selectedItem = comboBox.getSelectedItem();
        
        if (selectedItem instanceof Pair) {
            text = ((Pair) selectedItem).getSecond().toString();
        } else {
            text = selectedItem.toString();
        }
    } // update() method
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("comboBoxChanged")) {
            update();
        }
        
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
        } // if
    } // actionPerformed() method
    
} // ComboBoxLink class
