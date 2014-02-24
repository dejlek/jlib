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
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
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
    private String text;
    private String multiSelectText;
    private String delimiter;
    private Object selectedItem;
    private ArrayList<Object> selectedItems;
    private boolean isControlledByBoth = false;

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
        if (comboBox.getSelectedIndex() == -1 && (!isControlledByBoth && !isCarryOverControlled)) {
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
    
    /**
     * Responsible for updating the tool tip of the value component to provide a breakdown of all the 
     * selections made.
     */
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

    /**
     * Retrieval of the delimiter used for separating the selections made by the components of this link.
     * 
     * @return the string separator used between selections made
     */
    public String getDelimiter() {
        return delimiter;
    } // getDelimiter() method

    /**
     * Sets the delimiter to be used for separating the selections made by the components of this link.
     * @param argDelimiter
     */
    public void setDelimiter(String argDelimiter) {
        this.delimiter = argDelimiter;
    } // setDelimiter() method

    /**
     * Retrieval of all the selections made by the components of this link.
     * 
     * @return container of all the selections.
     */
    public ArrayList<Object> getSelectedItems() {
        return selectedItems;
    } // getSelectedItems() method
    
    /**
     * Retrieval of all the String (code) selections made by the components of this link.
     * 
     * @return container of all the String (code) selections.
     */
    public String[] getMultiSelection() {
        String[] multiSelection = null;
        if (multiSelectText != null) {
            multiSelection = multiSelectText.split(delimiter);
        } // if
        return multiSelection;
    } // getMultiSelection() method
    
    /**
     * Sets whether or not the components of this link to be the sources that trigger recordings of the 
     * selections made.
     * 
     * @param argControl boolean indicator of whether or not the components of this link are to be 
     * the selection sources.
     */
    public void setControlledByBoth(boolean argControl) {
        // case where we want both a carry over button and the combobox associated with this link to trigger 
        // recordings of the selections made
        if (argControl) { 
            boolean bothControllers = false;
            boolean comboControl = false;
            if (!isCarryOverControlled && carryOverButton == null) {
                createCarryOverButton();
                bothControllers = isCarryOverControlled;
            } // if
            if (comboBox != null) {
                ActionListener[] currentComboALs = comboBox.getActionListeners();
                ArrayList comboALs = new ArrayList(Arrays.asList(currentComboALs));
                if (!comboALs.contains(this)) {
                    comboBox.addActionListener(this);
                    comboControl = true;
                } else {
                    comboControl = true;
                } // else
                bothControllers = bothControllers && comboControl;
            } // if
            if (bothControllers) {
                isControlledByBoth = true;
            } // if
        } else { // case where we want either carry over button or the combobox associated with this link to 
                 // trigger recordings of the selections made
            boolean singleController = false;
            boolean comboControl = false;
            if (carryOverButton != null) { // case there is a carry over button associated with this link then
                                           // the button should be the only source for selection recording
                ActionListener[] currentCarryOverALs = carryOverButton.getActionListeners();
                ArrayList carryOverALs = new ArrayList(Arrays.asList(currentCarryOverALs));
                if (!carryOverALs.contains(this)) {
                    carryOverButton.addActionListener(this);
                    if (!isCarryOverControlled) {
                        isCarryOverControlled = true;
                    } // if
                } // if
                if (comboBox != null) {
                    ActionListener[] currentComboALs = comboBox.getActionListeners();
                    ArrayList comboALs = new ArrayList(Arrays.asList(currentComboALs));
                    if (!comboALs.contains(this)) {
                        comboControl = false;
                    } else {
                        comboBox.removeActionListener(this);
                        comboControl = false;
                    } // else
                } // if
            } else { // case there is only the combo box associated with this link that should be the only 
                     // source for selection recording
                if (isCarryOverControlled) {
                    isCarryOverControlled = false;
                } // if
                if (comboBox != null) {
                    ActionListener[] currentComboALs = comboBox.getActionListeners();
                    ArrayList comboALs = new ArrayList(Arrays.asList(currentComboALs));
                    if (!comboALs.contains(this)) {
                        comboBox.addActionListener(this);
                        comboControl = true;
                    } else {
                        comboControl = true;
                    } // else
                } // if
            } // else
            singleController = isCarryOverControlled ^ comboControl;
            if (singleController) {
                isControlledByBoth = false;
            } // if
        } // else
    } // setControlledByBoth() method
    
    /**
     * Clears the currently stored selections made using the participating components of this link.
     */
    public void clearSelection() {
        if (text != null) {
            text = null;
        } // if
        if (multiSelectText != null) {
            multiSelectText = null;
        } // if
        if (selectedItem != null) {
            selectedItem = null;
        } // if
        if (selectedItems != null) {
            selectedItems = null;
        } // if
        String clearText = "";
        if (comboBox != null) {
            Component editingComp = comboBox.getEditor().getEditorComponent();
            if (editingComp != null) {
                if (editingComp instanceof JTextField) {
                    JTextField tfEditingComp = (JTextField) editingComp;
                    tfEditingComp.setText(clearText);
                } // if
            } // if
        } // if
        if (valueComponent != null) {
            switch(type) {
                case LABEL:
                    JLabel label = (JLabel) valueComponent;
                    label.setText(clearText);
                    label.setToolTipText(clearText);
                    break;
                case TEXT_FIELD:
                    JTextField field = (JTextField) valueComponent;
                    field.setText(clearText);
                    field.setToolTipText(clearText);
                    break;
                case TEXT_AREA:
                    JTextArea area = (JTextArea) valueComponent;
                    area.setText(clearText);
                    area.setToolTipText(clearText);
                    break;
                default:
                    // nothing
            } // switch
        } // if
    } // clearSelection() method
    
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
        Object tmp = comboBox.getClientProperty("item-picked");
        //System.out.println("VAL:" + tmp.booleanValue());
        boolean itemPicked = false;
        if (tmp == null) {
            itemPicked = false;
        } else {
            itemPicked = new Boolean(tmp.toString());
        } // else
        System.out.println("ae.getActionCommand(): " + ae.getActionCommand());
        System.out.println("itemPicked: " + itemPicked);
        System.out.println("isCarryOverControlled: " + isCarryOverControlled);
        boolean applyText = (!isControlledByBoth && isCarryOverControlled) || (isControlledByBoth 
                && ae.getActionCommand().equals(carryOverButton.getText())) || (ae.getActionCommand()
                .equals("comboBoxEdited")  && itemPicked) || ((ae.getActionCommand().equals("comboBoxChanged")
                && itemPicked));
        if (applyText) {
            update();
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
            updateToolTip();
        } // if
    } // actionPerformed() method
    
} // ComboBoxMultiSelectionLink class

// $Id$
