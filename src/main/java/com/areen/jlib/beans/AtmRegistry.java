/**
 * $Id$
 *
 * Copyright (c) 2009-2010 Areen Design Services Ltd
 * 23 Eyot Gardens; London; W6 9TR
 * http://www.areen.com
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of Areen Design Services Ltd ("Confidential 
 * Information").  You shall not disclose such Confidential Information and shall use it only in accordance 
 * with the terms of the license agreement you entered into with Areen Design Services Ltd.
 * 
 * This file is best viewed with 110 columns.
12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 * 
 * Author(s) in chronological order:
 *   Dejan Lekic , http://dejan.lekic.org
 * Contributor(s):
 *   -
 */

package com.areen.jlib.beans;

import java.beans.*;
import java.io.Serializable;
import java.util.HashMap;
import javax.swing.table.AbstractTableModel;

/**
 * This class implements a Java bean that is going to be used by:
 * 
 * 1) AreenTablePanel objects to be able to find an appropriate AreenTableModel that has been previously
 *    created by valid AreenTablePanel instance. - Say an application creates, an AreenTablePanel, then
 *    closes it for some reason, and after a while creates an AreenTablePanel object with the same dataset
 *    ID again. In such cases we can use AtmRegistry to find AreenTableModel, and reuse it.
 * 
 * 2) Any JComponent that needs to listen to dataset changes. A perfect example is a lookup combo-box that
 *    shows dataset values.
 * 
 * @author dejan
 */
public class AtmRegistry implements Serializable {
    
    // ====================================================================================================
    // ==== Variables =====================================================================================
    // ==================================================================================================== 
    
    // :::::: PRIVATE/PROTECTED :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    private HashMap<String, AbstractTableModel> areenTableModels;
    private PropertyChangeSupport propertySupport;
    
    // ====================================================================================================
    // ==== Constructors ==================================================================================
    // ====================================================================================================
    
    public AtmRegistry() {
        propertySupport = new PropertyChangeSupport(this);
    }
        
    // ====================================================================================================
    // ==== Public Methods ================================================================================
    // ====================================================================================================

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(propertyName, listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(propertyName, listener);
    }
    
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return propertySupport.getPropertyChangeListeners();
    }
    
    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        return propertySupport.getPropertyChangeListeners(propertyName);
    }
    
    // ====================================================================================================
    // ==== Accessors =====================================================================================
    // ====================================================================================================
    
    /**
     * Get a reference to the AreenTableModel object that deals with the dataset with the ID equal to
     * argModelID.
     *
     * @return NULL if there is no such dataset ID, or valid reference to an AreenTableModel object.
     */
    public AbstractTableModel get(String argModelID) {
        return areenTableModels.get(argModelID);
    } // get() method

    /**
     * Updates a reference to an AreenTableModel object with dataset ID equal to argModelID to point to 
     * a new AreenTableModel object with reference argATM.
     * 
     * If, however, old reference and the new reference point to the same object, the even will not be
     * fired.
     *
     * @param argATM A new AreenTableModel object.
     * @param argModelID A dataset ID.
     */
    public void set(String argModelID, AbstractTableModel argATM) {
        AbstractTableModel oldAtm = get(argModelID);
        if (oldAtm == argATM) {
            // if objects are the same, we do not change the property, no point in doing so.
            return;
        }
        areenTableModels.put(argModelID, argATM);
        propertySupport.firePropertyChange(argModelID, oldAtm, argATM);
    } // set() method


} // AtmRegistry class

// $Id$
