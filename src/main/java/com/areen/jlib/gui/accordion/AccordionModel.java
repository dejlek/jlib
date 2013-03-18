/**
 * $Id$
 *
 * Copyright (c) 2009-2013 Areen Design Services Ltd
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
 *   Mateusz Dykiert , http://dykiert.org
 * Contributor(s):
 *   -
 */

package com.areen.jlib.gui.accordion;

import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.*;

import com.areen.jlib.gui.accordion.AccordionModel.AccordionPane;

/**
 * Data model for Accordion. TitledPane and its eights, set size and state are stored in AccordionPane object.
 * Title size and separator size are set here.
 * 
 * @author Matthew
 */
public class AccordionModel {
    
    // ====================================================================================================
    // ==== Variables =====================================================================================
    // ====================================================================================================
    
    // :::::: CONFIGURATION VARIABLES :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	int titleSize = 25; //size of the title (width if horizontal, height if vertical)
	int separatorSize = 5;
	
    // :::::: PUBLIC ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    // :::::: PRIVATE/PROTECTED :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	private ArrayList<AccordionPane> panes;
	private ArrayList<JSeparator> separators;

    private boolean horizontal;
	private int paneCount;    
	
    // ====================================================================================================
    // ==== Constructors ==================================================================================
    // ====================================================================================================
    
    public AccordionModel(boolean argHorizontal) {
        panes = new ArrayList<AccordionPane>();
        separators = new ArrayList<JSeparator>();
        this.horizontal = argHorizontal;
        propertyChangeSupport = new PropertyChangeSupport(this);
    } // Template constructor (default)
    
    // ====================================================================================================
    // ==== Interface/Superclass Methods ==================================================================
    // ====================================================================================================

    // :::: Interface/Superclass 1 ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    // :::: Interface/Superclass 2 ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    // ====================================================================================================
    // ==== Public Methods ================================================================================
    // ====================================================================================================
    
    /**
     * Adds the pane to the model (creates the data structure behind the scenes)
     * @param titledPane
     */
	public void addPane(TitledPane titledPane) {
		AccordionPane accordionPane = new AccordionPane(titledPane, false); // collapsed by default
		// by default we don't set the dimensions - if they are set then layout manager will 
		// ignore the weights
		accordionPane.setWeight(0.2); // default weight
		
		//add it to the list
		panes.add(accordionPane);
		
		//increase the size of the model
		paneCount++;
	}
	
	/**
	 * Adds a titled pane with specified title
	 * @param title
	 */
	public void addPane(String title) {
		TitledPane pane = new TitledPane(title, horizontal);
		AccordionPane accordionPane = new AccordionPane(pane, false); // collapsed by default
		// by default we don't set the dimensions - if they are set then layout manager will 
		// ignore the weights
		accordionPane.setWeight(0.2); // default weight
		
		//add it to the list
		panes.add(accordionPane);
		
		//increase the size of the model
		paneCount++;
	}

	/**
	 * Collapses all panes
	 */
	public void expandAll() {
		for (AccordionPane pane : panes) {
			pane.setExpanded(true);
		}
	}
	
	/**
	 * Collapses all panes
	 */
	public void collapseAll() {
		for (AccordionPane pane : panes) {
			pane.setExpanded(false);
		}
	}
	
	/**
	 * Trigger state change of specified object
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
	 * Get the weight share of the pane at index
	 * @param index
	 * @return
	 */
	public double getWeightShare(int index) {
		return panes.get(index).getWeight() / getTotalWeights();
	} // getWeightShare()
	
	/**
	 * Calculates occupied total horizontal space, i.e. it does not count 
         * panes which have no width specified (the ones that are used). 
         * Collapsed panes have specified globally specified width.
	 * @return
	 */
	public int getOccupiedHorizontalSpace() {
		int space = 0;
		
		// calculate the space taken by 
		for (AccordionPane pane : panes) {
			if (pane.isExpanded()) {
				if (pane.getDimension() != null) { 
                               // if no dimension is specified, 
                               //they will be calculated using this method later
					space += pane.getDimension().getWidth();
				}
			} else {
				space += titleSize;
			}
		} // for
		
		//add space taken up by dividers
		space += separators.size() * separatorSize; 
		
		return space;
	} // getOccupiedHorizontalSpace()
	
	/**
	 *Calculates occupied total vertical space, i.e. it does not count panes which 
	 * have no height specified (the ones that are used). Collapsed panes have specified 
         * globally specified height. 
	 * @return
	 */
	public int getOccupiedVerticalSpace() {
		int space = 0;
		
		// calculate the space taken by 
		for (AccordionPane pane : panes) {
			if (pane.isExpanded()) {
				if (pane.getDimension() != null) { // if no dimension is specified,
                                            // they will be calculated using this method later
					space += pane.getDimension().getHeight();
				} 
			} else {
				space += titleSize;
			}
		} // for
		
		//add space taken up by dividers
		space += panes.size() * separatorSize; 
		
		return space;
	} // getOccupiedVerticalSpace()

	/**
	 * Add separator
	 * @param separator
	 */
	public void addSeparator(JSeparator separator) {
		separators.add(separator);
	}
	
	/**
	 * Get index of an AccordionPane
	 */
	public int indexOf (AccordionPane pane) {
		return panes.indexOf(pane);
	}
	
	/**
	 * Get indexes of expanded panes
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
	
// ====================================================================================================
// ==== Accessors =====================================================================================
// ====================================================================================================

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
     * @param index
     * @return
     */
    AccordionPane getPaneAt(int index) {
    	//index check
    	if (index < 0 || index >= panes.size()) {
    		return null;
    	}
    		
    	return panes.get(index);
    }
    
    /**
     * Set accordionPane at index
     * @param index
     * @param accordionPane
     */
    public void setPaneAt(int index, AccordionPane accordionPane) {
    	panes.set(index, accordionPane);
    //	fireDataChanged(this);
    } // setPaneAt()
    
    /**
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
     * @param separator
     * @return
     */
    public int getSeparatorIndex(JSeparator separator) {
    	return separators.indexOf(separator);
    } // getSeparatorIndex
    
    /**
	 * @return the titleSize
	 */
	public int getTitleSize() {
		return titleSize;
	}

	/**
	 * @param titleSize the titleSize to set
	 */
	public void setTitleSize(int argTitleSize) {
		this.titleSize = argTitleSize;
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
		this.separatorSize = argSeparatorSize;
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
     * @param index
     * @return
     */
    public boolean isExpanded(int index) {
    	return panes.get(index).isExpanded();
    } // isExpanded()
    
    /**
     * Set the state of the pane at index
     * @param index
     * @param expanded
     */
    public void setExpanded(int index, boolean expanded) {
    	AccordionPane pane = panes.get(index);

    	boolean oldExampleProperty = pane.isExpanded();
    	pane.setExpanded(expanded);
    	
    	propertyChangeSupport.firePropertyChange(PROP_EXPANDED, oldExampleProperty, expanded);
    }

    // ::::: Dimension property ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
  
    public static final String PROP_DIMENSION = "sizeProperty";

    /**
     * Get the dimension of the pane at index
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
     * @param index
     * @param expanded
     */
    public void setPaneDimension(int index, Dimension dimension) {
    	AccordionPane pane = panes.get(index);

    	Dimension oldProperty = pane.getDimension();
    	pane.setDimension(dimension);
    	
    	//fire property change
    	propertyChangeSupport.firePropertyChange(PROP_DIMENSION, oldProperty, dimension);
    }
    
    // ::::: WEIGHT property ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public static final String PROP_WEIGHT = "weightProperty";
    
    /**
     * Get the weight of the pane at index
     * @param index
     * @return
     */
    public double getWeight(int index) {
    	return panes.get(index).getWeight();
    } // getWeight()

    /**
     * Get weights of all panes
     * @return
     */
    public double[] getWeights() {
    	double[] weights = new double[paneCount];
    	
    	for (int i = 0; i < weights.length; i++) {
    		weights[i] = panes.get(i).getWeight();
    	}
    	
    	return weights;
    } // getWeights()
    
    /**
     * Set the weight of the pane at index
     * @param index
     * @param expanded
     */
    public void setWeight(int index, double weight) {
    	AccordionPane pane = panes.get(index);

    	double oldExampleProperty = pane.getWeight();
    	pane.setWeight(weight);
    	
    	//fire property change
    	propertyChangeSupport.firePropertyChange(PROP_WEIGHT, oldExampleProperty, weight);
    }
    
    // ====================================================================================================
    // ==== Classes =======================================================================================
    // ====================================================================================================

	/**
     * Simple class to hold each TitledPane, dimension(in expanded) and its state
     * @author Matthew
     *
     */
    class AccordionPane {
    	private TitledPane titledPane;
    	private boolean expanded;
    	private Dimension dimension; // in expanded state!
    	private double weight;

		AccordionPane(TitledPane argTitledPane, boolean argExpanded) {
    		this.titledPane = argTitledPane;
    		this.expanded = argExpanded;
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
			boolean oldExampleProperty = expanded;
	       
			this.expanded = argExpanded;
			
                        // if we have horizontal we need to flip the title pane to west panel 
                        // and rotate it 90* anticlockwise
			titledPane.setExpanded(expanded);
					

			propertyChangeSupport.firePropertyChange(PROP_EXPANDED, oldExampleProperty, expanded);
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
		//	fireDataChanged(this);
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
			this.weight = argWeight;
		}
    }
} // AccordionModel class

// $Id$
