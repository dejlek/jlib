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
 *   Mehjabeen Nujurally
 */

package com.areen.jlib.gui;

import com.areen.jlib.gui.form.ifp.CodePair;
import com.areen.jlib.tuple.Pair;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Use this class to update a JComponent, typically a JLabel or JTextField or JTextArea whenever use selects 
 * something in a JComboBox object.
 * 
 * @author mehjabeen
 */
public class ComboBoxMultiSelectionLink implements ActionListener {
    enum Type {
        LABEL,
        TEXT_FIELD,
        TEXT_AREA
    } // Type enumeration
    private Type type;
    private JComponent valueComponent;
    private JComboBox comboBox;
    private JButton carryOverButton;
    private boolean isCarryOverControlled = false;
    private Object selectedItem;
    private String text;
    private String multiSelectText;
    private String delimiter;
    private ArrayList<Object> selectedItems;

    /**
     * The constructor when we have a JLable to link to.
     * 
     * @param argComboBox
     * @param argLabel 
     */
    public ComboBoxMultiSelectionLink(JComboBox argComboBox, JLabel argLabel) {
        comboBox = argComboBox;
        valueComponent = argLabel;
        type = Type.LABEL;
        update();
        if (multiSelectText != null) {
            argLabel.setText(multiSelectText);
        } // if
        comboBox.addActionListener(this);
    } // ComboBoxLink constructor
    
    /**
     * The constructor when we have a JTextField to link to.
     * 
     * @param argComboBox
     * @param argField 
     */
    public ComboBoxMultiSelectionLink(JComboBox argComboBox, JTextField argField) {
        comboBox = argComboBox;
        valueComponent = argField;
        type = Type.TEXT_FIELD;
        update();
        if (multiSelectText != null) {
            argField.setText(multiSelectText);
        } // if
        comboBox.addActionListener(this);
    } // ComboBoxLink constructor
    
    /**
     * The constructor when we have a JTextArea to link to.
     * 
     * @param argComboBox
     * @param argArea
     */
    public ComboBoxMultiSelectionLink(JComboBox argComboBox, JTextArea argArea) {
        comboBox = argComboBox;
        valueComponent = argArea;
        type = Type.TEXT_AREA;
        update();
        if (multiSelectText != null) {
            argArea.setText(multiSelectText);
        } // if
        updateToolTip();
        comboBox.addActionListener(this);
    } // ComboBoxLink constructor
    
    /**
     * The constructor when we have a JLable to link to, with a given Button to carry across selections 
     * made from the given combo.
     * 
     * @param argComboBox
     * @param argButton 
     * @param argLabel 
     */
    public ComboBoxMultiSelectionLink(JComboBox argComboBox, JButton argButton, JLabel argLabel) {
        comboBox = argComboBox;
        valueComponent = argLabel;
        type = Type.LABEL;
        update();
        if (multiSelectText != null) {
            argLabel.setText(multiSelectText);
        } // if
        updateToolTip();
        carryOverButton = argButton;
        if (carryOverButton != null) {
            isCarryOverControlled = true;
            carryOverButton.addActionListener(this);
        } // if
    } // ComboBoxLink constructor
    
    /**
     * The constructor when we have a JTextField to link to, with a given Button to carry across selections 
     * made from the given combo.
     * 
     * @param argComboBox
     * @param argButton
     * @param argField  
     */
    public ComboBoxMultiSelectionLink(JComboBox argComboBox, JButton argButton, JTextField argField) {
        comboBox = argComboBox;
        valueComponent = argField;
        type = Type.TEXT_FIELD;
        update();
        if (multiSelectText != null) {
            argField.setText(multiSelectText);
        } // if
        updateToolTip();
        carryOverButton = argButton;
        if (carryOverButton != null) {
            isCarryOverControlled = true;
            carryOverButton.addActionListener(this);
        } // if
    } // ComboBoxLink constructor
    
    /**
     * The constructor when we have a JTextArea to link to, with a given Button to carry across selections 
     * made from the given combo.
     * 
     * @param argComboBox
     * @param argButton 
     * @param argArea
     */
    public ComboBoxMultiSelectionLink(JComboBox argComboBox, JButton argButton, JTextArea argArea) {
        comboBox = argComboBox;
        valueComponent = argArea;
        type = Type.TEXT_AREA;
        update();
        if (multiSelectText != null) {
            argArea.setText(multiSelectText);
        } // if
        updateToolTip();
        carryOverButton = argButton;
        if (carryOverButton != null) {
            isCarryOverControlled = true;
            carryOverButton.addActionListener(this);
        } // if
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
        System.out.println("comboBox.getSelectedIndex(): " + comboBox.getSelectedIndex());
        if (comboBox.getSelectedIndex() == -1 && !isCarryOverControlled) {
            return;
        } // if
        selectedItem = comboBox.getSelectedItem();
        System.out.println("update(): selected:" + selectedItem);
        if (selectedItem != null) {
            //
            System.out.println("update(): selected String:" + selectedItem.toString());

            if (selectedItem instanceof Pair) {
                text = ((Pair) selectedItem).getFirst().toString();
            } else if (selectedItem instanceof CodePair) {
                text = ((CodePair) selectedItem).getFirst().toString();
            } else {
                text = selectedItem.toString();
            } // else
            
            if (selectedItems == null) {
                selectedItems = new ArrayList<Object>();
            } // if
            if (!selectedItems.contains(selectedItem)) {
                selectedItems.add(selectedItem);
                if (multiSelectText == null) {
                    multiSelectText = "";
                } // if
                multiSelectText += (selectedItems.size() >= 1 ? delimiter : "") + text;
            } // if
        } // if
        
    } // update() method
    
    private void updateToolTip() {
        String completeSelectionsToolTip = "<html>";
        if (selectedItems != null) {
            for (Object selectedItemObj : selectedItems) {
                if (selectedItemObj != null) {
                    System.out.println("update(): selectedItemObj String:" + selectedItemObj.toString());
                    if (selectedItemObj instanceof Pair) {
                        completeSelectionsToolTip += ((Pair) selectedItemObj).getFirst().toString() + " - " 
                                + ((Pair) selectedItemObj).getSecond().toString() + "<br>";
                    } else if (selectedItemObj instanceof CodePair) {
                        completeSelectionsToolTip += ((CodePair) selectedItemObj).getFirst().toString() 
                                + " - " + ((CodePair) selectedItemObj).getSecond().toString() + "<br>";
                    } else {
                        completeSelectionsToolTip += selectedItemObj.toString() + "<br>";
                    } // else
                } // if
            } // for
            completeSelectionsToolTip += "</html>";
            switch(type) {
                case LABEL:
                    JLabel label = (JLabel) valueComponent;
                    label.setToolTipText(completeSelectionsToolTip);
                    break;
                case TEXT_FIELD:
                    JTextField field = (JTextField) valueComponent;
                    field.setToolTipText(completeSelectionsToolTip);
                    break;
                case TEXT_AREA:
                    JTextArea area = (JTextArea) valueComponent;
                    area.setToolTipText(completeSelectionsToolTip);
                    break;
                default:
                    // nothing
            } // switch
        } // if
    } // updateToolTip() method

    public String getDelimiter() {
        return delimiter;
    } // getDelimiter() method

    public void setDelimiter(String argDelimiter) {
        this.delimiter = argDelimiter;
    } // setDelimiter() method

    public ArrayList<Object> getSelectedItems() {
        return selectedItems;
    } // getSelectedItems() method
    
    public String[] getMultiSelection() {
        String[] multiSelection = null;
        if (multiSelectText != null) {
            multiSelection = multiSelectText.split(delimiter);
        } // if
        return multiSelection;
    }
    
    /**
     * Creates a carry over button to act as middle processing for user selections from the combo. 
     * 
     * In the case the current carry over button is null this method initialises it and adds the action 
     * processing to it (instead of the combo box triggering actions)
     * 
     * @return carryOverButton
     */
    public JButton createCarryOverButton() {
        if (carryOverButton == null) {
            carryOverButton = new JButton(">");
            isCarryOverControlled = true;
            carryOverButton.addActionListener(this);
            if (comboBox != null) {
                boolean hasAl = false;
                ActionListener[] currentComboAL = comboBox.getActionListeners();
                if (currentComboAL != null) {
                    for (ActionListener al : currentComboAL) {
                        if (al.equals(this)) {
                            hasAl = true;
                            break;
                        } // if
                    } // for
                } // if
                if (hasAl) {
                    comboBox.removeActionListener(this);
                } // if
            } // if
        } // if
        return carryOverButton;
    } // createCarryOverButton() method
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        //System.out.println("---------------------------------------" + ae.getActionCommand());
        update();
        
        Boolean tmp = (Boolean) comboBox.getClientProperty("item-picked");
        //System.out.println("VAL:" + tmp.booleanValue());
        boolean itemPicked = false;
        if (tmp == null) {
            itemPicked = false;
        } else {
            itemPicked = tmp.booleanValue();
        } // else
        
        boolean applyText = isCarryOverControlled || ae.getActionCommand().equals("comboBoxEdited") 
                || (ae.getActionCommand().equals("comboBoxChanged") && itemPicked);
        if (applyText) {
            switch(type) {
                case LABEL:
                    JLabel label = (JLabel) valueComponent;
                    label.setText(multiSelectText);
                    label.repaint();
                    break;
                case TEXT_FIELD:
                    JTextField field = (JTextField) valueComponent;
                    field.setText(multiSelectText);
                    field.repaint();
                    break;
                case TEXT_AREA:
                    JTextArea area = (JTextArea) valueComponent;
                    area.setText(multiSelectText);
                    area.repaint();
                    break;
                default:
                    // nothing
            } // switch
        } // if
        updateToolTip();
    } // actionPerformed() method
    
} // ComboBoxMultiSelectionLink class

// $Id$
