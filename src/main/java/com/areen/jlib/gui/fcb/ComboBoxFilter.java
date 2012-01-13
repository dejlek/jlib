/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.gui.fcb;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

/**
 * Lots of ideas from http://www.orbital-computer.de/JComboBox/
 * To take a look: http://tech.chitgoks.com/2009/11/06/autocomplete-jcombobox/
 * 
 * Notes:
 * JTable sometimes steals events, so JComboBox behaves quite differently when is used as cell editor.
 * In this particular case, we must write code to manually select appropriate item. Therefore we capture
 * keyboard events and maintain what has to be selected, and whether arrow keys are pressed.
 * 
 * ALWAYS, when user navigates via arrow keys remove() is called first (whole string is deleted), followed
 * by an insertString() call to set the text from the next item. Since we do not want to lose what user types
 * we ignore these changes completely.
 * 
 * Whenever we call setSelectedIndex() or setSelectedItem() (JComboBox methods), remove() and insertString()
 * are called. Therefore we manage the "selecting" state, and surround every filtering process with
 * selecting = true; doFiltering(); selecting = false;
 * Both remove() and insertString() do nothing when selecting is true because of this.
 *
 * @author dejan
 */
public class ComboBoxFilter extends PlainDocument {
    private JComboBox comboBox;
    private JTextComponent comboBoxEditor;
    private FilteredComboBoxModel comboBoxModel;
    
    /**
     * Every call to setSelectedItem() in the combo-box will provoke another call to the insertString, with
     * the newly selected item. We prevent this by seting "selecting" to true before we call setPattern().
     * Subsequent calls to remove/insertString should be ignored.
     */
    boolean selecting = false;
    
    boolean hidePopupOnFocusLoss;
    
    // TODO: these two are not important, remove after debug
    boolean hitBackspaceOnSelection;
    boolean hitBackspace;
    private boolean arrowKeyPressed = false;
    private boolean finish = false;
    private int selectedIndex;
    
    /**
     * This constructor adds filtering capability to the given JComboBox object argComboBox. It will
     * also assign appropriate model to the combo-box, one that has setPattern() method. It will also
     * set the argComboBox to be editable.
     * 
     * @param argComboBox
     * @param argComboBoxModel 
     */
    public ComboBoxFilter(final JComboBox argComboBox, FilteredComboBoxModel argComboBoxModel) {
        comboBox = argComboBox;
        comboBox.setEditable(true);
        comboBoxModel = argComboBoxModel;
        comboBox.setModel(comboBoxModel);
        
        comboBoxEditor = (JTextComponent) comboBox.getEditor().getEditorComponent();
        comboBoxEditor.setDocument(this);

        // let's add a key listener to the editor
        comboBoxEditor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("keyPressed()");
                boolean isTableCellEditor = false;
                Object tmp = comboBox.getClientProperty("JComboBox.isTableCellEditor");
                if (tmp != null) {
                    isTableCellEditor = tmp.equals(Boolean.TRUE);
                }
                if (comboBox.isDisplayable()) {
                    comboBox.setPopupVisible(true);
                } // if
                arrowKeyPressed = false;
                finish = false;
                int currentIndex = comboBox.getSelectedIndex();
                switch (e.getKeyCode()) {
                    /*
                    // determine if the pressed key is backspace (needed by the remove method)
                    case KeyEvent.VK_BACK_SPACE : 
                        hitBackspace = true;
                        hitBackspaceOnSelection = (comboBoxEditor.getSelectionStart() 
                                != comboBoxEditor.getSelectionEnd());
                        break;
                    // ignore delete key
                    case KeyEvent.VK_DELETE: 
                        e.consume();
                        comboBox.getToolkit().beep();
                        break;
                    * 
                    */
                    case KeyEvent.VK_ENTER:
                        finish = true;
                        setText(comboBox.getSelectedItem().toString());
                        break;
                        
                    case KeyEvent.VK_UP:
                        arrowKeyPressed = true;
                        if (isTableCellEditor) {
                            System.out.println("----------------------------------------");
                            /* For some reason, the JTable is stealing keyboard events and preventing us   *
                             * from moving up/down, so we have to select proper values manually.           *
                             *                                                                             *
                             * If selection did not move, we will manually select appropriate item.        */ 
                            System.out.println("UP1 " + selectedIndex + "/" + currentIndex);
                            if ((selectedIndex == currentIndex) && (currentIndex != 0)) {
                                comboBox.setSelectedIndex(currentIndex - 1);
                                selectedIndex = currentIndex - 1;
                            } else {
                                selectedIndex = currentIndex;
                            }
                            
                            // DEBUG STUFF HERE:
                            currentIndex = comboBox.getSelectedIndex();
                            System.out.println("UP1 " + selectedIndex + "/" + currentIndex);
                            System.out.println("SEL: (" + comboBoxModel.getSize() + ") " 
                                    + comboBoxModel.getSelectedItem());
                            System.out.println("SEL idx: " + comboBox.getSelectedIndex());
                            for (int i = 0; i < 5; i++) {
                                System.out.println(i + " " + comboBox.getItemAt(i));
                            }
                        } // if
                        break;
                    case KeyEvent.VK_DOWN:
                        arrowKeyPressed = true;
                        if (isTableCellEditor) {
                            System.out.println("----------------------------------------");
                            /* For some reason, the JTable is stealing keyboard events and preventing us   *
                             * from moving up/down, so we have to select proper values manually.           *
                             *                                                                             *
                             * If selection did not move, we will manually select appropriate item.        */ 
                            System.out.println("DOWN0 " + selectedIndex + "/" + currentIndex);
                            if ((selectedIndex == currentIndex) 
                                    && (currentIndex != comboBox.getItemCount() - 1)) {
                                comboBox.setSelectedIndex(currentIndex + 1);
                                selectedIndex = currentIndex + 1;
                            } else {
                                selectedIndex = currentIndex;
                            } // else
                            // DEBUG STUFF HERE:
                            currentIndex = comboBox.getSelectedIndex();
                            System.out.println("DOWN1 " + selectedIndex + "/" + currentIndex);
                            System.out.println("SEL: (" + comboBoxModel.getSize() + ") " 
                                    + comboBoxModel.getSelectedItem());
                            System.out.println("SEL CB: (" + comboBox.getItemCount() + ") " 
                                    + comboBox.getSelectedItem());
                            System.out.println("SEL idx: " + comboBox.getSelectedIndex());
                            System.out.println(comboBoxModel.getElementAt(selectedIndex));
                            for (int i = 0; i < 5; i++) {
                                System.out.println(i + " " + comboBox.getItemAt(i));
                            }
                            comboBox.revalidate();
                        } // if
                        break;
                    default:
                        selectedIndex = currentIndex;
                } // switch
            } // keyPressed() method
        });
        
        /*
        // Bug 5100422 on Java 1.5: Editable JComboBox won't hide popup when tabbing out
        hidePopupOnFocusLoss = System.getProperty("java.version").startsWith("1.5");
        // Highlight whole text when focus gets lost
        comboBoxEditor.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                //highlightCompletedText(0);
                // Workaround for Bug 5100422 - Hide Popup on focus loss
                if (hidePopupOnFocusLoss) {
                    ComboBoxFilter.this.comboBox.setPopupVisible(false);
                } // if
            }
        });
        */
        
        Object selected = comboBox.getSelectedItem();
        selectedIndex = comboBox.getSelectedIndex();
        if (selected != null) {
            setText(selected.toString());
        }
    } // ComboBoxFilter constructor

    // ======================================================================================================
    //  Private methods
    // ======================================================================================================
    
    private void setText(String text) {
        try {
            // remove all text and insert the completed string
            super.remove(0, getLength());
            super.insertString(0, text, null);
        } catch (BadLocationException e) {
            throw new RuntimeException(e.toString());
        }
    }
    
    private void highlightCompletedText(int start) {
        comboBoxEditor.setCaretPosition(getLength());
        comboBoxEditor.moveCaretPosition(start);
    }
    
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        //System.out.println("insertString(" + offs + ", " + str + ", " + a + ")");
        // return immediately when selecting an item
        if (selecting) {
            return;
        } // if
        
        if (arrowKeyPressed) {
            return;
        }
        
        System.out.println("O");
        
        // insert the string into the document
        super.insertString(offs, str, a);
        
        if (finish) {
            return;
        }

        // lookup and select a matching item
        Object lookupItem = comboBoxModel.lookupItem(getText(0, getLength()));
        if (lookupItem != null) {
            Object selectedItem = comboBox.getSelectedItem();
            System.out.println("LOOKUP:" + lookupItem);
            System.out.println("SELECTED:" + selectedItem);
            if (lookupItem == selectedItem) {
                return;
            }
        }
        
        filterTheModel();
    } // insertString() method
    
    @Override
    public void remove(int offs, int len) throws BadLocationException {
        //System.out.println("remove(" + offs + ", " + len + ")");
        // return immediately when selecting an item
        if (selecting) {
            // remove() is called whenever setSelectedItem() or setSelectedIndex() are called. They may be
            // called during the filtering process, so we do not want to remove while in the middle.
            return;
        } // if
        
        if (arrowKeyPressed) {
            if (isTableCellEditor()) {
            // if the remove() has been called while user navigates through the combobox list, we do not
            // filter. when user navigates via arrow keys, remove() is always called first, followed by
            // the insertString. However, sometimes table steals the event, and causes trouble. As remove()
            // is called first, here we check if correct value has been selected after user presses UP/DOWN
            int currentIndex = comboBox.getSelectedIndex();
            if (selectedIndex != currentIndex) {
                // they are not equal, fix it
                comboBox.setSelectedIndex(selectedIndex);
            } // if
            } // if
            return;
        } // if
        
        System.out.println("X");
        
        super.remove(offs, len);

        // lookup and select a matching item
        Object lookupItem = comboBoxModel.lookupItem(getText(0, getLength()));
        if (lookupItem != null) {
            Object selectedItem = comboBox.getSelectedItem();
            System.out.println("LOOKUP:" + lookupItem);
            System.out.println("SELECTED:" + selectedItem);
            if (lookupItem == selectedItem) {
                return;
            }
        }
        
        if (finish) {
            // user pressed ENTER so in the case remove is called we do not filter the model.
            return;
        } // if
        
        // finally, do the filter
        filterTheModel();
    } // remove() method
    
    private void filterTheModel() throws BadLocationException {
        // we have to "guard" the call to comboBoxModel.setPattern() with selecting set to true, then false
        selecting = true;
        comboBoxModel.setPattern(getText(0, getLength()));
        if (comboBoxModel.getSize() > 0) {
            comboBox.setSelectedIndex(0);
            selectedIndex = 0;
        }        
        comboBox.validate();
        selecting = false;
        System.out.println("SELECTED AFTER:" + comboBox.getSelectedItem());
    }
    
    /**
     * Use this method whenever you need to determine if the comboBox is used as a cell editor or not.
     * @return boolean Value indicating whether comboBox is a cell editor (TRUE) or not (FALSE).
     */
    private boolean isTableCellEditor() {
        boolean isTableCellEditor = false;
        Object tmp = comboBox.getClientProperty("JComboBox.isTableCellEditor");
        if (tmp != null) {
            isTableCellEditor = tmp.equals(Boolean.TRUE);
        } // if
        return isTableCellEditor;
    } // isTableCellEditor method
    
} // ComboBoxFilter class

// $Id$
