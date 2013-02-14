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

import com.areen.jlib.gui.ConfigurableListCellRenderer;
import com.areen.jlib.tuple.Pair;
import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;
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
public class FilteredComboBoxCellRenderer 
        extends DefaultListCellRenderer 
        implements ConfigurableListCellRenderer {
    
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
    private Configuration config;
    private String delimiter;
    
    /**
     * Default FilteredComboBoxCell constructor.
     */
    public FilteredComboBoxCellRenderer() {
        super();
        delimiter = " - ";
        config = Configuration.CodeAndValue;
    } // FilteredComboBoxCell constructor (default)
    
    /**
     * 
     * @param argCBModel
     */
    public FilteredComboBoxCellRenderer(FilteredComboBoxModel argCBModel) {
        this();
        delimiter = " - ";
        cbModel = argCBModel;
    } // FilteredComboBoxCell constructor
    
    // ====================================================================================================
    // ==== Interface/Superclass Methods ==================================================================
    // ====================================================================================================
    
    // :::: ConfigurableListCellRenderer ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @Override
    public int getConfig() {
        return config.ordinal();
    }

    @Override
    public void setConfig(int argConfig) {
        switch (argConfig) {
            case 0:
                config = Configuration.Default;
                break;
            case 1:
                config = Configuration.Code;
                break;
            case 2:
                config = Configuration.Value;
                break;
            case 3:
                config = Configuration.CodeAndValue;
                break;
                
            default:
                config = Configuration.CodeAndValue;
        } // switch
    } // setConfig(0 method

    // :::: DefaultListCellRenderer :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
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
                Object[] row = (Object[]) value;
                setText(getText(row));
            } // else
        } else if (value instanceof Pair) {
            setIcon(null);
            if (value == null) {
                setText("");
            } else {
                Pair pair = (Pair) value;
                setText(getText(pair));
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

    // ====================================================================================================
    // ==== Public Methods ================================================================================
    // ====================================================================================================
    
    // ====================================================================================================
    // ==== Accessors =====================================================================================
    // ====================================================================================================

    /**
     * Introduced this method to set the combobox model, when the model is changed on the fly.
     * DL & SN discussed.
     * 
     * @param argcombBoxModel 
     */
    public void setComboBoxModel(FilteredComboBoxModel argcombBoxModel) {
        cbModel = argcombBoxModel;
    } // setComboBoxModel()   
    
    // ====================================================================================================
    // ==== Private/Protected/Package Methods =============================================================
    // ====================================================================================================
    
    /**
     * Depending on the value of the `config` variable
     * @param argValue
     * @return 
     */
    private String getText(Pair argValue) {
        String ret;
        switch (config) {
            case Code: 
                ret = argValue.getFirst().toString();
                break;
            case Value:
                ret = argValue.getSecond().toString();
                break;
            case CodeAndValue:
                ret = argValue.getFirst().toString() + delimiter + argValue.getSecond().toString();
                break;
            default:
                ret = argValue.getFirst().toString() + delimiter + argValue.getSecond().toString();
        } // switch
        return ret;
    } // getText() method
    
    /**
     * @param argArray
     * @return 
     */
    private String getText(String[] argArray) {
        String ret;
        switch (config) {
            case Code: 
                // we assume always the first element in the argArray is the code
                ret = argArray[0];
                break;
            case Value:
                ret = argArray[1];
                for (String el : Arrays.copyOfRange(argArray, 2, argArray.length)) {
                    ret = ret + delimiter + el;
                } // foreach
                break;
            case CodeAndValue:
                ret = argArray[0];
                for (String el : Arrays.copyOfRange(argArray, 1, argArray.length)) {
                    ret = ret + delimiter + el;
                } // foreach
                break;
            default:
                ret = argArray[0];
                for (String el : Arrays.copyOfRange(argArray, 1, argArray.length)) {
                    ret = ret + delimiter + el;
                } // foreach
        } // switch
        return ret;
    }
    
    private String getText(Object[] argArray) {
        Object[] row = argArray;
        
        if (cbModel != null) {
            if (cbModel.isTableModelInUse()) {
                int[] cols = cbModel.getColumns();
                String[] strs = new String[cols.length];
                strs[0] = row[cols[0]].toString();
                for (int i = 1; i < cols.length; i++) {
                    strs[i] = row[cols[i]].toString();
                }
                return getText(strs);
            } // if
        } // if
        
        String[] strs = new String[argArray.length];
        int cnt = 0;
        for (Object obj : argArray) {
            strs[cnt] = obj.toString();
            ++cnt;
        } // foreach
        return getText(strs);
    } // getText() method
    
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
    
    // ====================================================================================================
    // ==== Classes and Enums =============================================================================
    // ====================================================================================================
    
    public enum Configuration {
        Default,
        Code,
        Value,
        CodeAndValue
    } // Configuration enum
    
} // FilteredComboBoxCell

// $Id$
