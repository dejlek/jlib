/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.areen.jlib.gui.attrib;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Logger;

/**
 *
 * @author dejan
 */
public class AttributesModel {
    /* Class implementation comment (if any)
     * 
     */
    
    // ====================================================================================================
    // ==== Variables =====================================================================================
    // ====================================================================================================
    
    // :::::: CONFIGURATION VARIABLES :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    // :::::: PUBLIC ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    // :::::: PRIVATE/PROTECTED :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    private static final Logger LOGGER = Logger.getLogger(AttributesModel.class.getName());
    
    private boolean silent = false;
    
    /**
     * Each attribute has a set of allowed values. If, say, we want user to be able to chose between
     * 'Y', 'N' and '?' (unspecified) for the first attribute, then allowedValues[0] will be "YN?".
     */
    private String[] allowedValues;
    private byte[] currentIndexes;
    private char[] defaultValues;
    private String[] titles;
    private String[] descriptions; /// Here we will store item descriptions.
    

    private int selectedAttributeIndex2;
    
    private Color selectedBackgroundColor;
    private Color backgroundColor;
    
    // ====================================================================================================
    // ==== Constructors ==================================================================================
    // ====================================================================================================
    
    
    
    /**
     * The simplest constructor which takes argAllowedValues. Note that we do not check for duplicates.
     * Developers must make sure there are no duplicate values.
     * @param argAllowedValues 
     */
    public AttributesModel(String[] argAllowedValues) {
        selectedBackgroundColor = Color.LIGHT_GRAY;
        backgroundColor = Color.decode("#ccccff");
        
        allowedValues = argAllowedValues;
        attributes = "";
        propertyChangeSupport = new PropertyChangeSupport(this);
        selectedAttributeIndex = -1; // indicates that no attribute is selected
        currentIndexes = new byte[argAllowedValues.length];
        
        defaultValues = new char[argAllowedValues.length];
        descriptions = new String[argAllowedValues.length];
        for (int i = 0; i < getNumberOfAttributes(); i++) {
            defaultValues[i] = allowedValues[i].charAt(0); // we set the first characters to be default
            attributes += defaultValues[i];
            descriptions[i] = "";
        } // for
        
        // Titles
        titles = new String[getNumberOfAttributes()];
        for (int i = 0; i < getNumberOfAttributes(); i++) {
            titles[i] = "";
        }
    }
    
    public AttributesModel(String[] argAllowedValues, char[] argDefaultValues) {
        this(argAllowedValues);
        defaultValues = argDefaultValues;
        String attrs = "";
        for (char ch : defaultValues) {
            attrs += ch;
        }
        setAttributes(attrs);
    }
    
    public AttributesModel(String[] argAllowedValues, String argInitialValues) {
        this(argAllowedValues);
        setAttributes(argInitialValues);
    } // Template constructor (default)
    
    // ====================================================================================================
    // ==== Interface/Superclass Methods ==================================================================
    // ====================================================================================================
    
    // :::: <Superclass> method overrides :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @Override
    public String toString() {
        return "AttributesModel{" + "silent=" + silent + ", allowedValues=" + allowedValues 
                + ", currentIndexes=" + currentIndexes + ", current=" + selectedAttributeIndex 
                + ", propertyChangeSupport=" + propertyChangeSupport + ", attributes=" + attributes + '}';
    }
    
    // :::: <Interface> method implementations ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    // ====================================================================================================
    // ==== Public Methods ================================================================================
    // ====================================================================================================
    
    public int getNumberOfAttributes() {
        return allowedValues.length;
    }
    
    public char getValue(int argIndex) {
        return attributes.charAt(argIndex);
    }
    
    /**
     * Use this method to change the value of the attribute with index argIndex.
     * New value will be the argValue.
     * @param argIndex
     * @param argValue 
     */
    public void setValue(int argIndex, char argValue) {
        char value = Character.toUpperCase(argValue);
        if (isValid(argIndex, value)) {
            if (getValue(argIndex) != value) {
                // the new value is valid, and different from what is currently the value of the attribute
                StringBuilder sb = new StringBuilder(attributes);
                sb.replace(argIndex, argIndex + 1, "" + value);
                setAttributes(sb.toString());
            } // if
        } // if
    } // setValue() method
    
    /**
     * Changes the value of an attribute with index argIndex to the next value.
     * It rotates.
     */
    public void next(int argIndex) {
        byte pos = currentIndexes[argIndex];
        int nextPos = pos + 1;
        if (nextPos >= allowedValues[argIndex].length()) {
            nextPos = 0;
        }
        currentIndexes[argIndex] = (byte) nextPos;
        setValue(argIndex, allowedValues[argIndex].charAt(nextPos));
    } // next() method
    
    /**
     * Changes the value of an attribute with index argIndex to the next value.
     * It rotates.
     */
    public void previous(int argIndex) {
        byte pos = currentIndexes[argIndex];
        int nextPos = pos - 1;
        if (nextPos < 0) {
            nextPos = allowedValues[argIndex].length() - 1;
        }
        currentIndexes[argIndex] = (byte) nextPos;
        setValue(argIndex, allowedValues[argIndex].charAt(nextPos));
    } // next() method
    
    /**
     * Moves selection to the next attribute.
     */
    public void nextAttribute() {
        int pos = getSelectedAttributeIndex();
        int nextPos = pos + 1;
        if (nextPos >= getNumberOfAttributes()) {
            nextPos = 0;
        }
        setSelectedAttributeIndex(nextPos);
    }
    
    /**
     * Moves selection to the previous attribute.
     */
    public void previousAttribute() {
        int pos = getSelectedAttributeIndex();
        int nextPos = pos - 1;
        if (nextPos < 0) {
            nextPos = getNumberOfAttributes() - 1;
        }
        setSelectedAttributeIndex(nextPos);
    }
    
    /**
     * This method is used to check whether given character argValue is a valid value for the
     * attribute with index equal to argIndex. It relies on indexOf() method actually.
     * 
     * @param argIndex Attribute index.
     * @param argValue Character to check for.
     * @return TRUE if valid, FALSE if not.
     * @see indexOf(int, char)
     */
    public boolean isValid(int argIndex, char argValue) {
        return (indexOf(argIndex, argValue) >= 0);
    } // isValid() method
    
    /**
     * Use this method to reset to defaults.
     */
    public void reset() {
        attributes = "";
        for (int i = 0; i < getNumberOfAttributes(); i++) {
            attributes += defaultValues[i];
        } // for
    }
    
    public String getDescription(int argIndex) {
        return descriptions[argIndex];
    }
    
    public void setDescription(int argIndex, String argDescription) {
        descriptions[argIndex] = argDescription;
    }
    
    // ====================================================================================================
    // ==== Accessors =====================================================================================
    // ====================================================================================================
    
    public String[] getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String[] argDescriptions) {
        descriptions = argDescriptions;
    }
    
    
    public String[] getTitles() {
        return titles;
    }

    public void setTitles(String[] argTitles) {
        for (int i = 0; i < getNumberOfAttributes(); i++) {
            if (descriptions[i].length() == 0) {
                descriptions[i] = argTitles[i];
            }
        } // if
        
        titles = argTitles;
    }
    
    public char[] getDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(char[] argDefaultValues) {
        defaultValues = argDefaultValues;
    }
    
    
    public Color getSelectedBackgroundColor() {
        return selectedBackgroundColor;
    }

    public void setCurrentBackgroundColor(Color argCurrentBackgroundColor) {
        selectedBackgroundColor = argCurrentBackgroundColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color argBackgroundColor) {
        backgroundColor = argBackgroundColor;
    }
    
    public String getTitle(int argIndex) {
        return titles[argIndex];
    }
    
    /**
     * WARNING: No bounds check!
     * @param argIndex
     * @param argTitle 
     */
    public void setTitle(int argIndex, String argTitle) {
        if (argIndex < titles.length) {
            titles[argIndex] = argTitle;
        }
    }
    
    // ====================================================================================================
    // ==== Private/Protected/Package Methods =============================================================
    // ====================================================================================================
    
    /**
     * Used internally to find the index of a given value argValue, in the string of allowed values for the
     * attribute with index argIndex.
     *
     * @param argIndex
     * @param argValue
     * @return 
     */
    private int indexOf(int argIndex, char argValue) {
        if (argIndex >= allowedValues.length) {
            return -1;
        }
        
        int ret = -1;
        
        for (int i = 0; i < allowedValues[argIndex].length(); i++) {
            char ch = allowedValues[argIndex].charAt(i);
            if (ch == argValue) {
                return i;
            }
        } // for
        
        return ret;
    } // indexOf() method
    

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
    
    // ::::: `attributes` property :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    private String attributes;

    public static final String PROP_ATTRIBUTES = "attributes";

    /**
     * Get the value of attributes
     *
     * @return the value of attributes
     */
    public String getAttributes() {
        return attributes;
    }

    /**
     * Set the value of attributes
     *
     * @param argAttributes new value of attributes
     */
    public final void setAttributes(String argAttributes) {
        String oldAttributes = attributes;
        String newAttributes = "";
        for (int i = 0; i < getNumberOfAttributes(); i++) {
            char newc = defaultValues[i];
            char ch;
            if (argAttributes.length() > i) {
                ch = argAttributes.charAt(i);
                if (isValid(i, ch)) {
                    newc = ch;
                }
            }

            if (i < argAttributes.length()) {
                if (isValid(i, argAttributes.charAt(i))) {
                    newc = argAttributes.charAt(i);
                }
            }
            
            newAttributes += newc;
        } // for

        if (!oldAttributes.equals(newAttributes)) {
            attributes = newAttributes;
            propertyChangeSupport.firePropertyChange(PROP_ATTRIBUTES, oldAttributes, attributes);
        }
    } // setAttributes() method
    
    // ::::: `descriptionsChanged` property ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    private boolean descriptionsChanged = false;

    public static final String PROP_DESCRIPTIONSCHANGED = "descriptionsChanged";

    /**
     * Get the value of descriptionsChanged
     *
     * @return the value of descriptionsChanged
     */
    public boolean descriptionsChanged() {
        return descriptionsChanged;
    }

    /**
     * Set the value of descriptionsChanged
     *
     * @param argDescriptionsChanged new value of descriptionsChanged
     */
    public void setDescriptionsChanged(boolean argDescriptionsChanged) {
        boolean oldDescriptionsChanged = descriptionsChanged;
        if (argDescriptionsChanged != oldDescriptionsChanged) {
            descriptionsChanged = argDescriptionsChanged;
            propertyChangeSupport.firePropertyChange(PROP_DESCRIPTIONSCHANGED, 
                oldDescriptionsChanged, argDescriptionsChanged);
        } // if
    } // setDescriptionsChanged() method
    
    // ::::: `selectedAttributeIndex` property :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    /**
     * Used to tell us what attribute is selected. If the value is -1 it means no attribute is selected.
     * This is the case most of the time with the AttributesEditableView as it is not used for editing, 
     * except with mouse.
     */
    private int selectedAttributeIndex = -1;

    public static final String PROP_SELECTEDATTRIBUTEINDEX = "selectedAttributeIndex";

    /**
     * Get the value of selectedAttributeIndex
     *
     * @return the value of selectedAttributeIndex
     */
    public int getSelectedAttributeIndex() {
        return selectedAttributeIndex;
    }

    /**
     * Set the value of selectedAttributeIndex
     *
     * @param argIndex new value of selectedAttributeIndex
     */
    public void setSelectedAttributeIndex(int argIndex) {
        int oldSelectedAttributeIndex = selectedAttributeIndex;
        if (selectedAttributeIndex != argIndex) {
            selectedAttributeIndex = argIndex;
            propertyChangeSupport.firePropertyChange(PROP_SELECTEDATTRIBUTEINDEX, 
                    oldSelectedAttributeIndex, selectedAttributeIndex);
        } // if
    }

    
    // ====================================================================================================
    // ==== Classes, Interfaces, Enums ====================================================================
    // ====================================================================================================
    
    // ====================================================================================================
    // ==== Main function =================================================================================
    // ====================================================================================================
    
	public static void main(String[] args) {
        // Allowed values
        String[] alvals = new String[] { "YN?", "ABCD", "TF" };
        
        // Initialisation
        AttributesModel model = new AttributesModel(alvals, "?AT");
        
        System.out.println(model.indexOf(0, '?')); // 2
        System.out.println(model.indexOf(1, 'A')); // 0
        System.out.println(model.indexOf(2, 'F')); // 1
        System.out.println(model.indexOf(3, 'X')); // -1, we do not have 4 attributes (idx = 3)
        
        model.setValue(0, 'N');
        System.out.println(model.getAttributes()); // NAT
        model.next(0); 
        System.out.println(model.getAttributes()); // NAT, because selected attribute was the first one
        model.next(0);
        System.out.println(model.getAttributes()); // ?AT
        
        System.out.println("------");
        model.setAttributes("");
        System.out.println(model.getAttributes());
        model.setAttributes("T");
        System.out.println(model.getAttributes());
        model.setAttributes("TC");
        System.out.println(model.getAttributes());
        model.setAttributes("TCF");
        System.out.println(model.getAttributes());
        
	} // main() function

    // ====================================================================================================
    // ==== Code borrowed from some other projects (with permission) ======================================
    // ====================================================================================================
	
    // ::::: BEGIN Fname Sname' code ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    // Source: http://www.example.com/article01.html
    //
    // More details about where you got code from
    // ....................................................................................................

    // External code paste here
    
    // ::::: END Fname Sname' code ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    // ====================================================================================================
    // ==== Autogenerated code ============================================================================
    // ====================================================================================================
	
} // Template class

// $Id$
