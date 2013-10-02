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
package com.areen.jlib.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Base class for all Model classes.
 * 
 * NOTE: The AbstractModel class is not in the com.aree.jlib.pattern.mvc package. Instead it is in the
 *       com.areen.jlib.model. Reason for this is that I hope it can be re-used in MVC, MVP and MVVM patterns.
 * 
 * Code taken from:
 * http://www.oracle.com/technetwork/articles/javase/index-142890.html
 */
public abstract class AbstractModel {

    protected PropertyChangeSupport propertyChangeSupport;

    public AbstractModel() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
} // AbstractModel class

// $Id$