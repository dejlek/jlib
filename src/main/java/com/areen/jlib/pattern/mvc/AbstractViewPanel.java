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
package com.areen.jlib.pattern.mvc;

import java.beans.PropertyChangeEvent;
import javax.swing.JPanel;

/**
 * The base class for all the View classes that are part of the MVC pattern.
 * 
 * NOTE: The AbstractModel class is not in the com.aree.jlib.pattern.mvc package. Instead it is in the
 *       com.areen.jlib.model. Reason for this is that I hope it can be re-used in MVC, MVP and MVVM patterns.
 * 
 * @author Dejan
 */
public abstract class AbstractViewPanel extends JPanel {
    
    /**
     * Called by the controller when it needs to pass along a property change 
     * from a model.
     *
     * @param argEvent PropertyChangeEvent event from the model.
     */
    public abstract void modelPropertyChange(PropertyChangeEvent argEvent);
    
} // AbstractViewPanel class

// $Id$
