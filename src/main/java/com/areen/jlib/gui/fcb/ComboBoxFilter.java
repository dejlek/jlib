/*
 * File: ComboBoxFilter.java
 */
package com.areen.jlib.gui.fcb;

import com.areen.jlib.util.Sise;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
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
    private boolean hitBackspaceOnSelection;
    private boolean hitBackspace;

    private boolean arrowKeyPressed = false;
    private boolean finish = false;
    private int selectedIndex;

    private int previousItemCount;
    
    /**
     * We have to store the popup menu's dimension so we can fix incorrect popup size during the filtering
     * process.
     */
    private int popupMenuWidth; 
    private int popupMenuHeight; 
    
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
                    
                    case KeyEvent.VK_ENTER:
                        finish = true;
                        comboBoxModel.setReadyToFinish(false); // we expect cell editor
                        //setText(comboBox.getSelectedItem().toString());
                        if (!isTableCellEditor()) {
                            setText(comboBoxModel.getKeyOfTheSelectedItem().toString());
                        }
                        break;

                    case KeyEvent.VK_UP:
                        arrowKeyPressed = true;
                        if (isTableCellEditor) {
                            /* For some reason, the JTable is stealing keyboard events and preventing us   *
                             * from moving up/down, so we have to select proper values manually.           *
                             *                                                                             *
                             * If selection did not move, we will manually select appropriate item.        */ 
                            if ((selectedIndex == currentIndex) && (currentIndex > 0)) {
                                comboBox.setSelectedIndex(currentIndex - 1);
                                selectedIndex = currentIndex - 1;
                            } else {
                                selectedIndex = currentIndex;
                            }
                            
                            // we set this to false ONLY if the combo box is a cell editor!
                            arrowKeyPressed = false;
                        } // if
                        break;
                        
                    case KeyEvent.VK_DOWN:
                        arrowKeyPressed = true;
                        if (isTableCellEditor && comboBox.isPopupVisible()) {
                            /* For some reason, the JTable is stealing keyboard events and preventing us   *
                             * from moving up/down, so we have to select proper values manually.           *
                             *                                                                             *
                             * If selection did not move, we will manually select appropriate item.        */ 
                            if ((selectedIndex == currentIndex)
                                    && (currentIndex < comboBox.getItemCount() - 1)) {
                                comboBox.setSelectedIndex(currentIndex + 1);
                                selectedIndex = currentIndex + 1;
                            } else {
                                selectedIndex = currentIndex;
                            } // else
                            // we set this to false ONLY if the combo box is a cell editor!
                            arrowKeyPressed = false;
                        } // if
                        break;

                    default:
                        selectedIndex = currentIndex;
                } // switch
            } // keyPressed() method
        });
        
        // Bug 5100422 on Java 1.5: Editable JComboBox won't hide popup when tabbing out
        hidePopupOnFocusLoss = System.getProperty("java.version").startsWith("1.5");
        // Highlight whole text when focus gets lost
        comboBoxEditor.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                // Workaround for Bug 5100422 - Hide Popup on focus loss
                if (hidePopupOnFocusLoss) {
                    ComboBoxFilter.this.comboBox.setPopupVisible(false);
                } // if
                comboBoxModel.setReadyToFinish(false);
            }
        });
        
        /*
         * The following PopupMenuListener is needed to store the inidial Dimension of the combobox popup.
         */
        comboBox.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent pme) {
                /* NOTE: Dimension returned by getSize() method is actually the dimension of the combo-box
                 *       popup menu! It is not the size of the JTextComponent object! :)
                 */
                System.out.println("popupMenuWillBecomeVisible()");
                JComboBox box = (JComboBox) pme.getSource();
                popupMenuWidth = box.getSize().width;
                popupMenuHeight = box.getSize().height;
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent pme) {
                // do nothing
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent pme) {
                // do nothing
            }
        });
        
        Object selected = comboBox.getSelectedItem();
        selectedIndex = comboBox.getSelectedIndex();
        if (selected != null) {
            setText(comboBoxModel.getKeyOfTheSelectedItem().toString());
        }
    } // ComboBoxFilter constructor

    /**
     * User typically will call this method from a table cell editor when we want to "inform" filtered
     * combo-box that we want to re-filter before we actually start editing. The reason for this is when
     * user goes to another cell, the filtered set of entries from the previous cell will not apply.
     * 
     * @param argPattern 
     */
    public void prepare(Object argPattern) {
        //System.out.println("prepare(" + argPattern + ")");
        try {
            selecting = true;
            if (isTableCellEditor()) {
                comboBoxModel.setReadyToFinish(false);
            }
            if (argPattern == null) {
                setText("");
            } else {
                setText(argPattern.toString().trim());
            } // else
            filterTheModel(); // will set selecting to false
        } catch (BadLocationException ex) {
            Logger.getLogger(ComboBoxFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
    } // prepare() method
    
    // ======================================================================================================
    // ===== Private methods ================================================================================
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
    
    /**
     * This method is a leftover from the previous version of the ComboBoxFilter. Should be removed after
     * the testing phase.
     */
    private void highlightCompletedText(int start) {
        comboBoxEditor.setCaretPosition(getLength());
        comboBoxEditor.moveCaretPosition(start);
    }
    
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        System.out.println("insertString(" + offs + ", " + str + ")");
        
        // return immediately when selecting an item
        if (selecting) {
            return;
        } // if

        if (arrowKeyPressed) {
            return;
        }

        // insert the string into the document
        if (str.contains(Sise.UNIT_SEPARATOR_STRING)) {
            // we got a string in the Sise format, that must be because user picked an item with a mouse
            // in that case, we will take the key component (SISE unit) and put that instead.
            String[] strs = Sise.units(str);
            int idx = comboBoxModel.getKeyIndex();
            if (isTableCellEditor()) {
                comboBoxModel.setReadyToFinish(true);
            }
            
            super.insertString(offs, strs[idx], a);

            if (!isTableCellEditor()) {
                comboBox.putClientProperty("item-picked", Boolean.TRUE);
                // we have to filter after the user selects an item with the mouse.
                // WARNING: here we rely on the FilteredComboBoxModel's setPattern() method to select the
                //          exact match - ie the item that user picked with the mouse.
                filterTheModel();
                comboBox.putClientProperty("item-picked", Boolean.FALSE);
            } // if
        } else {
            // otherwise, insert the whole string
            super.insertString(offs, str, a);
        } // else

        if (finish) {
            return;
        }

/*
        // lookup and select a matching item
        Object lookupItem = comboBoxModel.lookupItem(getText(0, getLength()));
        if (lookupItem != null) {
            Object selectedItem = comboBox.getSelectedItem();
            //System.out.println("LOOKUP:" + lookupItem);
            //System.out.println("SELECTED:" + selectedItem);
            if (lookupItem == selectedItem) {
                return;
            } // if
        } // if
*/

        filterTheModel();
    } // insertString() method
    
    @Override
    public void remove(int offs, int len) throws BadLocationException {
        System.out.println("remove(" + offs + ", " + len + ")");
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
                // the insertString. However, sometimes table steals the event, and causes trouble. 
                // As remove() is called first, here we check if correct value has been selected after user 
                // presses UP/DOWN
                int currentIndex = comboBox.getSelectedIndex();
                if (selectedIndex != currentIndex) {
                    // they are not equal, fix it
                    comboBox.setSelectedIndex(selectedIndex);
                } // if
            } // if
            return;
        } // if
        
        // remove the string from the document
        super.remove(offs, len);
/*
        // lookup and select a matching item
        Object lookupItem = comboBoxModel.lookupItem(getText(0, getLength()));
        if (lookupItem != null) {
            Object selectedItem = comboBox.getSelectedItem();
            //System.out.println("LOOKUP:" + lookupItem);
            //System.out.println("SELECTED:" + selectedItem);
            if (lookupItem == selectedItem) {
                
                return;
            } // if
        } // if
*/        
        if (finish) {
            // user pressed ENTER so in the case remove is called we do not filter the model.
            return;
        } // if
        
        // finally, do the filter
        filterTheModel();
    } // remove() method
    
    /**
     * This method calls the setPatter() method, and starts the filtering.
     * 
     * It also sets the previousItemCount variable to hold the previous number of filtered items.
     */
    private void filterTheModel() throws BadLocationException {
        // we have to "guard" the call to comboBoxModel.setPattern() with selecting set to true, then false
        selecting = true;
        boolean oldValue = comboBoxModel.isReadyToFinish();
        comboBoxModel.setReadyToFinish(false); // we must set this to false during the filtering
        previousItemCount = comboBox.getItemCount(); /// store the number of items before filtering
        int pos = comboBoxEditor.getCaretPosition();
        comboBoxModel.setPattern(getText(0, getLength()));
        
        if (comboBoxEditor.getSelectedText() != null) {
            // we have a selected text, removing the selection.On Windows text may become selected by default
            System.out.println("SELECTED TEXT: " + comboBoxEditor.getSelectedText());
            comboBoxEditor.select(0, 0);
            // return caret position to the original place
            comboBoxEditor.setCaretPosition(pos);
        } // if
        
        this.fixPopupSize();
        
        comboBoxModel.setReadyToFinish(oldValue); // restore the value
        selecting = false;
        selectedIndex = comboBox.getSelectedIndex();
        //System.out.println("SELECTED AFTER:" + comboBox.getSelectedItem());
    } // filterTheModel()
    
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

    /**
     * This method is used internally to fix the popup-menu size. Apparantly JComboBox has a bug and does
     * not calculate the proper height of the popup.
     * 
     * The first time popup menu is shown, ComboBoxFilter stores the dimension, and re-adjusts the width to
     * the original value all the time. Reason for this is that we do not want to have different widths
     * while user types something.
     */
    private void fixPopupSize() {
        int maxRows = comboBox.getMaximumRowCount();
        if ((previousItemCount < maxRows) || (comboBox.getItemCount() < maxRows)) {
            // do this only when we have less than maxRows items, to prevent the flickering.
            // this is a hack and is actually the easiest solution to the JComboBox's popup resizing problem.
            if (comboBox.isPopupVisible()) {
                comboBox.setPopupVisible(false);
                comboBox.setPopupVisible(true);
            } // if
        } // if 
        return;
        /*
        JComboBox box = comboBox;
        int maxRows = box.getMaximumRowCount();
        
        if (!box.isPopupVisible()) {
            // if popup menu is not visible, we should close it, there is no point of fixing the size
            return;
        }
        
        Object comp = box.getUI().getAccessibleChild(box, 0);
        JPopupMenu popup;
        if (comp instanceof JPopupMenu) { 
            popup = (JPopupMenu) comp;
        } else {
            return;
        }
        
        JComponent scrollPane = (JComponent) popup.getComponent(0);
        Dimension size = new Dimension();
        
        /// We want to keep the same width all the time
        size.width = popupMenuWidth; 
        //size.width = box.getPreferredSize().width;
        
        size.height = scrollPane.getPreferredSize().height;
        
        System.out.println(size);
        int newHeight = maxRows * 24;
        if (comboBox.getItemCount() < maxRows && comboBox.getItemCount() > 0) {
            newHeight = comboBox.getItemCount() * 24;
            popupMenuHeight = Math.max(popupMenuHeight, newHeight);
            popupMenuHeight = Math.max(popupMenuHeight, size.height);
            newHeight = popupMenuHeight;
        }
        if (comboBox.getItemCount() >= maxRows) {
            // we will store maximal popup height here
            popupMenuHeight = Math.max(popupMenuHeight, size.height);
            newHeight = popupMenuHeight;
        } // if
        System.out.println(popupMenuWidth + "," + popupMenuHeight);
        size.height = newHeight;
        System.out.println("fixed: " + size);
        //scrollPane.setSize(size);
        scrollPane.setPreferredSize(size);
        popup.setPopupSize(size);
        //comboBox.validate();
        
        //  following line for Tiger (MacOS X):
        // scrollPane.setMaximumSize(size);
        * 
        * 
        */
    } // fixPopupSize() method
    
} // ComboBoxFilter class

// $Id$
