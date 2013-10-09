/**
 * Project: jlib
 * Version: $Id$
 * License: SPL
 * 
 * Authors (in chronological order):
 *   Dejan Lekic - http://dejan.lekic.org
 * Contributors (in chronological order):
 *   -
 */

package com.areen.jlib.gui.model;

import com.areen.jlib.model.SimpleObject;
import java.util.ArrayList;
import java.util.BitSet;
import javax.swing.AbstractListModel;

/**
 *
 * @author Dejan
 * @param <E> an element type which extends the ValueObject type.
 */
public class VoListModel<E extends SimpleObject> 
        extends AbstractListModel<E> {
    ArrayList<E> data;
    BitSet pickedItems;
    Class<E> elementClass;
    
    public VoListModel() {
        data = new ArrayList<E>();
        pickedItems = new BitSet();
    }
    
    // ====================================================================================================
    // ==== Interface/Superclass Methods ==================================================================
    // ====================================================================================================
    
    // ::::: AbstractListModel :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    /**
     * {@inheritDoc }
     */
    @Override
    public int getSize() {
        return data.size();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public E getElementAt(int index) {
        return data.get(index);
    } // getElementAt() method
    
    // ====================================================================================================
    // ==== Public Methods ================================================================================
    // ====================================================================================================
    
    public boolean add(E argElement) {
        boolean ret = data.add(argElement);
        if (ret) {
            pickedItems.set(data.size(), false);
        }
        return ret;
    }
    
    public void add(int argIndex, E argElement) {
        data.add(argIndex, argElement);
        fireIntervalAdded(this, argIndex, argIndex);
    }
    
    public boolean isEmpty() {
        return data.isEmpty();
    }
    
    public boolean isPicked(int argIndex) {
        return pickedItems.get(argIndex);
    }
    
    public void setPickedItem(int argIndex) {
        pickedItems.set(argIndex);
    }
    
    public void setPickedItem(int argIndex, boolean argValue) {
        pickedItems.set(argIndex, argValue);
    }
    
    public void togglePickedItem(int argIndex) {
        pickedItems.flip(argIndex);
    }
    
    public int getNumberOfPickedItems() {
        return pickedItems.cardinality();
    }
    
} // VoListModel class

// $Id$
