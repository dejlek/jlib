package com.areen.jlib.gui;

import java.awt.Dimension;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/**
 * This class implements a JComboBox which takes into account size of its components and make the pop-up
 * window wide enough.
 * 
 * The code has been borrowed from http://www.jroller.com/santhosh/entry/make_jcombobox_popup_wide_enough
 * 
 * Santhosh took the code from the http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4618607 and improved
 * it further.
 */
public class WideComboBox extends JComboBox {
    private boolean layingOut = false;
    private int ww;

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
        Dimension dim = super.getSize(); 
        if (!layingOut) {
            ww = Math.max(ww, dim.width); 
            ww = Math.max(ww, getPreferredSize().width);
            dim.width = ww;
        } // if
        return new Dimension(ww, dim.height);
    } //  getSize() method
    
} // WideComboBox class

// $Id$
