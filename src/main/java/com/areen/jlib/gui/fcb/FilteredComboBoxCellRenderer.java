/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.gui.fcb;

import com.areen.jlib.tuple.Pair;
import com.areen.jlib.util.Utility;
import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import sun.swing.DefaultLookup;

/**
 * A renderer for the FilteredComboBox. If you plan to use a filtered combo-box in a table, you have to
 * use this renderer as the default one will not correctly highlight the selected item.
 * 
 * @author dejan
 */
public class FilteredComboBoxCellRenderer extends DefaultListCellRenderer {
    /**
     * An empty <code>Border</code>. This field might not be used. To change the
     * <code>Border</code> used by this renderer override the
     * <code>getListCellRendererComponent</code> method and set the border
     * of the returned component directly.
     */
    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    
    private FilteredComboBoxModel cbModel;
    private int height;
    
    /**
     * Default FilteredComboBoxCell constructor.
     */
    public FilteredComboBoxCellRenderer() {
        super();
    } // FilteredComboBoxCell constructor (default)
    
    /**
     * 
     * @param argCBModel
     */
    public FilteredComboBoxCellRenderer(FilteredComboBoxModel argCBModel) {
        super();
        cbModel = argCBModel;
    } // FilteredComboBoxCell constructor
    
    /**
     * This is a straight copy from the DefaultListCellRenderer.
     * @return 
     */
    private Border getNoFocusBorder() {
        Border border = DefaultLookup.getBorder(this, ui, "List.cellNoFocusBorder");
        if (System.getSecurityManager() != null) {
            if (border != null) {
                return border;
            } // if
            return SAFE_NO_FOCUS_BORDER;
        } else {
            if (border != null && (noFocusBorder == null || noFocusBorder == DEFAULT_NO_FOCUS_BORDER)) {
                return border;
            } // if
            return noFocusBorder;
        } // else
    } // getNoFocusBorder() method

    
    /**
     * {@inheritDoc }
     */
    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
        /*
         * Following block fixes a weird problem with JComboBox - sometimes an item that has selected
         * flag in the model does not get selected in the combo box for some reason...
         * 
         * Thus, we do not rely on the isSelected parameter as it may not have correct value.
         */
        if (cbModel != null) {
            if (cbModel.getSelectedItem() == value) {
                isSelected = true;
            } else {
                isSelected = false;
            } // else
        } // if
        
        setComponentOrientation(list.getComponentOrientation());

        Color bg = null;
        Color fg = null;

        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation != null
                && !dropLocation.isInsert()
                && dropLocation.getIndex() == index) {

            bg = DefaultLookup.getColor(this, ui, "List.dropCellBackground");
            fg = DefaultLookup.getColor(this, ui, "List.dropCellForeground");

            isSelected = true;
        } // if

        if (isSelected) {
            setBackground(bg == null ? list.getSelectionBackground() : bg);
            setForeground(fg == null ? list.getSelectionForeground() : fg);
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        } // else
        
        if (value instanceof Icon) {
            setIcon((Icon) value);
            setText("");
        } else if (value instanceof Object[]) {
            setIcon(null);
            if (value == null) {
                setText("");
            } else {
                //setText(Arrays.toString((Object[]) value));
                setText(Utility.oa2string((Object[]) value, " - "));
            } // else
        } else if (value instanceof Pair) {
            setIcon(null);
            if (value == null) {
                setText("");
            } else {
                Pair pair = (Pair) value;
                setText(pair.getFirst() + " - " + pair.getSecond());
            } // else
        } else {
            setIcon(null);
            setText((value == null) ? "" : value.toString());
        } // else

        setEnabled(list.isEnabled());
        setFont(list.getFont());

        Border border = null;
        if (cellHasFocus) {
            if (isSelected) {
                border = DefaultLookup.getBorder(this, ui, "List.focusSelectedCellHighlightBorder");
            } // if
            if (border == null) {
                border = DefaultLookup.getBorder(this, ui, "List.focusCellHighlightBorder");
            } // if
        } else {
            border = getNoFocusBorder();
        } // else
        setBorder(border);

        return this;
    } // getListCellRendererComponent() method
    
} // FilteredComboBoxCell

// $Id$
