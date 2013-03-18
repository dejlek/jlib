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


import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;

import javax.swing.*;

import com.areen.jlib.gui.accordion.AccordionModel.AccordionPane;

/**
 * Accordion. It is a container which orders TitledPanes (panels with title component at the top/left) in 
 * a row or a column. TitledPane can be in one in two states: collapsed or expanded. All TitledPane are
 * collapsed at startup as default. Each TitledPane is separated by JSeparator which can be moved to resize
 * adjacent expanded panes. Weights should be specified at startup, each panel will take a proportional 
 * share of the screen when expanded. Once a panel is resized then its weight is not taken into layout 
 * calculation.
 * 
 * @author Matthew
 */
public class Accordion extends JComponent implements PropertyChangeListener {
	
    /* 
     * Accordion ignores insets, maximum and preferred sizes. Only minimum sizes are supported in 
     * one dimension
     */
    
    // ====================================================================================================
    // ==== Variables =====================================================================================
    // ====================================================================================================
    
    // :::::: PRIVATE/PROTECTED :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	private String uiClassID = "AccordionUI"; //UI Class
	private AccordionModel model;

	private AccordionPane firstPane; // field to hold reference to left/top pane to resize when 
                                         // moving a separator
	private AccordionPane secondPane; // right/bottom pane to resize
		
    // ====================================================================================================
    // ==== Constructors ==================================================================================
    // ====================================================================================================
    
    public Accordion(boolean horizontal) {
    	model = new AccordionModel(horizontal);
    	
    	//register to listen for property changes
    	model.addPropertyChangeListener(AccordionModel.PROP_EXPANDED, this);
    	model.addPropertyChangeListener(AccordionModel.PROP_DIMENSION, this);
    	
    } // Accordion constructor (default)

    public Accordion(boolean horizontal, TitledPane... panes) {
    	this(horizontal);
    	MouseListener titleMouseListener = new TitleMouseListener();
    	
    	//if we have horizontal orientation then make separator vertical
    	int orientation = !horizontal ? JSeparator.HORIZONTAL : JSeparator.VERTICAL;
    	
    	//add components
    	for (int i = 0; i < panes.length; i++) {
    		//get title component. If the mode is horizontal, it will be in the west pane. Top otherwise.
    		panes[i].getTitle().addMouseListener(titleMouseListener);
    		
    		model.addPane(panes[i]);
    		add(panes[i]);
    	
    		//skip adding separator 
    		if (i != panes.length - 1) {
    			JSeparator separator = new JSeparator(orientation);
    			model.addSeparator(separator);
    			add(separator);   		
    		}
     	}
    	
    	updateUI();
    }
    
    // ====================================================================================================
    // ==== Interface/Superclass Methods ==================================================================
    // ====================================================================================================
 
    @Override
    public String getUIClassID() {
        return uiClassID;
    }
    
    @Override
    public void updateUI() {
        if (UIManager.get(getUIClassID()) != null) {
            setUI((AccordionUI) UIManager.getUI(this));
        } else {
            setUI(new AccordionUI());
        }
    }
 
    public void setUI(AccordionUI ui) {
        super.setUI(ui);
    }
    
    public AccordionUI getUI() {
        return (AccordionUI) ui;
    }

    // :::: Interface/Superclass 1 ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		revalidate();
	}
    
    // :::: Interface/Superclass 2 ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    // ====================================================================================================
    // ==== Public Methods ================================================================================
    // ====================================================================================================
    
	/**
	 * Resize components 
	 * @param dragSource
	 * @param dx
	 * @param dy
	 */
	public void resizeBySeparator(JSeparator dragSource, int dx, int dy) {
		//get index of dragSource
		int separatorIndex = model.getSeparatorIndex(dragSource);

		// resize if it's allowed - 2 panes opened
		if (canResize(separatorIndex)) {
			Dimension firstDimension;
			Dimension secondDimension;
			
			//check if the dimensions are set on the pane, otherwise we have to copy
			//them from titledPane.bounds and change
			
			//first pane
			if (firstPane.getDimension() == null) {
				firstDimension = firstPane.getTitledPane().getSize();
			} else {
				firstDimension = firstPane.getDimension();
			}
			
			//second pane
			if (secondPane.getDimension() == null) {
				secondDimension = secondPane.getTitledPane().getSize();
			} else {
				secondDimension = secondPane.getDimension();
			} //if

			int h1;
			int w1;
			int h2;
			int w2;
			
			// compute new sizes
			if (model.isHorizontal()) { //Panes in a row (horizontal case)
				h1 = firstDimension.height;
				w1 = firstDimension.width + dx;
				
				h2 = secondDimension.height;
				w2 = secondDimension.width - dx;
			} else { // vertical case
				// in vertical state do not change X coordinate
				h1 = firstDimension.height + dy;
				w1 = firstDimension.width;
				
				h2 = secondDimension.height - dy;
				w2 = secondDimension.width;
			} //else
			
			//check minimum dimensions - check if the new dimension won't be smaller than minimum size
			if (!checkMinimumDimension(firstPane.getTitledPane().getMinimumSize(), w1, h1) 
					|| !checkMinimumDimension(firstPane.getTitledPane().getMinimumSize(), w2, h2)) {
				return;
			}
			
			// in horizontal state do not change Y coordinate
			model.setPaneDimension(model.indexOf(firstPane), new Dimension(w1, h1));
			model.setPaneDimension(model.indexOf(secondPane), new Dimension(w2, h2));
		} //if
	}	
	
	/**
	 * Checks if the new size is greater than minimum dimension
	 * @param firstDimension
	 * @param w1
	 * @param h1
	 * @return true if minimum dimension is smaller than Dimension(w1, h1)
	 */
	private boolean checkMinimumDimension(Dimension dimension, int w1, int h1) {
		if (dimension.width < w1 && dimension.height < h1) {
			return true;
                } 
		
                return false;
	} // checkMinimumDimension

	/**
     * Adds an expandable pane to the Accordion
     * @param titledPane
     */
    public void addPane(TitledPane titledPane) {
    	model.addPane(titledPane);
    } // add 
    
	/**
     * Adds new expandable pane with specified title, JLabel and JPanel by default
     * @param title
     */
    public void addPane(String title) {
    	model.addPane(title);
    } //add
    
    /**
     * Expends all panes
     */
    public void expandAll() {
    	model.expandAll();
    } // expandAll()
    
    /**
     * Collapses all panes
     */
    public void collapseAll() {
    	model.collapseAll();
    } // collapseAll
    
    /**
     * Expands panes with indexes ...
     * @param index
     */
    public void expand(int... index) {
    	for (int i : index) {
    		model.getPaneAt(i).setExpanded(true);
    	} //for
    } // expand()
    
    /**
     * Collapses panes with indexes ...
     * @param index
     */
    public void collapse(int... index) {
    	for (int i : index) {
    		model.getPaneAt(i).setExpanded(false);
    	} //for
    } // collapse()
    
    /**
     * Set TitlePanes dimensions
     * @param dimensions
     */
    public void setSizes(Dimension[] dimensions) { 
    	for (int i = 0; i < dimensions.length; i++) {
    		model.setPaneDimension(i, dimensions[i]);
    	}
    } // setSizes()
    
    /**
     * Get pane dimensions
     * @return
     */
    public Dimension[] getDimensions() { 
    	return model.getPaneDimensions();
    } // getDimensions()
    
    /**
     * Retrieve pane weights
     * @return
     */
    public double[] getWeights() {
    	return model.getWeights();
    } // getWeights()
    
    public LinkedList<Integer> getExpandedPanes() {
    	return model.getExpanded();
    } //getExpandedPanes
    
    // ====================================================================================================
    // ==== Accessors =====================================================================================
    // ====================================================================================================

    /**
     * Get model
     * @return
     */
	public AccordionModel getModel() {
		return model;
	} // getModel()
	
    /**
     * Sets pane selected at index idx
     * @param idx
     */
    public void setSelected(int idx) {
    	model.getPaneAt(idx).requestFocus();
    } // setSelected ()

    // ====================================================================================================
    // ==== Private/Protected/Package Methods =============================================================
    // ====================================================================================================

    /**
     * Function to determine if resizing action can take place.
     * Currently, the algorithm checks if there are two expanded neighbouring panes 
     * (Separators are disregarded for neighbour calculation).
     * @param separatorIndex
     * @return
     */
    private boolean canResize(int separatorIndex) {
    	// find a pane to the top/left of the separator
    	firstPane = getPreviousExpandedPane(separatorIndex);
    	
    	// find the other pane to resize
    	secondPane = getNextExpandedPane(separatorIndex);

    	// if two expanded panes are found then we can resize, and if the pane won't be 
        // smaller than 2xtitle size
    	if (firstPane != null && secondPane != null) {
    		return true;
        }
        
    	return false;
	} // can resize

    /**
     * Find an expanded pane to the top/left of the separator.
     * @param separatorIndex
     * @return null if not found
     */
    private AccordionPane getPreviousExpandedPane(int separatorIndex) {
    	AccordionPane current;
    	
    	//look for an expanded pane before the separator (same index of smaller)
    	for (int i = separatorIndex; i >= 0; i--) {
    		current = model.getPaneAt(i); 
    	
    		//if found expanded return it!
    		if (current.isExpanded()) {
    			return current;
                }
    	} //for
    	
    	return null;
    }
    
    /**
     * Find an expanded pane to the right/bottom of the separator.
     * @param separatorIndex
     * @return null if not found
     */
    private AccordionPane getNextExpandedPane(int separatorIndex) {
    	AccordionPane current;
    	
    	//look for an expanded pane before the separator (same index of smaller)
    	for (int i = separatorIndex + 1; i < model.getPaneCount(); i++) {
    		current = model.getPaneAt(i); 
    	
    		//if found expanded return it!
    		if (current.isExpanded()) {
    			return current;
                }
    	} //for
    	
    	return null;
    }
    
    // ====================================================================================================
    // ==== Classes =======================================================================================
    // ====================================================================================================

	class TitleMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			//double left click
			if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
				Component c = (Component) e.getSource();
				TitledPane titledPane = (TitledPane) c.getParent();

				//flip the state
				model.triggerStateChange(titledPane);			
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) { }

		@Override
		public void mouseExited(MouseEvent e) { }

		@Override
		public void mousePressed(MouseEvent e) { }

		@Override
		public void mouseReleased(MouseEvent e) { }
	}
} // Accordion class

// $Id$