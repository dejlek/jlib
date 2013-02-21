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

import java.awt.Dimension;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/**
 * This class implements a JComboBox which takes into account size of its components and make the pop-up
 * window wide enough.
 * When an instance of this combo-box is a table cell editor, by default it will popup whenever edit starts.
 * If you want to disable this behaviour, use the setPopupOnEditEnabled() method.
 * 
 * The code has been borrowed from http://www.jroller.com/santhosh/entry/make_jcombobox_popup_wide_enough
 * 
 * Santhosh took the code from the http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4618607 and improved
 * it further.
 */
public class WideComboBox extends JComboBox {
    private static final Logger LOGGER = Logger.getLogger(WideComboBox.class.getName());
    
    private boolean layingOut = false;
    private int ww;
    private boolean popupOnEditEnabled = true;
    private int maxWidth = -1; /// -1 indicates that it is not set
    

    /**
     * 
     */
    public WideComboBox() {
        super();
    } // WideComboBox() method

    /**
     * 
     * @param items
     */
    public WideComboBox(final Object[] items) {
        super(items);
    } // WideComboBox() method

    /**
     * 
     * @param items
     */
    public WideComboBox(Vector items) {
        super(items);
    } // WideComboBox() method

    /**
     * 
     * @param aModel
     */
    public WideComboBox(ComboBoxModel aModel) {
        super(aModel);
    } // WideComboBox() method

    /**
     * {@inheritDoc}
     *
     * If popupOnEditEnabled is set, then we show the pop-up whenever user edits the cell.
     *
    @Override
    public void processFocusEvent(FocusEvent fe) {
        super.processFocusEvent(fe);
        //System.out.println(fe.toString());
        if (popupOnEditEnabled && isTableCellEditor()) {
            System.out.println("popupFocusEvent");
            // if we have configured this WideComboBox instance to popup on edit
            // and if it is a table cell editor, then we show the popup
            // NOTE: it seems this solution does not work with editable combo-boxes... :(
            Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            if (isDisplayable() && fe.getID() == FocusEvent.FOCUS_GAINED
                    && focusOwner == this && !isPopupVisible()) {
                showPopup();
            } //  if
        } // if
    } // processFocusEvent() method
    * 
    */

    /**
     * @{@inheritDoc }
     */
    @Override
    public void doLayout() {
        try {
            layingOut = true;
            super.doLayout();
        } finally {
            layingOut = false;
        } // finally
    } // doLayout() method

    /**
     * @{@inheritDoc }
     * 
     * NOTE: Apparently, the width of the Dimension returned by getSize() is also the width of the popup! :)
     *
     * @return Dimension object containing a much better dimension than the one from the JComboBox.
     * 
     */
    @Override
    public Dimension getSize() {
        // what is the bigger pref. size? the top-level ancestor's, or mine?
        int tlaorme = Math.max(getTopLevelAncestor().getPreferredSize().width, 
                getPreferredSize().width);
        
        // it must not be bigger than this:
        int tlaormemax = 3 * getTopLevelAncestor().getSize().width / 4;
        
        // if it is, we limit it
        if (tlaorme > tlaormemax) {
            tlaorme = tlaormemax; 
        }
        
        // maxWidth grows until tlaormemax
        maxWidth = Math.max(tlaorme, maxWidth);
        
        Dimension dim = super.getSize(); 
        if (!layingOut) {
            ww = Math.max(ww, dim.width); 
            ww = Math.max(ww, getPreferredSize().width);
            if (ww > maxWidth) {
                // if calculated new width exceeds the preferred width of the main container, we limit it.
                ww = maxWidth;
            } // if
            dim.width = ww;
        } // if
        Dimension ret = new Dimension(ww, dim.height);
        
        return ret;
    } //  getSize() method

    /**
     * Use this method whenever you need to determine if the comboBox is used as a cell editor or not.
     * @return boolean Value indicating whether comboBox is a cell editor (TRUE) or not (FALSE).
     */
    private boolean isTableCellEditor() {
        boolean isTableCellEditor = false;
        Object tmp = getClientProperty("JComboBox.isTableCellEditor");
        if (tmp != null) {
            isTableCellEditor = tmp.equals(Boolean.TRUE);
        } // if
        return isTableCellEditor;
    } // isTableCellEditor() method

    /**
     * 
     * @return
     */
    public boolean isPopupOnEditEnabled() {
        return popupOnEditEnabled;
    } // isPopupOnEditEnabled() method

    /**
     * 
     * @param argPopupOnEditEnabled
     */
    public void setPopupOnEditEnabled(boolean argPopupOnEditEnabled) {
        popupOnEditEnabled = argPopupOnEditEnabled;
    } // setPopupOnEditEnabled() method
    
    /**
     * 
     * @return
     */
    public int getMaxWidth() {
        return maxWidth;
    } // getMaxWidth() method

    /**
     * 
     * @param argMaxWidth
     */
    public void setMaxWidth(int argMaxWidth) {
        System.out.println(">>>> " + argMaxWidth);
        maxWidth = argMaxWidth;
    } // setMaxWidth() method

} // WideComboBox class

// $Id$
