/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    public Component getListCellRendererComponent(JList<?> list, 
            Object value, 
            int index, 
            boolean isSelected, 
            boolean cellHasFocus) {
        Component ret = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Pair) {
            Pair p = (Pair) value;
            setText(p.getFirst() + " - " + p.getSecond());
        }
        return this;
    } // getListCellRendererComponent() method
    
} // ComboBoxPairRenderer class

// $Id$
