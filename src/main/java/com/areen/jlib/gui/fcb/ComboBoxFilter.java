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
import com.areen.jlib.tuple.Pair;
import com.areen.jlib.util.Sise;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import org.apache.log4j.Logger;

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
    private boolean navigationKeyPressed = false; // UP, DOWN, PGUP, PGDOWN
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
    
    private String delimiter = " - ";
    
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

        /**
         * TODO: ComboBoxFilter should implement KeyListener interface, and we should move this anonymous
         *       class methods into ComboBoxFilter methods.
         */
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

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
                } // if

                if (comboBox.isDisplayable()) {
                    comboBox.setPopupVisible(true);
                } // if

                navigationKeyPressed = false;
                finish = false;
                int currentIndex = comboBox.getSelectedIndex();

                boolean pa = comboBoxModel.isAnyPatternAllowed();
                boolean ma = comboBoxModel.isMultiSelectionAllowed();
                int delta = 0;
                switch (keyCode) {
                    
                    case KeyEvent.VK_TAB:
                        handleTabKeyPress(pa, ma);
                        break;

                    case KeyEvent.VK_ESCAPE:
                        if (isTableCellEditor()) {
                            comboBoxModel.setCancelled(true);
                            comboBox.setSelectedItem(pickedItem);
                        } else {
                            if (pickedKey == null) {
                                ComboBoxFilter.this.setText("");
                            } else {
                                ComboBoxFilter.this.setText(pickedKey.toString());
                            } // else
                        } // else
                        break;

                    case KeyEvent.VK_ENTER:
                        handleEnter(pa, ma);
                        break;

                    case KeyEvent.VK_HOME:
                        navigationKeyPressed = true;
                        if (isTableCellEditor) {
                            if (comboBox.getItemCount() > 0) {
                                selectedIndex = 0;
                                comboBox.setSelectedIndex(selectedIndex);
                            } // if
                        } // if
                        break;

                    case KeyEvent.VK_END:
                        navigationKeyPressed = true;
                        if (isTableCellEditor) {
                            if (comboBox.getItemCount() > 0) {
                                selectedIndex = comboBox.getItemCount() - 1;
                                comboBox.setSelectedIndex(selectedIndex);
                            } // if
                        } // if
                        break;
                        
                    case KeyEvent.VK_PAGE_UP:
                        navigationKeyPressed = true;
                        
                        if (isTableCellEditor) {
                            System.out.println(selectedIndex);
                            System.out.println(comboBox.getSelectedIndex());
                            selectedIndex = currentIndex;
                            System.out.println(selectedIndex);
                        }
                        
                        /*
                        delta = -8;
                        if (isTableCellEditor) {
                            if ((selectedIndex == currentIndex) && (currentIndex > 0)) {
                                if (currentIndex < 8) {
                                    delta = -currentIndex;
                                }
                                comboBox.setSelectedIndex(currentIndex + delta);
                                selectedIndex = currentIndex + delta;
                            } else {
                                selectedIndex = currentIndex;
                            } // else
                            //arrowKeyPressed = false;
                        }
                        */
                        break;
                        
                    case KeyEvent.VK_PAGE_DOWN:
                        navigationKeyPressed = true;
                        
                        if (isTableCellEditor) {
                            System.out.println(selectedIndex);
                            System.out.println(comboBox.getSelectedIndex());
                            selectedIndex = currentIndex;
                            System.out.println(selectedIndex);
                        }
                        
                        /*
                        delta = 8;
                        if (isTableCellEditor) {
                            if ((selectedIndex == currentIndex)
                                    && (currentIndex < comboBox.getItemCount() - 1)) {
                                if (currentIndex > comboBox.getItemCount() - 8) {
                                    delta = comboBox.getItemCount() - currentIndex - 1;
                                }
                                comboBox.setSelectedIndex(currentIndex + delta);
                                selectedIndex = currentIndex + delta;
                            } else {
                                selectedIndex = currentIndex;
                            } // else
                            // we set this to false ONLY if the combo box is a cell editor!
                            //arrowKeyPressed = false;
                        } // if
                        */
                        break;
                        
                    case KeyEvent.VK_UP:
                        navigationKeyPressed = true;

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
                            } // else

                            // we set this to false ONLY if the combo box is a cell editor!
                            navigationKeyPressed = false;
                        } // if
                        break;

                    case KeyEvent.VK_DOWN:
                        navigationKeyPressed = true;
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
                            navigationKeyPressed = false;
                        } // if
                        break;

                    default:
                        selectedIndex = currentIndex;
                } // switch
                keyPressed = false;
            } // keyPressed() method

            private void handleEnter(boolean pa, boolean ma) {
                finish = true;
                comboBoxModel.setReadyToFinish(false); // we expect cell editor
                String txt = updateFcbEditor();
                if ((pa || ma)) {
                    if (txt == null) {
                        // do nothing
                    } else {
                        // we have to update the text here because updateFcbEditor won't
                        setText(txt); 
                        pickedItem = comboBox.getSelectedItem();
                        pickedKey = txt;
                        comboBoxModel.setPickedItem(pickedItem);
                        comboBoxModel.setPickedKey(txt);
                    } //else
                } else {
                    if (txt == null) {
                        // if user types a string that has no match, we select the last picked item.
                        comboBox.setSelectedItem(pickedItem);

                        // After all events are processed, alert the user
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                String eol = System.getProperty("line.separator");
                                JOptionPane.showMessageDialog(comboBox, 
                                        "Invalid option.");
                            } // run() method
                        }); // Runnable (anonymous) implementation
                    } else {
                        // setText(txt); // No need because updateFcbEditor will do it in this case
                        pickedItem = comboBox.getSelectedItem();
                        pickedKey = txt;
                        comboBoxModel.setPickedItem(pickedItem);
                        comboBoxModel.setPickedKey(txt);
                    } // else
                } // else
            } // handleEnter method

            private void handleTabKeyPress(boolean pa, boolean ma) {
                finish = true;
                comboBoxModel.setReadyToFinish(false);

                if (!isTableCellEditor()) {                           
                    String txt = updateFcbEditor();
                    String enteredText = "";
                    try {
                        enteredText = getText(0, getLength());
                    } catch (BadLocationException ex) {
                        Logger.getLogger(ComboBoxFilter.class.getName()).error(ex.toString());
                    }
                    if (pa || ma) {
                        // TODO
                    } else {
                        if ((txt == null) && !enteredText.isEmpty()) {
                            // After all events are processed, alert the user
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    String eol = System.getProperty("line.separator");
                                    JOptionPane.showMessageDialog(comboBox, 
                                            "Invalid option. Old value restored.");
                                    //ComboBoxFilter.this.setText(pickedKey.toString());
                                    comboBox.requestFocusInWindow();
                                } // run() method
                            }); // Runnable (anonymous) implementation
                        } // if
                    } // else
                } // if

                if ((comboBox.getSelectedItem() == null)) {
                    /*
                     * If TAB is pressed, but nothing is selected, and
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
                        Object obj = comboBox.getSelectedItem();
                        
                        boolean shouldUpdatePicked = false;
                        if (pickedItem == null) {
                            shouldUpdatePicked = true;
                        } else {
                            shouldUpdatePicked = pickedItem.getClass().equals(obj.getClass());
                        }
                        
                        if (shouldUpdatePicked) {
                            /* we need this block in the case when user:
                             * 1) navigates items with CURSOR KEYS, and then
                             * 2) presses TAB
                             * 3) the last selected item should be the one that is picked.
                             * NOTE: getSelectedItem() may give us a String. That is why we compare
                             *       the classes, and only if they DO match we set the picked key.
                             */
                            pickedItem = comboBox.getSelectedItem();
                            pickedKey = comboBoxModel.getKeyOfTheSelectedItem().toString();
                        } else {
                            System.out.println(obj.toString());
                        }
                    } // else
                } // if
                
                if (pickedItem != null) {
                    comboBoxModel.setPickedItem(pickedItem);
                    comboBoxModel.setPickedKey(pickedKey);
                    
                } // if
                
                if (!(pa || ma)) {
                    finish = false;
                    comboBox.setSelectedItem(pickedItem);
                }
                // At this point, the selected item should match the picked item
            }

        }; // KeyAdapter subclass (anonymous)
        
        // add the keyAdapter as *the first* KeyListener registered in the comboBoxEditor
        addAsTheFirstKeyListener(keyAdapter);

        // Bug 5100422 on Java 1.5: Editable JComboBox won't hide popup when tabbing out
        hidePopupOnFocusLoss = System.getProperty("java.version").startsWith("1.5");
        // Highlight whole text when focus gets lost
        comboBoxEditor.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                boolean pa = comboBoxModel.isAnyPatternAllowed();
                boolean ma = comboBoxModel.isMultiSelectionAllowed();

                if (pickedKey != null && !(pa || ma)) {
                    // When combo-box loses focus, we need to set the text to the selected
                    setText(pickedKey.toString());
                } else {
                    if (!(pa || ma)) {
                        setText("");
                    } // if
                } // else

                // Workaround for Bug 5100422 - Hide Popup on focus loss
                if (hidePopupOnFocusLoss) {
                    ComboBoxFilter.this.comboBox.setPopupVisible(false);
                } // if
                comboBoxModel.setReadyToFinish(false);
            } // focusLost() method

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
            } // popupMenuWillBecomeVisible() method

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent pme) {
                comboBox.putClientProperty("item-picked", Boolean.FALSE);
            } // popupMenuWillBecomeInvisible() method

            @Override
            public void popupMenuCanceled(PopupMenuEvent pme) {
                // do nothing
            } // popupMenuCanceled() method
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
        } // if
        
        if (!isTableCellEditor()) {
            if ((pickedItem == null) && (comboBox.getItemCount() > 0)) {
                comboBox.setSelectedIndex(0);
                pickedItem = comboBox.getSelectedItem();
                Object tmpKey = comboBoxModel.getKeyOfTheSelectedItem();
                if (tmpKey instanceof String) {
                    pickedKey = tmpKey.toString();
                } else {
                    pickedKey = tmpKey;
                } // else
            } // if
        } // if
    } // ComboBoxFilter() method

    /**
     * User typically will call this method from a table cell editor when we
     * want to "inform" filtered combo-box that we want to re-filter before we
     * actually start editing. 
     * 
     * The reason for this is when user goes to another
     * cell, the filtered set of entries from the previous cell will not apply.
     *
     * @param argPattern
     */
    public void prepare(Object argPattern) {

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
        } // if

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
                } // if
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
            } else {
                if (!pat.isEmpty()) {
                    // depending on the configuration, we set text...
                    LOGGER.info(pickedItem.toString());
                    LOGGER.info(pickedItem.getClass().getCanonicalName());
                    LOGGER.info(getConfig());
                    LOGGER.info("-----");

                    // Do not worry, this reference will change below.
                    // I just want to make it sure that in the case we got a null, we still have an 
                    // "useful" Pair
                    Pair pair = new Pair(pickedKey, pickedKey); 

                    if (pickedItem instanceof Pair) {
                        pair = (Pair) pickedItem;
                    } else if (pickedItem instanceof Object[]) {
                        // In the case we got Object[], not Pair, we have to use the model to get the Pair
                        pair = comboBoxModel.getKeyValuePairForElementAt(selectedIndex);
                    }

                    // Now we should have Pair object, we can set the text
                    if (getConfig() == 2) {
                        setText(pair.getSecond().toString());
                    } else if (getConfig() == 3) {
                        setText(pair.getFirst().toString() + getDelimiter() + pair.getSecond().toString());
                    } // else if
                }
            } // else
        } catch (BadLocationException ex) {
            Logger.getLogger(ComboBoxFilter.class.getName()).error(ex);
        } // catch
        if (isTableCellEditor()) {
            comboBoxModel.setReadyToFinish(false);
        } // if
        inPreparation = false;
    } // prepare() method

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        // return immediately when selecting an item
        if (selecting) {
            return;
        } // if

        if (navigationKeyPressed) {
            navigationKeyPressed = false;
            comboBox.putClientProperty("item-picked", Boolean.FALSE);
            return;
        } // if
            
        boolean itemPicked = false; // we need this value because item-picked may change due to chain
        // of events
        // inform action-performed listeners that the item has been picked so they may update
        // some other components
        if (keyPressed) {
            itemPicked = false;
        } else {
            itemPicked = true;
        } // else
        boolean isPicked = (Boolean) comboBox.getClientProperty("item-picked");
        comboBox.putClientProperty("item-picked", itemPicked || isPicked);
        
        // TODO: FIXME, when user presses PgDn and PgUp Object[] is picked and insertString is
        //       actually inserting String like "[Ljava.lang.Object;@1410770" 
        //       This is a quick hack to not confuse users.
        if (str.contains("[Ljava.lang.Object")) {
            int idx = comboBoxModel.getKeyIndex();
            if (isTableCellEditor()) {
                comboBoxModel.setReadyToFinish(true);
            } // if
            pickedItem = comboBox.getSelectedItem();
            boolean isObjectArr = (pickedItem instanceof Object[]);
            if (pickedItem != null) {
                if (isObjectArr) {
                    Object[] pickedItemObjArr = (Object[]) pickedItem;
                    if (pickedItemObjArr != null) {
                        if (pickedItemObjArr.length >= idx) {
                            Object keyOfPickedItem = pickedItemObjArr[idx];
                            if (keyOfPickedItem != null) {
                                str = keyOfPickedItem.toString();
                            } // if
                        } // if
                    } // if
                } // if
            } // if
            super.insertString(offs, str, a);
            
            filterTheModel();

            if (itemPicked) {
                Object tmp = comboBoxModel.getKeyOfTheSelectedItem();
                if (itemPicked && isObjectArr) {
                    pickedKey = tmp.toString();
                    
                } // if
            } // if
            comboBox.putClientProperty("item-picked", Boolean.TRUE);
            comboBoxModel.setPickedItem(pickedItem);
            comboBoxModel.setPickedKey(pickedKey);
            return;
        } // if
        
        System.out.println("STR: " + str);
        // insert the string into the document
        if (str.contains(Sise.UNIT_SEPARATOR_STRING)) {
            System.out.println("blah!");
            // we got a string in the Sise format, that must be because user picked an item with a mouse
            // in that case, we will take the key component (SISE unit) and put that instead.
            String[] strs = Sise.units(str);
            int idx = comboBoxModel.getKeyIndex();
            if (isTableCellEditor()) {
                comboBoxModel.setReadyToFinish(true);
            } // if
            
            // This is an ArrayOutOfBounds exception "fix". When user presses SPACE + ENTER sometimes we get
            // an error, because strs is an empty array!
            if (strs.length > 0) {
                super.insertString(offs, strs[idx], a);
            } else {
                super.insertString(offs, "", a);
            } // else

            // We have to filter after the user selects an item with the mouse.
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
            comboBoxModel.setPickedItem(pickedItem);
            comboBoxModel.setPickedKey(pickedKey);
            return;
        } else {
            // otherwise, insert the whole string
            super.insertString(offs, str, a);
            Object obj = comboBoxModel.getSelectedItem();
            if (obj != null) {
                if (itemPicked && !(obj instanceof Pair) && !(obj instanceof Object[])) {
                    comboBoxModel.setPickedItem(obj);
                    comboBoxModel.setPickedKey(obj);
                } // if
            } // if
        } // else

        if (finish) {
            return;
        } // if
        
        filterTheModel();
    } // insertString() method

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        // return immediately when selecting an item
        if (selecting) {
            // remove() is called whenever setSelectedItem() or setSelectedIndex() are called. They may be
            // called during the filtering process, so we do not want to remove while in the middle.
            return;
        } // if

        if (navigationKeyPressed) {
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
     * Use this method when you need to programmatically pick an item.
     * @param argItem 
     */
    public void pickItem(Object argItem) {
        Object obj = comboBoxModel.getKeyOfAnItem(argItem);
        if ((argItem == null) && (obj == null)) {
            return;
        } // if 
        pickedItem = argItem;
        pickedKey = obj.toString();
    } // pickItem() method

    // ====================================================================================================
    // ==== Accessors =====================================================================================
    // ====================================================================================================

    public int getConfig() {
        return comboBoxModel.getConfig();
    }

    public void setConfig(int argConfig) {
        comboBoxModel.setConfig(argConfig);
    }
    
    /**
     * 
     * @return
     */
    public boolean isTriggeredByKeyPress() {
        return triggeredByKeyPress;
    } // isTriggeredByKeyPress() method

    /**
     * 
     * @param argTriggeredByKeyPress
     */
    public void setTriggeredByKeyPress(boolean argTriggeredByKeyPress) {
        triggeredByKeyPress = argTriggeredByKeyPress;
    } // setTriggeredByKeyPress() method

    /**
     * 
     * @return
     */
    public Object getPickedItem() {
        return pickedItem;
    } // getPickedItem() method

    /**
     * 
     * @return
     */
    public Object getPickedKey() {
        return pickedKey;
    } // getPickedKey() method
    
    public void setModel(FilteredComboBoxModel argModel) {
        // TODO: we must add more stuff here, update the stuff, etc.
        comboBoxModel = argModel;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String argDelimiter) {
        delimiter = argDelimiter;
    }

    // ====================================================================================================
    // ==== Private/Protected/Package Methods =============================================================
    // ====================================================================================================
    
    private void clearTextSelection() {
        if (comboBoxEditor.getSelectedText() != null) {
            // we have a selected text, removing the selection. On Windows text may become selected by default
            int pos = comboBoxEditor.getCaretPosition();
            comboBoxEditor.select(0, 0);
            // return caret position to the original place
            comboBoxEditor.setCaretPosition(pos);
        } // if
    } // clearTextSelection() method

    private void setText(String text) {
        try {
            // remove all text and insert the completed string
            super.remove(0, getLength());
            super.insertString(0, text, null);
        } catch (BadLocationException e) {
            throw new RuntimeException(e.toString());
        } // catch
    } // setText() method

    /**
     * This method is a leftover from the previous version of the
     * ComboBoxFilter. Should be removed after the testing phase.
     */
    private void highlightCompletedText(int start) {
        comboBoxEditor.setCaretPosition(getLength());
        comboBoxEditor.moveCaretPosition(start);
    } // highlightCompletedText() method

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
        comboBoxModel.setPattern(pattern);

        clearTextSelection();
        fixPopupSize();

        comboBoxModel.setReadyToFinish(oldValue); // restore the value
        selecting = false;
        selectedIndex = comboBox.getSelectedIndex();
    } // filterTheModel() method

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
        } // if
        return isTableCellEditor;
    } // isTableCellEditor() method

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
        } // if
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
            comboBox.setUI(ColorArrowUI.createUI(comboBox));
        } // if
    } // fixComboBoxArrowUI() method
    
    private String updateFcbEditor() {
        Object obj = comboBoxModel.getKeyOfTheSelectedItem();
        String txt = null;
        if (obj != null) {
            txt = obj.toString();
        } // if 
        if (txt != null) {
            if (!comboBoxModel.isAnyPatternAllowed() || !comboBoxModel.isMultiSelectionAllowed()) {
                /* In the case when *any* pattern is allowed, or all we want is to get a   *
                 * list of items that match, then we do not update the comboBox editor      *
                 * component with the newly selected item's key.                           */
                if (!isTableCellEditor()) {
                    setText(txt);
                } // if
            } // if
        } // if
        return txt;
    } // updateFcbEditor() method
    
    /**
     * This method is used internally to insert the ComboBoxFilter's internal KeyAdapter object as
     * *the first* key listener of the combo box editor component. 
     * 
     * Other key listeners will come after.
     * 
     * When ComboBoxFilter is instantiated some key listeners may already be installed in the comboBoxEditor.
     * This method fixes that.
     * 
     * @param argKeyAdapter 
     */
    private void addAsTheFirstKeyListener(KeyAdapter argKeyAdapter) {
        // get old key listeners
        KeyListener[] keyListeners = comboBoxEditor.getKeyListeners();
        
        if (keyListeners.length == 0) {
            // if there are no other key listeners installed, install the internal one and return.
            comboBoxEditor.addKeyListener(argKeyAdapter);
        } else {
            // if there were some key listeners do the following:
            
            // remove all
            for (KeyListener kl : keyListeners) {
                comboBoxEditor.removeKeyListener(kl);
            }

            // now add ComboBoxFilter's KeyAdapter as the first:
            comboBoxEditor.addKeyListener(argKeyAdapter);

            // now add all old key listeners
            for (KeyListener kl : keyListeners) {
                comboBoxEditor.addKeyListener(kl);
            }
        } // else
    } // addAsTheFirstKeyListener() method
    
} // ComboBoxFilter class

// $Id$
