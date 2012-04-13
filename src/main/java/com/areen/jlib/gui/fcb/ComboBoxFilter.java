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

import com.areen.jlib.gui.ColorArrowUI;
import com.areen.jlib.util.Sise;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

/**
 * Lots of ideas from http://www.orbital-computer.de/JComboBox/ To take a look:
 * http://tech.chitgoks.com/2009/11/06/autocomplete-jcombobox/
 *
 * Notes: JTable sometimes steals events, so JComboBox behaves quite differently
 * when is used as cell editor. In this particular case, we must write code to
 * manually select appropriate item. Therefore we capture keyboard events and
 * maintain what has to be selected, and whether arrow keys are pressed.
 *
 * ALWAYS, when user navigates via arrow keys remove() is called first (whole
 * string is deleted), followed by an insertString() call to set the text from
 * the next item. Since we do not want to lose what user types we ignore these
 * changes completely.
 *
 * Whenever we call setSelectedIndex() or setSelectedItem() (JComboBox methods),
 * remove() and insertString() are called. Therefore we manage the "selecting"
 * state, and surround every filtering process with selecting = true;
 * doFiltering(); selecting = false; Both remove() and insertString() do nothing
 * when selecting is true because of this.
 *
 * @author dejan
 */
public class ComboBoxFilter extends PlainDocument {

    private JComboBox comboBox;
    private JTextComponent comboBoxEditor;
    private FilteredComboBoxModel comboBoxModel;
    /**
     * Every call to setSelectedItem() in the combo-box will provoke another
     * call to the insertString, with the newly selected item. We prevent this
     * by seting "selecting" to true before we call setPattern(). Subsequent
     * calls to remove/insertString should be ignored.
     */
    boolean selecting = false;
    boolean hidePopupOnFocusLoss;
    // TODO: these two are not important, remove after debug
    private boolean hitBackspaceOnSelection;
    private boolean hitBackspace;
    private boolean arrowKeyPressed = false;
    private boolean keyPressed = false;
    private boolean finish = false;
    private boolean inPreparation;
    private int selectedIndex;
    private Object pickedItem;
    private Object pickedKey;
    /**
     * This value is set by cell-editors to inform ComboBoxFilter whether to
     * execute pick value or not during the preparation phase (prepare()
     * method).
     */
    private boolean triggeredByKeyPress = false;
    private int previousItemCount;
    /**
     * We have to store the popup menu's dimension so we can fix incorrect popup
     * size during the filtering process.
     */
    private int popupMenuWidth;
    private int popupMenuHeight;

    static final Logger LOGGER = Logger.getLogger(ComboBoxFilter.class.getCanonicalName());
    
    /**
     * This constructor adds filtering capability to the given JComboBox object
     * argComboBox. It will also assign appropriate model to the combo-box, one
     * that has setPattern() method. It will also set the argComboBox to be
     * editable.
     *
     * @param argComboBox
     * @param argComboBoxModel
     */
    public ComboBoxFilter(final JComboBox argComboBox, FilteredComboBoxModel argComboBoxModel) {
        comboBox = argComboBox;
        comboBox.setEditable(true);

        fixComboBoxArrowUI();

        comboBoxModel = argComboBoxModel;
        comboBox.setModel(comboBoxModel);
        
        // If initially an item is selected, we will assume that item is a picked item.
        comboBox.putClientProperty("item-picked", Boolean.TRUE);
        /*
        if (comboBox.getSelectedItem() == null) {
            comboBox.putClientProperty("item-picked", Boolean.TRUE);
        } else {
            comboBox.putClientProperty("item-picked", Boolean.TRUE);
        }
        * 
        */
        
        comboBoxEditor = (JTextComponent) comboBox.getEditor().getEditorComponent();
        comboBoxEditor.setDocument(this);

        // let's add a key listener to the editor
        comboBoxEditor.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                LOGGER.info("keyPressed()");
                int keyCode = e.getKeyCode();
                System.out.println(keyCode);

                /*
                 * In the case user presses SHIFT, CTRL, ALT keys, or <ANY>+TAB,
                 * we return imediately.
                 */
                //int modifiers = e.getModifiersEx();
                if (keyCode == KeyEvent.VK_SHIFT
                        || (keyCode == KeyEvent.VK_ALT)
                        || (keyCode == KeyEvent.VK_CONTROL)
                        || (keyCode == KeyEvent.VK_WINDOWS)
                        || (keyCode == KeyEvent.VK_CONTEXT_MENU)
                        || (keyCode == KeyEvent.VK_TAB && e.isShiftDown())) {
                    keyPressed = false;
                    return;
                } // if

                keyPressed = true;
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

                switch (keyCode) {
                    case KeyEvent.VK_TAB:
                        LOGGER.info("TAB!");
                        finish = true;
                        comboBoxModel.setReadyToFinish(false);
                        
                        if (!isTableCellEditor()) {
                            String txt = updateFcbEditor();
                        }

                        if ((comboBox.getSelectedItem() == null)) {
                            /*
                             * if TAB is pressed, but nothing is selected, and
                             * the picked item is not null * that means the user
                             * pressed tab when there was an empty list of
                             * items. * (Typically when user typed something
                             * that does not exist in the list of * items). In
                             * this case we cancel the editing.
                             */
                            comboBoxModel.setCancelled(true);
                        } // if

                        if (comboBox.getSelectedItem() != null) {
                            if (pickedItem == comboBox.getSelectedItem()) {
                                /*
                                 * We cancel the editing when the picked item is
                                 * the same as the item that * is currently
                                 * selected in the combo-box, because we do not
                                 * want to * trigger database change (there is
                                 * no need to update to the same value).
                                 */
                                comboBoxModel.setCancelled(true);
                            } else {
                                pickedItem = comboBox.getSelectedItem();
                                pickedKey = comboBoxModel.getKeyOfTheSelectedItem().toString();
                            } // else
                        } // if
                        break;

                    case KeyEvent.VK_ESCAPE:
                        if (isTableCellEditor()) {
                            comboBoxModel.setCancelled(true);
                            comboBox.setSelectedItem(pickedItem);
                        } else {
                            ComboBoxFilter.this.setText(pickedKey.toString());
                        }
                        break;

                    case KeyEvent.VK_ENTER:
                        finish = true;
                        comboBoxModel.setReadyToFinish(false); // we expect cell editor
                        String txt = updateFcbEditor();

                        pickedItem = comboBox.getSelectedItem();
                        pickedKey = txt;
                        break;

                    case KeyEvent.VK_UP:
                        arrowKeyPressed = true;

                        if (isTableCellEditor) {
                            /*
                             * For some reason, the JTable is stealing keyboard
                             * events and preventing us * from moving up/down,
                             * so we have to select proper values manually. * *
                             * If selection did not move, we will manually
                             * select appropriate item.
                             */
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
                            /*
                             * For some reason, the JTable is stealing keyboard
                             * events and preventing us * from moving up/down,
                             * so we have to select proper values manually. * *
                             * If selection did not move, we will manually
                             * select appropriate item.
                             */
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
                keyPressed = false;
            } // keyPressed() method
        });

        // Bug 5100422 on Java 1.5: Editable JComboBox won't hide popup when tabbing out
        hidePopupOnFocusLoss = System.getProperty("java.version").startsWith("1.5");
        // Highlight whole text when focus gets lost
        comboBoxEditor.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                boolean pa = comboBoxModel.isAnyPatternAllowed();
                boolean ma = comboBoxModel.isMultiSelectionAllowed();
                LOGGER.info("focusLost()");
                if (pickedKey != null && !(pa || ma)) {
                    // When combo-box loses focus, we need to set the text to the selected
                    setText(pickedKey.toString());
                } // if

                // Workaround for Bug 5100422 - Hide Popup on focus loss
                if (hidePopupOnFocusLoss) {
                    ComboBoxFilter.this.comboBox.setPopupVisible(false);
                } // if
                comboBoxModel.setReadyToFinish(false);
            }

            @Override
            public void focusGained(FocusEvent fe) {
                if (!isTableCellEditor()) {
                    super.focusGained(fe);
                    comboBoxEditor.selectAll();
                } // if
            } // focusGained() method
        });

        /*
         * The following PopupMenuListener is needed to store the inidial
         * Dimension of the combobox popup.
         */
        comboBox.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent pme) {
                /*
                 * NOTE: Dimension returned by getSize() method is actually the
                 * dimension of the combo-box popup menu! It is not the size of
                 * the JTextComponent object! :)
                 */
                JComboBox box = (JComboBox) pme.getSource();
                popupMenuWidth = box.getSize().width;
                popupMenuHeight = box.getSize().height;
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent pme) {
                LOGGER.info("hiding popup...");
                comboBox.putClientProperty("item-picked", Boolean.FALSE);
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent pme) {
                // do nothing
            }
        });


        if (!isTableCellEditor()) {
            comboBoxEditor.setFocusTraversalKeysEnabled(false);

            Action myAction = new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // Nothing is needed here - all we want is to skip focusLost() call when user presses TAB
                    comboBoxEditor.transferFocus();
                } // actionPerformed() method
            };

            comboBoxEditor.getActionMap().put("tab-action", myAction);
            comboBoxEditor.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                    .put(KeyStroke.getKeyStroke("TAB"), "tab-action");
        } // if

        Object selected = comboBox.getSelectedItem();
        pickedItem = selected;

        Object tmp = comboBoxModel.getKeyOfTheSelectedItem();
        if (tmp instanceof String) {
            pickedKey = tmp.toString();
        } else {
            pickedKey = tmp;
        } // else

        selectedIndex = comboBox.getSelectedIndex();
        if (selected != null) {
            setText(comboBoxModel.getKeyOfTheSelectedItem().toString());
        }
    }

    /**
     * User typically will call this method from a table cell editor when we
     * want to "inform" filtered combo-box that we want to re-filter before we
     * actually start editing. The reason for this is when user goes to another
     * cell, the filtered set of entries from the previous cell will not apply.
     *
     * @param argPattern
     */
    public void prepare(Object argPattern) {
        LOGGER.info("prepare(" + argPattern + ")");

        if (argPattern == null) {
            // If the previous value is null, we simply exit this method.
            pickedItem = null;
            pickedKey = null;
            comboBox.setSelectedItem(null);
            return;
        } // if

        inPreparation = true;
        selecting = true;
        comboBoxModel.setCancelled(false);
        if (isTableCellEditor()) {
            comboBoxModel.setReadyToFinish(false);
        }

        String pat = (String) argPattern.toString();

        /*
         * If the editing is triggered by a key-press in a JTable, then the
         * argPattern contains a string in SISE format, so we have to extract
         * they key pressed and the previous value.
         */
        String key = "";
        if (isTriggeredByKeyPress()) {
            String[] strs = Sise.units(pat);
            /*
             * In the case the cell's value was a NULL, then strs will have only
             * one element. In that case we set pat to be a null, and do not set
             * the pickedItem.
             */
            if (strs.length == 2) {
                pat = strs[1];
            } else {
                pat = null;
            } // else
            key = strs[0];
        } // if

        try {
            if (argPattern == null) {
                setText("");
            } else {
                if (pat != null) {
                    setText(pat.trim());
                }
            } // else

            if (pat == null) {
                pickedItem = null;
                pickedKey = null;
            } else {
                // Cell's value was not a null, so we can execute filter so the combo-box updates the 
                // selected item to be what was previously selected.
                filterTheModel();
                pickedItem = comboBox.getSelectedItem();
                pickedKey = comboBoxModel.getKeyOfTheSelectedItem();
            } // else

            // Finally, when we have selected the item that was previously selected, now we can insert
            // the character user typed inside a JTable
            if (isTriggeredByKeyPress()) {
                setText(key);
                filterTheModel();
            } // if
        } catch (BadLocationException ex) {
            Logger.getLogger(ComboBoxFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (isTableCellEditor()) {
            comboBoxModel.setReadyToFinish(false);
        }
        inPreparation = false;
        LOGGER.info("prepare done.");
    } // prepare() method

    // ======================================================================================================
    // ===== Private methods ================================================================================
    // ======================================================================================================
    
    private void clearTextSelection() {
        if (comboBoxEditor.getSelectedText() != null) {
            // we have a selected text, removing the selection. On Windows text may become selected by default
            LOGGER.info("SELECTED TEXT: " + comboBoxEditor.getSelectedText());
            int pos = comboBoxEditor.getCaretPosition();
            comboBoxEditor.select(0, 0);
            // return caret position to the original place
            comboBoxEditor.setCaretPosition(pos);
        } // if
    }

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
     * This method is a leftover from the previous version of the
     * ComboBoxFilter. Should be removed after the testing phase.
     */
    private void highlightCompletedText(int start) {
        comboBoxEditor.setCaretPosition(getLength());
        comboBoxEditor.moveCaretPosition(start);
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        LOGGER.info("insertString(" + offs + ", " + str + ")");
        //LOGGER.info("insertString(" + selecting + ", " + arrowKeyPressed + ")");
        // return immediately when selecting an item
        if (selecting) {
            return;
        } // if

        if (arrowKeyPressed) {
            arrowKeyPressed = false;
            comboBox.putClientProperty("item-picked", Boolean.FALSE);
            return;
        }
            
        boolean itemPicked = false; // we need this value because item-packed may change due to chain
        // of events
        // inform action-performed listeners that the item has been picked so they may update
        // some other components
        if (keyPressed) {
            itemPicked = false;
        } else {
            itemPicked = true;
        }
        boolean isPicked = (Boolean) comboBox.getClientProperty("item-picked");
        comboBox.putClientProperty("item-picked", itemPicked || isPicked);
        
        // insert the string into the document
        if (str.contains(Sise.UNIT_SEPARATOR_STRING)) {
            LOGGER.info("%%%%%%%%%%%%%");
            System.out.println(str);
            // we got a string in the Sise format, that must be because user picked an item with a mouse
            // in that case, we will take the key component (SISE unit) and put that instead.
            String[] strs = Sise.units(str);
            int idx = comboBoxModel.getKeyIndex();
            if (isTableCellEditor()) {
                comboBoxModel.setReadyToFinish(true);
            }

            super.insertString(offs, strs[idx], a);

            // we have to filter after the user selects an item with the mouse.
            // WARNING: here we rely on the FilteredComboBoxModel's setPattern() method to select the
            //          exact match - ie the item that user picked with the mouse.
            filterTheModel();

            if (itemPicked) {
                pickedItem = comboBox.getSelectedItem();
                Object tmp = comboBoxModel.getKeyOfTheSelectedItem();
                if (tmp instanceof String) {
                    pickedKey = tmp.toString();
                } else {
                    pickedKey = tmp;
                } // else
            } // if
            comboBox.putClientProperty("item-picked", Boolean.TRUE);
            return;
        } else {
            // otherwise, insert the whole string
            super.insertString(offs, str, a);
        } // else

        if (finish) {
            return;
        }

        filterTheModel();
    } // insertString() method

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        LOGGER.info("remove(" + offs + ", " + len + ")");
        LOGGER.info("remove(" + selecting + ", " + arrowKeyPressed + ")");
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
     * It also sets the previousItemCount variable to hold the previous number
     * of filtered items.
     */
    private void filterTheModel() throws BadLocationException {
        // we have to "guard" the call to comboBoxModel.setPattern() with selecting set to true, then false
        selecting = true;
        boolean oldValue = comboBoxModel.isReadyToFinish();
        comboBoxModel.setReadyToFinish(false); // we must set this to false during the filtering
        previousItemCount = comboBox.getItemCount(); /// store the number of items before filtering

        String pattern = getText(0, getLength());
        //LOGGER.info("filterTheModel(): " + pattern);
        comboBoxModel.setPattern(pattern);

        clearTextSelection();
        fixPopupSize();

        comboBoxModel.setReadyToFinish(oldValue); // restore the value
        selecting = false;
        selectedIndex = comboBox.getSelectedIndex();
        //LOGGER.info("SELECTED AFTER:" + comboBox.getSelectedItem());
    }

    /**
     * Use this method whenever you need to determine if the comboBox is used as
     * a cell editor or not.
     *
     * @return boolean Value indicating whether comboBox is a cell editor (TRUE)
     * or not (FALSE).
     */
    private boolean isTableCellEditor() {
        boolean isTableCellEditor = false;
        Object tmp = comboBox.getClientProperty("JComboBox.isTableCellEditor");
        if (tmp != null) {
            isTableCellEditor = tmp.equals(Boolean.TRUE);
        }
        return isTableCellEditor;
    } // isTableCellEditor method

    /**
     * This method is used internally to fix the popup-menu size. Apparently
     * JComboBox has a bug and does not calculate the proper height of the
     * popup.
     *
     * The first time popup menu is shown, ComboBoxFilter stores the dimension,
     * and re-adjusts the width to the original value all the time. Reason for
     * this is that we do not want to have different widths while user types
     * something.
     */
    private void fixPopupSize() {
        if (inPreparation) {
            return;
        }
        LOGGER.info("fixPopupSize()");
        int maxRows = comboBox.getMaximumRowCount();
        if ((previousItemCount < maxRows) || (comboBox.getItemCount() < maxRows)) {
            // do this only when we have less than maxRows items, to prevent the flickering.
            // this is a hack and is actually the easiest solution to the JComboBox's popup resizing problem.
            if (comboBox.isPopupVisible()) {
                comboBox.setPopupVisible(false);
                comboBox.setPopupVisible(true);
            } // if
        } // if 
    } // fixPopupSize() method

    /**
     * If the l&f is system, and OS is Windows, we have to fix the arrow UI of
     * the combo box, when editing is enabled. This method is responsible for
     * doing that.
     */
    private void fixComboBoxArrowUI() {
        String scn = UIManager.getSystemLookAndFeelClassName();
        if (System.getProperty("os.name").startsWith("Windows")
                && UIManager.getLookAndFeel().getClass().getCanonicalName().equals(scn)) {
            System.err.println("DEBUG: fixing the combo-box's arrow");
            comboBox.setUI(ColorArrowUI.createUI(comboBox));
        } // if
    } // fixComboBoxArrowUI() method
    
    private String updateFcbEditor() {
        Object obj = comboBoxModel.getKeyOfTheSelectedItem();
        String txt = null;
        if (obj != null) {
            txt = obj.toString();
        } // if

        if (!(comboBoxModel.isAnyPatternAllowed() || comboBoxModel.isMultiSelectionAllowed())) {
            /* In the case when *any* pattern is allowed, or all we want is to get a   *
             * listof items that match, then we do not update the comboBox editor      *
             * component with the newly selected item's key.                           */
            if (!isTableCellEditor()) {
                setText(txt);
            }
        } // if
        return txt;
    } // updateFcbEditor() method

    public boolean isTriggeredByKeyPress() {
        return triggeredByKeyPress;
    }

    public void setTriggeredByKeyPress(boolean argTriggeredByKeyPress) {
        triggeredByKeyPress = argTriggeredByKeyPress;
    }

    public Object getPickedItem() {
        return pickedItem;
    }

    public Object getPickedKey() {
        return pickedKey;
    }
} // ComboBoxFilter class

// $Id$
