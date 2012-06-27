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

import com.areen.jlib.tuple.Pair;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author dejan
 */
public class ComboBoxPairRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, 
            Object value, 
            int index, 
            boolean isSelected, 
            boolean cellHasFocus) {
        //PMDUnusedLocalVariable: Component ret = super.getListCellRendererComponent(list, value, index, 
        //isSelected, cellHasFocus);
        if (value instanceof Pair) {
            Pair p = (Pair) value;
            setText(p.getFirst() + " - " + p.getSecond());
        } // if
        return this;
    } // getListCellRendererComponent() method
    
} // ComboBoxPairRenderer class

// $Id$
