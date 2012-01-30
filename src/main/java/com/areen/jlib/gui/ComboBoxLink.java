/**
 * $Id: LocationPanel.java 1461 2012-01-18 18:38:07Z dejan $
 *
 * ComboBoxLink
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
    }
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
        System.out.println(comboBox.getSelectedIndex());
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
        System.out.println(ae.getActionCommand());
        System.out.println(comboBox.isPopupVisible());
        update();

        Boolean tmp = (Boolean) comboBox.getClientProperty("item-picked");
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
            // if user picked an item via mouse, we rely on the client property "item-picked"
            // FIXME: ComboBoxLink should be a reusable component, this makes it depend on JComboBox to
            //        be filtered! (Yes, ComboBoxLink works properly only with filtered combo box)
            //        Perhaps better solution would be to use PopupMenuListener and trap the 
            //        popupMenuWillBecomeInvisible event.
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
        } // else if
    } // actionPerformed() method
    
} // ComboBoxLink class
