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
 *   Dejan Lekic , http://dejan.lekic.org
 */

package com.areen.jlib.gui.accordion;


import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;

import javax.swing.*;

import com.areen.jlib.gui.accordion.AccordionModel.AccordionPane;
import javax.swing.border.Border;

/**
 * Accordion. It is a container which orders TitledPanes (panels with title component at the top/left) in 
 * a row or a column. TitledPane can be in one in two states: collapsed or expanded. All TitledPane are
 * collapsed at startup as default. Each TitledPane is separated by JSeparator which can be moved to resize
 * adjacent expanded panes. Weights should be specified at startup, each panel will take a proportional 
 * share of the screen when expanded. Once a panel is resized then its weight is not taken into layout 
 * calculation. The TitledPanes inside Accordion can have fixed dimension (width or height 
 * depending on orientation); use setFixedSize(index, fixed(boolean=true), requiredDimension). 
 * If we use that function with fixed=false then the accordion will be resized using weight value but 
 * resizing will not be allowed. setResizable(int, true) has the same behaviour as 
 * setFixedSize(index, true, null)
 * 
 * @author Matthew
 */
public class Accordion extends JComponent implements PropertyChangeListener {
	
    /* 
     * Accordion ignores insets, maximum and preferred sizes. Only minimum sizes are supported in 
     * one dimension
     *   
     *   Horizontal accordion looks like this:
          ___________________________________________________________________
         |ACCORDION                                                          |
         |                EXPANDED TITLED PANE                               |
         | O----O  /-\  O--------------------------------------------------O |
         | | C  |  |J|  |+------------------------------------------------+| |
         | | O  |  |S|  ||                                                || |
         | | L  |  |E|  ||      TITLE                                     || |
         | | L  |  |P|  ||                                                || |
         | | A  |  |A|  ||                                                || |
         | | P  |  |R|  |+------------------------------------------------+| |
         | | S  |  |A|  |+------------------------------------------------+| |
         | | E  |  |T|  ||                                                || |
         | | D  |  |O|  ||      MAIN COMPONENT                            || |
         | |    |  |R|  ||                                                || |
         | | T  |  | |  ||                                                || |
         | | I  |  | |  ||                                                || |
         | | T  |  | |  ||                                                || |
         | | L  |  | |  ||                                                || |
         | | E  |  | |  ||                                                || |
         | | D  |  | |  ||                                                || |
         | |    |  | |  ||                                                || |
         | | P  |  | |  ||                                                || |
         | | A  |  | |  ||                                                || |
         | | N  |  | |  ||                                                || |
         | | E  |  | |  ||                                                || |
         | |    |  | |  ||                                                || |
         | |    |  | |  ||                                                || |
         | |    |  | |  |+------------------------------------------------+| |
         | O----O  \-/  O--------------------------------------------------O |
         |___________________________________________________________________|

     */
    
    // ====================================================================================================
    // ==== Variables =====================================================================================
    // ====================================================================================================

    // :::::: CONFIGURATION VARIABLES :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    protected Color titleBackgroundColor; // background colour for titles, if set
    protected Color separatorBackgroundColor; // background colour for separators
    protected Border separatorBorder; // separator's border
    
    // :::::: PRIVATE/PROTECTED :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private String uiClassID = "AccordionUI"; //UI Class
    private AccordionModel model;
    private AccordionPane firstPane; // field to hold reference to left/top pane to resize when 
    // moving a separator
    private AccordionPane secondPane; // right/bottom pane to resize
    private MouseListener titleMouseListener; //mouse listener for title component in TitledPane
    private SeparatorMouseListener separatorMouseListener;
    private int clicksRequired = 1; /// Number of clicks required to expand/collapse.

    // ====================================================================================================
    // ==== Constructors ==================================================================================
    // ====================================================================================================
    
    public Accordion(boolean horizontal) {
    	model = new AccordionModel(horizontal);
    	
    	updateUI();
    	
    	titleMouseListener = new TitleMouseListener();
    	separatorMouseListener = new SeparatorMouseListener();
    	
    	// register to listen for property changes
    	model.addPropertyChangeListener(AccordionModel.PROP_EXPANDED, this);
    	model.addPropertyChangeListener(AccordionModel.PROP_DIMENSION, this);  
    } // Accordion constructor
    
    /**
     * Creates a vertical Accordion.
     */
    public Accordion() {
        this(false);
    } // Accordion constructor (default)
    
    public Accordion(boolean horizontal, TitledPane... panes) {
    	model = new AccordionModel(horizontal);
    	
    	// register to listen for property changes
    	model.addPropertyChangeListener(AccordionModel.PROP_EXPANDED, this);
    	model.addPropertyChangeListener(AccordionModel.PROP_DIMENSION, this);  
    	
    	updateUI();
    	
    	titleMouseListener = new TitleMouseListener();
    	separatorMouseListener = new SeparatorMouseListener();
    	
    	//if we have horizontal orientation then make separator vertical
    	int orientation = !horizontal ? JSeparator.HORIZONTAL : JSeparator.VERTICAL;
    	
    	//add components
    	for (int i = 0; i < panes.length; i++) {
    		//get title component. If the mode is horizontal, it will be in the west pane. Top otherwise.
    		panes[i].getTitle().addMouseListener(titleMouseListener);
    		
    		model.addPane(panes[i]);
    		super.add(panes[i]);
    	
    		//skip adding separator 
    		if (i != panes.length - 1) {
    			JLabel separator = new JLabel();
                        separator.setOpaque(true);
    			separator.addMouseListener(separatorMouseListener);
    			separator.addMouseMotionListener(separatorMouseListener);
    			model.addSeparator(separator);
    			super.add(separator);   		
    		}
     	} // for
    } // Accordion constructor
    
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

    @Override
    public void setBounds(Rectangle r) {
    	super.setBounds(r);
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
    	super.setBounds(x, y, width, height);
    }
    
    @Override
    public void setPreferredSize(Dimension dim) {
    	super.setPreferredSize(dim);
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
     * Set background colour of titled panes
     *
     * @param colour
     */
    public void setTitleBackground(Color colour) {
        model.setTitleBackground(colour);
        titleBackgroundColor = colour;
    }

    /**
     * Set separator background
     *
     * @param colour
     */
    public void setSeparatorBackground(Color colour) {
        model.setSeparatorBackground(colour);
        separatorBackgroundColor = colour;
    }

    /**
     * Set separator's border
     *
     *
     * @param border
     */
    public void setSeparatorBorder(Border border) {
        model.setSeparatorBorder(border);
        separatorBorder = border;
    }

	/**
	 * Resize components 
	 * @param dragSource
	 * @param dx
	 * @param dy
	 */
	public void resizeBySeparator(JLabel dragSource, int dx, int dy) {
		//get index of dragSource
		int separatorIndex = model.getSeparatorIndex(dragSource);

		if (canResize(separatorIndex)) {
			Dimension firstDimension;
			
			//check if the dimensions are set on the pane, otherwise we have to copy
			//them from titledPane.bounds and change
			
			//first pane
			if (firstPane.getDimension() == null) {
				firstDimension = firstPane.getTitledPane().getSize();
			} else {
				firstDimension = firstPane.getDimension();
			}

			int h1;
			int w1;
			
			// compute new sizes
			if (model.isHorizontal()) { //Panes in a row (horizontal case)
				h1 = firstDimension.height;
				w1 = firstDimension.width + dx;
			} else { // vertical case
				// in vertical state do not change X coordinate
				h1 = firstDimension.height + dy;
				w1 = firstDimension.width;
			} //else
			
			//check minimum dimensions - check if the new dimension won't be smaller than minimum size
			if (!checkMinimumDimension(firstPane.getTitledPane().getMinimumSize(), w1, h1)) {
				return;
			}
			
			// in horizontal state do not change Y coordinate
			model.setPaneDimension(model.indexOf(firstPane), new Dimension(w1, h1));

                        // recalculate accordion preffered size
                        setPreferredSize(new Dimension(model.calculateTotalMinimumWidth(), 
                                model.calculateTotalMinimumHeight()));
                        
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
		if ((model.isHorizontal() && dimension.width < w1) 
				|| (!model.isHorizontal() && dimension.height < h1)) {
			return true;
		} 

		return false;
	} // checkMinimumDimension

	/**
	 * DO NOT USE
	 */
	@Override
	@Deprecated
	public Component add(Component arg0, int arg1) {
		return arg0;
	}

	/**
	 * DO NOT USE
	 */
	@Override
	@Deprecated
	public void add(Component arg0, Object arg1, int arg2) { }

	/**
	 * DO NOT USE
	 */
	@Override
	@Deprecated
	public Component add(String arg0, Component arg1) {
		return arg1;
	}


	/**
	 * DO NOT USE!!!
	 * @param c
	 * @return
	 */
	@Override
	@Deprecated
	public Component add(Component c) {
		return c;
	}
	
	@Override
	@Deprecated
	public void add(Component c, Object bla) { }
	
	/**
	 * Adds an expandable pane to the Accordion
	 * @param titledPane
	 */
	   public void add(TitledPane titledPane) {
        //if we have horizontal orientation then make separator vertical
  //      int orientation = !model.isHorizontal() ? JSeparator.HORIZONTAL : JSeparator.VERTICAL;

        //add separator only if it's not the first one in the accordion
        if (model.getPaneCount() > 0) {
            JLabel separator = new JLabel();
            separator.setOpaque(true);
            separator.setBackground(separatorBackgroundColor);
            separator.setBorder(separatorBorder);
            separator.addMouseListener(separatorMouseListener);
            separator.addMouseMotionListener(separatorMouseListener);
            model.addSeparator(separator);
            super.add(separator);
        }
        
        Component component = titledPane.getTitle();
        component.addMouseListener(titleMouseListener);
        component.setBackground(titleBackgroundColor);

        //add component
        model.addPane(titledPane);
        super.add(titledPane);

        //revalidate
        revalidate();
    } // add 
    
	/**
	 * Create new titled pane with title and component inside
	 * @param component
	 * @param title
	 */
	public void add(JComponent component, String title) {
    	//add component
		TitledPane pane = new TitledPane(title, component, model.isHorizontal());
		
		//add pane
		add(pane);
	}
	
	/**
     * Adds new expandable pane with specified title, RotatablePane and JPanel by default
     * @param title
     */
    public void add(String title) {
    	add(new TitledPane(title, model.isHorizontal()));
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
    	validate();
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
     * Set size of a pane at index
     * @param index
     * @param d
     */
    public void setPaneSize(int index, Dimension d) {
    	model.setPaneDimension(index, d);
    }
    
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

    /**
     * Get index of titled pane
     * @param pane
     * @return
     */
    public int indexOf(TitledPane pane) {
    	return model.indexOf(pane);
    }

    /**
     * Get index of an accordion pane
     * @param pane
     * @return
     */
    public int indexOf(AccordionPane pane) {
    	return model.indexOf(pane);
    }
    
    /**
     * Sets title at index
     * @param index
     * @param title
     */
    public void setTitleAt(int index, String title) {
    	((RotatableTitle) model.getPaneAt(index).getTitledPane().getTitle()).setText(title);
    }
    
    /**
     * Get title at index
     * @param index
     */
    public void getTitleAt(int index) {
    	((RotatableTitle) model.getPaneAt(index).getTitledPane().getTitle()).getText();
    }
    
    // ====================================================================================================
    // ==== Accessors =====================================================================================
    // ====================================================================================================
    
    public int getClicksRequired() {
        return clicksRequired;
    }

    public void setClicksRequired(int argClicksRequired) {
        clicksRequired = argClicksRequired;
    }
    
    /**
     * Get model
     * @return
     */
	public AccordionModel getModel() {
		return model;
	} // getModel()
	
	/**
	 * Lock expanded state of a pane at index
	 * @param argIndex
	 * @param argLocked
	 */
	public void setPaneLocked(int argIndex, boolean argLocked) {
		model.getPaneAt(argIndex).setLock(argLocked);
	}
	
	/**
	 * Lock expanded state of a pane 
	 * @param argIndex
	 * @param argLocked
	 */
	public void setPaneLocked(TitledPane argPane, boolean argLocked) {
		model.getByTitledPane(argPane).setLock(argLocked);
	}
	
    /**
     * Set fixed size of a TitledPane
     * @param argIndex
     * @param argFixedSize
     */
    public void setFixedSize(int argIndex, Dimension argDimension) {
    	model.getPaneAt(argIndex).setFixedSize(argDimension);
    }
    
    /**
     * Set fixed size property of a TitledPane
     * @param dimension 
     * @param argIndex
     * @param argFixedSize
     */
    public void setFixedSize(TitledPane pane, Dimension argDimension) {
    	model.getByTitledPane(pane).setFixedSize(argDimension);
    }
    
    /**
     * Set titled pane not to be resizable
     * @param argIndex
     * @param argResizable
     */
    public void setResizable(int argIndex, boolean argResizable) {
    	model.getPaneAt(argIndex).setResizable(argResizable);
    }
	
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
    boolean canResize(int separatorIndex) {
    //	System.out.println(separatorIndex);
    	// find a pane to the top/left of the separator
    	firstPane = getPreviousExpandedPane(separatorIndex);
    	
    	// find the other pane to resize
    	//secondPane = getNextExpandedPane(separatorIndex);

    	// if two expanded panes are found then we can resize
    	if (firstPane != null) {
    		return true;
        }
        
    	System.out.println("Can't resize!");
    	
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
    	
    		//if found expanded and has no fixed size sized return it!
    		if (current.isExpanded() && current.isResizable()) {
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
    	
    		//if found expanded and has no fixed size return it!
    		if (current.isExpanded() && current.isResizable()) {
    			return current;
                }
    	} //for
    	
    	return null;
    }
    
    // ====================================================================================================
    // ==== Classes =======================================================================================
    // ====================================================================================================

    /**
     * Simple implementation of the MouseListener interface needed for listening to the clicks on the
     * Accordion titles.
     */
	class TitleMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			//double left click

			if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == getClicksRequired()) {
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
	} // TitleMouseListener Class

	/**
	 * Mouse Listener and Mouse Motion listener for Separator
	 * @author Matthew
	 *
	 */
	class SeparatorMouseListener implements MouseListener,
											MouseMotionListener {
		// separator drag related variables
		private boolean dragHappening;
		private Point clickPoint;
		private Point lastDragPoint;
		private JLabel dragSource;
		private Component component;
		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseDragged(MouseEvent e) { 
			// while drag is active update the last point
			if (dragHappening) {
				//		System.out.println("MouseDragged");	
				lastDragPoint = e.getPoint();
			}
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseMoved(MouseEvent e) { }

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) { }

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
			// get component at point
			component = e.getComponent();
		
			//if separator and can resize set change mouse cursor
			if (component instanceof JLabel) {
				// set moving cursor
				if (model.isHorizontal()) {
					component.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
				} else {
					component.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
				}
			}
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseExited(MouseEvent e) {
			// if dragging do not change the cursor even though we have exited the JSeparator
			if (!dragHappening) {
				// get component at point
				component = e.getComponent();

				//if separator change cursor back to normal
				if (component instanceof JLabel) {
					component.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			Point point = e.getPoint();
			
			// get component at point
			component = e.getComponent().getComponentAt(point);
			
			// we are concerned about JLabel
			if (component instanceof JLabel) {
				// store initial point
				clickPoint = point;
			//	System.out.println("StartedDragging");
				// mark it as drag started
				dragHappening = true;
				 
				// set source
				dragSource = (JLabel) component;
			}
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			//stop dragging
			dragHappening = false;
			
			//if no drag happened cleanup and return;
			if (lastDragPoint == null) {
				dragDone();

				return;
			} //if

			//calculate change due to drag
			int dx = lastDragPoint.x - clickPoint.x; // +ve is right, -ve left
			int dy = lastDragPoint.y - clickPoint.y; // +ve is up, -ve down

			//resize components
			resizeBySeparator(dragSource, dx, dy);
							
			// clean up the drag 
			dragDone();
		} // SeparatorMouseListener
		
		protected void dragDone() {
			//important - after dragging 
			lastDragPoint = null;
			dragSource = null;
		//	System.out.println("DragDone");
		} // dragDone()
	} // SeparatorMouseListener class
    
} // Accordion class

// $Id$
