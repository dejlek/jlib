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
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.BitSet;
import javax.swing.AbstractListModel;

/**
 *
 * @author Dejan
 * @param <E> an element type which extends the ValueObject type.
 */
public class VoListModel<E extends SimpleObject> 
        extends AbstractListModel {
    ArrayList<E> data;
    BitSet pickedItems;
    Class<E> elementClass;
    
    public VoListModel() {
        data = new ArrayList<E>();
        pickedItems = new BitSet();
        propertyChangeSupport = new PropertyChangeSupport(this);
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
        updateNumberOfPickedItems();
    }
    
    public void setPickedItem(int argIndex, boolean argValue) {
        pickedItems.set(argIndex, argValue);
        updateNumberOfPickedItems();
    }
    
    public void togglePickedItem(int argIndex) {
        pickedItems.flip(argIndex);
        updateNumberOfPickedItems();
    }
    
    // ====================================================================================================
    // ==== Bean variables and methods ====================================================================
    // ====================================================================================================

    // These are the common building-blocks we need for handy property change support
    
    private final PropertyChangeSupport propertyChangeSupport;
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }
    
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return propertyChangeSupport.getPropertyChangeListeners();
    }
    
    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        return propertyChangeSupport.getPropertyChangeListeners(propertyName);
    }
    
    // ::::: <name> property ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    private int numberOfPickedItems = 0;
    public static final String PROP_NUMBEROFPICKEDITEMS = "numberOfPickedItems";
    
    /**
     * Get the value of exampleProperty
     *
     * @return the value of exampleProperty
     */
    public int getNumberOfPickedItems() {
        return pickedItems.cardinality();
    }

    /**
     * This PRIVATE method is used by the VoListModel to fire property change event whenever cardinality of
     * the pickedItems BitSet is changed.
     * 
     * NOTE: this method is safe to call with any argument.
     *
     * @param argRowMarked new value of exampleProperty
     */
    private void updateNumberOfPickedItems() {
        int oldNumber = numberOfPickedItems;
        int argNumber = pickedItems.cardinality();
        
        if ((oldNumber == argNumber) || argNumber >= pickedItems.size()) {
            return;
        }
        
        numberOfPickedItems = argNumber;
        propertyChangeSupport.firePropertyChange(PROP_NUMBEROFPICKEDITEMS, oldNumber, argNumber);
    } // setNumberOfPickedItems() method
    
} // VoListModel class

// $Id$
