/**
 * $Id$
 *
 * Copyright (c) 2009-2013 Areen Design Services Ltd 23 Eyot Gardens; London; W6 9TR
 * http://www.areen.com All rights reserved.
 *
 * This software is the confidential and proprietary information of Areen Design Services Ltd
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with Areen Design
 * Services Ltd.
 *
 * This file is best viewed with 110 columns.
 * 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567
 *
 * Author(s) in chronological order: 
 *   Mateusz Dykiert , http://dykiert.org 
 * Contributor(s): -
 */

package com.areen.jlib.gui.accordion;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * Data model for Accordion. TitledPane and its weights, set size and state are stored in
 * AccordionPane object. There are two lists, one for AccordionPanes, and the other for JSeparators.
 *
 * Title size and separator size are set here.
 *
 * @author Matthew
 */
public class AccordionModel {

    // ====================================================================================================
    // ==== Variables =====================================================================================
    // ====================================================================================================
    private int fontSize = 15;
    private int titleSize = 22;
    private int separatorSize = 10;
    // :::::: PUBLIC ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    // :::::: PRIVATE/PROTECTED :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private ArrayList<AccordionPane> panes;
    private ArrayList<JLabel> separators;
    private boolean horizontal;
    private int paneCount;
    private Dimension minimumDimension;

    // ====================================================================================================
    // ==== Constructors ==================================================================================
    // ====================================================================================================
    public AccordionModel(boolean argHorizontal) {
        panes = new ArrayList<AccordionPane>();
        separators = new ArrayList<JLabel>();
        this.horizontal = argHorizontal;
        propertyChangeSupport = new PropertyChangeSupport(this);
    } // Template constructor (default)

    // ====================================================================================================
    // ==== Public Methods ================================================================================
    // ====================================================================================================
    /**
     * Adds the pane to the model (creates the data structure behind the scenes)
     *
     * @param titledPane
     */
    public void addPane(TitledPane titledPane) {
        AccordionPane accordionPane = new AccordionPane(titledPane, false, 0.2); // collapsed by default

        //set up rotatable pane
        RotatableTitle rt = ((RotatableTitle) titledPane.getTitle());
        rt.setFontSize(fontSize);
        rt.setDimension(new Dimension(titleSize, titleSize));

        //add it to the list
        panes.add(accordionPane);

        //increase the size of the model
        paneCount++;
    }

    public void clearAllDimensions() {
        AccordionPane pane;
        for (int i = 0; i < panes.size(); i++) {
            pane = panes.get(i);
            if (pane.isResizable()) {
                pane.resetToDefaultWeight();
            }
        }
    }

    /**
     * Collapses all panes
     */
    public void expandAll() {
        for (AccordionPane pane : panes) {
            if (!pane.isLocked()) {
                pane.setExpanded(true);
            }
        }
    }

    /**
     * Collapses all panes
     */
    public void collapseAll() {
        for (AccordionPane pane : panes) {
            if (!pane.isLocked()) {
                pane.setExpanded(false);
            }
        }
    }

    /**
     * Trigger state change of specified object
     *
     * @param pane
     */
    public void triggerStateChange(TitledPane pane) {
        //get the AccordionPane which contains this TitledPane
        AccordionPane accordionPane = getByTitledPane(pane);

        // change the state
        accordionPane.setExpanded(!accordionPane.isExpanded());
    }

    /**
     * Calculates total weight of expanded panes
     *
     * @return
     */
    public double getTotalWeights() {
        double weight = 0.0;

        //iterate and add up the weights - expanded and uses weights -
        // - no dimension set
        for (AccordionPane pane : panes) {
            if (pane.isExpanded() && pane.getDimension() == null) {
                weight += pane.getWeight();
            }
        }

        return weight;
    } // getTotalWeights()
    
    /**
     * Calculate how much space is taken by expanded panes
     * @return 
     */
    public int getSpaceTaken() {
        int space = 0;
        
         for (AccordionPane pane : panes) {
            if (pane.isExpanded() && pane.getDimension() == null) {
                if (horizontal) {
                   space += pane.getTitledPane().getWidth();
                } else {
                   space += pane.getTitledPane().getHeight();
                }
            }
        }
        
        return space;
    }

    /**
     * Get the weight share of the pane at index
     *
     * @param index
     * @return
     */
    public double getWeightShare(int index) {
        return panes.get(index).getWeight() / getTotalWeights();
    } // getWeightShare()

    /**
     * Calculates occupied total horizontal space, i.e. it does not count panes which have no width
     * specified (the ones that are used). Collapsed panes have specified globally specified width.
     *
     * @return
     */
    public int getOccupiedHorizontalSpace() {
        int space = 0;

        // calculate the space taken by 
        for (AccordionPane pane : panes) {     
            if (!pane.isExpanded()) {
                space += titleSize;
            } else if (!pane.isResizable()) {
                space += pane.getDimension().getWidth();
            }
        } // for

        //add space taken up by dividers
        space += separators.size() * separatorSize;

        return space;
    } // getOccupiedHorizontalSpace()

    /**
     * Calculates occupied total vertical space, i.e. it does not count panes which have no height
     * specified (the ones that are used). Collapsed panes have specified globally specified height.
     *
     * @return
     */
    public int getOccupiedVerticalSpace() {
        int space = 0;

        // calculate the space taken by 
        for (AccordionPane pane : panes) {
               if (!pane.isExpanded()) {
                space += titleSize;
            } else if (!pane.isResizable()) {
                space += pane.getDimension().getHeight();
            }
        } // for

        //add space taken up by dividers
        space += panes.size() * separatorSize;

        return space;
    } // getOccupiedVerticalSpace()

     /**
     * Calculates available space in the container. It takes total height or width depending on the
     * orientation of the layout. If column it will return height remaining, width otherwise. It
     * only looks for the space to be taken by expanded panes and collapsed specified size.
     *
     * @return
     */
    public int getAvailableSpace(Container c) {
        //calculate
        if (isHorizontal()) {
            return c.getWidth() - getOccupiedHorizontalSpace();
        } else {
            return c.getHeight() - getOccupiedVerticalSpace();
        } //else
    }
    
    /**
     * Add separator
     *
     * @param separator
     */
    public void addSeparator(JLabel separator) {
        separators.add(separator);
    }

    /**
     * Get index of an AccordionPane
     */
    public int indexOf(AccordionPane pane) {
        return panes.indexOf(pane);
    }

    /**
     * Get index of a TitledPane
     *
     * @param pane
     */
    public int indexOf(TitledPane pane) {
        for (int i = 0; i < paneCount; i++) {
            if (panes.get(i).getTitledPane().equals(pane)) {
                return i;
            }
        }

        return -1; //not found
    }

    /**
     * Get indexes of expanded panes
     *
     * @return
     */
    public LinkedList<Integer> getExpanded() {
        LinkedList<Integer> list = new LinkedList<Integer>();

        for (int i = 0; i < paneCount; i++) {
            if (panes.get(i).isExpanded()) {
                list.add(i);
            }
        } //for

        return list;
    }

    /**
     * Calculate highest titled pane
     *
     * @return
     */
    public int calculateHighestTitledPane() {
        int highest = 0;
        int height;

        // iterate through all panes
        for (AccordionPane pane : panes) {
            //get preferred height
            height = (int) pane.getTitledPane().getPreferredSize().getHeight();

            // if it's bigger set highest
            if (height > highest) {
                highest = height;
            }
        }

        return highest;
    }

    /**
     * Calculate widest titled pane
     *
     * @return
     */
    public int calculateWidestTitledPane() {
        int widest = 0;
        int width;

        // iterate through all panes
        for (AccordionPane pane : panes) {
            //get preferred width
            width = (int) pane.getTitledPane().getPreferredSize().getWidth();

            // if wider set it so
            if (width > widest) {
                widest = width;
            }
        }
        return widest;
    }

    /**
     * Get the greatest minimum width of all panes
     *
     * @return
     */
    public int calculateGreatestMinimumWidth() {
        int greatest = 0;
        int width;

        // iterate and find the greatest
        for (AccordionPane pane : panes) {
            width = pane.getTitledPane().getMinimumSize().width;

            //if it's the greatest so far.. store it
            if (width > greatest) {
                greatest = width;
            } // if
        } // foreach

        return greatest;
    } // calculateGreatestMinimumWidth;

    /**
     * Calculate greatest minimum height from all panes
     *
     * @return
     */
    public int calculateGreatestMinimumHeight() {
        int greatest = 0;
        int height;

        // iterate and find the greatest
        for (AccordionPane pane : panes) {
            height = pane.getTitledPane().getMinimumSize().height;

            //if it's the greatest so far.. store it
            if (height > greatest) {
                greatest = height;
            }
        }

        return greatest;
    }

    /**
     * Calculate the minimum size of the accordion = minimum size of all opened + separators +
     * collapsed
     *
     * @return
     */
    public Dimension calculateMinimumSize() {
        int height = 0;
        int width = 0;

        //horizontal accordion
        if (horizontal) {
            height = calculateGreatestMinimumHeight();
            if (minimumDimension == null) {
                minimumDimension = new Dimension(0, height);
            }
           
            if (height > minimumDimension.height) {
                minimumDimension.height = height;
            }
            //if (height < actual height return otherwise set height)
        } else { //vertical
            width = calculateGreatestMinimumWidth();
            if (minimumDimension == null) {
                minimumDimension = new Dimension(width, 0);
            }
            
            if (width > minimumDimension.width) {
                minimumDimension.width = width;
            }
        } //else

        return minimumDimension;
    } // calculateMinimumSize()

    /**
     * Set background colour to the title component
     *
     * @param colour
     */
    public void setTitleBackground(Color colour) {
        for (AccordionPane pane : panes) {
            pane.getTitledPane().setTitleBackground(colour);
        }
    }

    /**
     * Set background colour to the separators
     *
     * @param colour
     */
    public void setSeparatorBackground(Color colour) {
        for (JLabel separator : separators) {
            separator.setBackground(colour);
        }
    }

    public void setSeparatorBorder(Border border) {
        for (JLabel separator : separators) {
            separator.setBorder(border);
        }
    }
// ====================================================================================================
// ==== Accessors =====================================================================================
// ====================================================================================================

    /**
     * Get default font size for titled panes
     *
     * @return
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * Set font size and update set it to all titles in titled panes
     *
     * @param size
     */
    public void setFontSize(int argFontSize) {
        fontSize = argFontSize;

        // iterate thorugh all
        for (AccordionPane ap : panes) {
            ap.getTitledPane().setTitleFontSize(fontSize);
        }
    }

    /**
     * Get the size of a title
     *
     * @return
     */
    public int getTitleSize() {
        return titleSize;
    }

    /**
     * Set title size
     *
     * @param size
     */
    public void setTitleSize(int argSize) {
        titleSize = argSize;
    }

    /**
     * @return the separatorSize
     */
    public int getSeparatorSize() {
        return separatorSize;
    }

    /**
     * @param separatorSize the separatorSize to set
     */
    public void setSeparatorSize(int argSeparatorSize) {
        separatorSize = argSeparatorSize;
    }

    /**
     * @return the number of panes
     */
    public int getPaneCount() {
        return paneCount;
    }

    /**
     * @return the horizontal
     */
    public boolean isHorizontal() {
        return horizontal;
    }

    /**
     * @param horizontal the horizontal to set
     */
    public void setHorizontal(boolean argHorizontal) {
        this.horizontal = argHorizontal;
    }

    /**
     * Get pane at index
     *
     * @param index
     * @return
     */
    public AccordionPane getPaneAt(int index) {
        //index check
        if (index < 0 || index >= panes.size()) {
            return null;
        }

        return panes.get(index);
    }

    /**
     * Set accordionPane at index
     *
     * @param index
     * @param accordionPane
     */
    public void setPaneAt(int index, AccordionPane accordionPane) {
        panes.set(index, accordionPane);
        //	fireDataChanged(this);
    } // setPaneAt()

    /**
     * Get titledPane
     *
     * @param pane
     * @return
     */
    public AccordionPane getByTitledPane(TitledPane pane) {

        for (AccordionPane p : panes) {
            if (p.getTitledPane().equals(pane)) {
                return p;
            }
        }

        return null;
    } // getByTitledPane

    /**
     * Get index of a separator
     *
     * @param separator
     * @return
     */
    public int getSeparatorIndex(JLabel separator) {
        return separators.indexOf(separator);
    } // getSeparatorIndex

    /**
     * Sets titled pane to be non resizable
     *
     * @param argIndex
     * @param argFixedSize
     * @param argDimension
     */
    public void setFixedSize(int argIndex, Dimension argDimension) {
        panes.get(argIndex).setFixedSize(argDimension);
    }
    // ====================================================================================================
    // ==== Private/Protected/Package Methods =============================================================
    // ====================================================================================================
    // ====================================================================================================
    // ==== Bean variables and methods ====================================================================
    // ====================================================================================================
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
    // ::::: EXPANDED property ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public static final String PROP_EXPANDED = "expandedProperty";

    /**
     * Get the state of the pane at index
     *
     * @param index
     * @return
     */
    public boolean isExpanded(int index) {
        return panes.get(index).isExpanded();
    } // isExpanded()

    /**
     * Set the state of the pane at index
     *
     * @param index
     * @param expanded
     */
    public void setExpanded(int index, boolean expanded) {
        AccordionPane pane = panes.get(index);

        // if we have lock on expanded state don't do anything...
        if (pane.isLocked()) {
            return;
        }

        pane.setExpanded(expanded);
    }
    // ::::: Dimension property ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public static final String PROP_DIMENSION = "sizeProperty";

    /**
     * Get the dimension of the pane at index
     *
     * @param index
     * @return
     */
    public Dimension getPaneDimension(int index) {
        return panes.get(index).getDimension();
    } // getWeight()

    public Dimension[] getPaneDimensions() {
        Dimension[] dims = new Dimension[paneCount];

        for (int i = 0; i < dims.length; i++) {
            dims[i] = panes.get(i).getDimension();
        }

        return dims;
    } //getPaneDimensions

    /**
     * Set the dimension of the pane at index
     *
     * @param index
     * @param expanded
     */
    public void setPaneDimension(int index, Dimension dimension) {
        AccordionPane pane = panes.get(index);

        Dimension oldProperty = pane.getDimension();
        pane.setDimension(dimension);
    }
    // ::::: WEIGHT property ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public static final String PROP_WEIGHT = "weightProperty";

    /**
     * Get the weight of the pane at index
     *
     * @param index
     * @return
     */
    public double getWeight(int index) {
        return panes.get(index).getWeight();
    } // getWeight()

    /**
     * Get weights of all panes
     *
     * @return
     */
    public double[] getPaneWeights() {
        double[] weights = new double[paneCount];

        for (int i = 0; i < weights.length; i++) {
            weights[i] = panes.get(i).getWeight();
        }

        return weights;
    } // getWeights()

    /**
     * Set the weight of the pane at index
     *
     * @param index
     * @param expanded
     */
    public void setPaneWeight(int index, double weight) {
        AccordionPane pane = panes.get(index);
        pane.setWeight(weight);
    }
    // ::::: Locked property ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public static final String PROP_LOCKED = "lockedProperty";

    /**
     * Get the lock state of the pane at index
     *
     * @param index
     * @return
     */
    public boolean isLocked(int index) {
        return panes.get(index).isLocked();
    } // isLocked()

    /**
     * Locks expanded state of a pane at index
     *
     * @param index
     * @param expanded
     */
    public void setPaneLocked(int index, boolean argLock) {
        AccordionPane pane = panes.get(index);

        boolean oldProperty = pane.isLocked();
        pane.setLock(oldProperty);
    }

    // ====================================================================================================
    // ==== Classes =======================================================================================
    // ====================================================================================================
    /**
     * Simple class to hold each TitledPane, dimension(in expanded) and its state
     *
     * @author Matthew
     *
     */
    class AccordionPane {

        private TitledPane titledPane;
        private boolean expanded;
        private Dimension dimension;    // in expanded state!
        private double weight;
        private double defaultWeight;   // default weight which is only set once
        private boolean resizable = true;      // resizable by default 
        private boolean lock; 		    // locks expanded state - setExpanded() will not work if lock=true

        AccordionPane(TitledPane argTitledPane, boolean argExpanded, double argDefaultWeight) {
            this.titledPane = argTitledPane;
            this.expanded = argExpanded;
            this.defaultWeight = argDefaultWeight;
            this.weight = argDefaultWeight;
        } // AccordionPane

        /**
         * @return the titledPane
         */
        public TitledPane getTitledPane() {
            return titledPane;
        }

        /**
         * Request focus
         */
        public void requestFocus() {
            titledPane.requestFocus();
        }

        /**
         * @param titledPane the titledPane to set
         */
        public void setTitledPane(TitledPane argTitledPane) {
            this.titledPane = argTitledPane;
        }

        /**
         * @return the state
         */
        public boolean isExpanded() {
            return expanded;
        }

        /**
         * @param state the state to set
         * @param orientation of the accordion
         */
        public void setExpanded(boolean argExpanded) {
            //System.out.println("setExpand(" + argExpanded + "); Locked: " + lock);
            // if we have lock on expanded just return - we don't want to expand or collapse!
            if (lock) {
                return;
            }

            boolean oldExampleProperty = expanded;

            this.expanded = argExpanded;

            titledPane.setExpanded(expanded);

            propertyChangeSupport.firePropertyChange(PROP_EXPANDED, oldExampleProperty, expanded);
        }

        /**
         * Set default weight to be the current weight
         */
        public void resetToDefaultWeight() {
            setWeight(defaultWeight);
        }
        
        /**
         * @return the dimension
         */
        public Dimension getDimension() {
            return dimension;
        }

        /**
         * @param dimension the dimension to set
         */
        public void setDimension(Dimension argDimension) {
            this.dimension = argDimension;
        }

        /**
         * @param dimension the dimension to set
         */
        public void setDimension(int width, int height) {
            this.dimension = new Dimension(width, height);
        }

        /**
         * @return the weight
         */
        public double getWeight() {
            return weight;
        }

        /**
         * @param weight the weight to set
         */
        public void setWeight(double argWeight) {
            double oldExampleProperty = weight;

            this.weight = argWeight;

            propertyChangeSupport.firePropertyChange(PROP_WEIGHT, oldExampleProperty, weight);
        }

        /**
         * Set default weight
         * @return 
         */
        public double getDefaultWeight() {
            return defaultWeight;
        }

        /**
         * Get default weight
         * @param defaultWeight 
         */
        public void setDefaultWeight(double argDefaultWeight) {
            this.defaultWeight = argDefaultWeight;
        }

        /**
         * @return the resizable
         */
        public boolean isResizable() {
            return resizable;
        }

        /**
         * @param dimension
         * @param fixedSize the fixedSize to set
         */
        public void setFixedSize(Dimension argDimension) {
            this.resizable = false;
            this.dimension = argDimension;
        }

        /**
         * Set resizable
         *
         * @param argResizable
         */
        public void setResizable(boolean argResizable) {
            this.resizable = argResizable;
        }

        /**
         * @return the lock
         */
        public boolean isLocked() {
            return lock;
        }

        /**
         * @param lock the lock to set
         */
        public void setLock(boolean argLock) {
            this.lock = argLock;
            titledPane.getTitle().setEnabled(!lock);

            // if locked we want to show 'disabled' background
            if (lock) {
                titledPane.getTitle().setForeground(UIManager.getColor("Label.disabledText"));
            } else {
                titledPane.getTitle().setForeground(UIManager.getColor("Label.text"));
            }

            titledPane.repaint();
        }
    }
} // AccordionModel class

// $Id$
