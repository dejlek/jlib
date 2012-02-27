/**
 * Project: jlib
 * Version: $Id$
 * License: SPL
 * 
 * Authors (in chronological order):
 *   Dejan Lekic - http://dejan.lekic.org
 * 
 * Contributors (in chronological order):
 *   -
 */

package com.areen.jlib.gui;

import java.awt.Dimension;
import java.util.Vector;
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
    private boolean layingOut = false;
    private int ww;
    private boolean popupOnEditEnabled = true;
    private int maxWidth = -1; /// -1 indicates that it is not set

    public WideComboBox() {
        super();
    }

    public WideComboBox(final Object[] items) {
        super(items);
    }

    public WideComboBox(Vector items) {
        super(items);
    }

    public WideComboBox(ComboBoxModel aModel) {
        super(aModel);
    }

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
        if ((maxWidth == -1) && (getTopLevelAncestor() != null)) {
            maxWidth = getTopLevelAncestor().getPreferredSize().width;
            maxWidth = 3 * maxWidth / 4; // We do not allow more than 3/4 of the max width for the combo box
        } // if
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
        return new Dimension(ww, dim.height);
    } //  getSize() method

    public boolean isPopupOnEditEnabled() {
        return popupOnEditEnabled;
    }

    public void setPopupOnEditEnabled(boolean argPopupOnEditEnabled) {
        popupOnEditEnabled = argPopupOnEditEnabled;
    }

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
    } // isTableCellEditor method

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int argMaxWidth) {
        maxWidth = argMaxWidth;
    }
    
} // WideComboBox class

// $Id$
