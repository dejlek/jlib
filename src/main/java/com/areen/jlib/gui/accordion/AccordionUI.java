/**
 * $Id$
 *
 * Copyright (c) 2009-2012 Areen Design Services Ltd
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
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

/**
 * TODO: Short class description.
 * 
 * TODO: Longer class description.
 * 
 * @author Matthew
 */
public class AccordionUI extends ComponentUI {
	/* ideas from an article by Kirill Grouchnikov - JFlexiSlider 
	 * 
	 */

	// ====================================================================================================
	// ==== Variables =====================================================================================
	// ====================================================================================================

	// :::::: CONFIGURATION VARIABLES :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	// :::::: PUBLIC ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	// :::::: PRIVATE/PROTECTED :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	private Accordion accordion;
	private MouseListener mouseListener;
	private MouseMotionListener mouseMotionListener;
	
	// separator drag related variables
	private boolean dragHappening;
	private Point clickPoint;
	private Point lastDragPoint;
	private JSeparator dragSource;
	
	// ====================================================================================================
	// ==== Constructors ==================================================================================
	// ====================================================================================================

	// ====================================================================================================
	// ==== Interface/Superclass Methods ==================================================================
	// ====================================================================================================

	public static AccordionUI createUI(JComponent c) {
		return new AccordionUI();
	}

	@Override
	public void installUI(final JComponent c) {
		accordion = (Accordion) c;
		c.setLayout(new AccordionLayout(accordion.getModel()));

		accordion.addMouseListener(mouseListener);

		mouseListener = new MouseListener() {
			Component component;
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub	
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// get component at point
				component = c.getComponentAt(e.getPoint());
			
				//if separator and can resize set change mouse cursor
				if (component instanceof JSeparator) {
					// set moving cursor
					if (accordion.getModel().isHorizontal()) {
						component.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
					} else {
						component.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
					}
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// if dragging do not change the cursor even though we have exited the JSeparator
				if (!dragHappening) {
					// get component at point
					component = c.getComponentAt(e.getPoint());

					//if separator change cursor back to normal
					if (component instanceof JSeparator) {
						component.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				Point point = e.getPoint();
				
				// get component at point
				component = e.getComponent().getComponentAt(point);
				
				// we are concerned about JSeparator
				if (component instanceof JSeparator) {
					// store initial point
					clickPoint = point;
					
					// mark it as drag started
					dragHappening = true;
					 
					// set source
					dragSource = (JSeparator) component;
				}
			}

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
				accordion.resizeBySeparator(dragSource, dx, dy);
								
				// clean up the drag 
				dragDone();
			}
		};
		
		accordion.addMouseListener(mouseListener);
		
		mouseMotionListener = new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				// while drag is active update the last point
				if (dragHappening) {
					lastDragPoint = e.getPoint();
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {

			}
		};

		accordion.addMouseMotionListener(mouseMotionListener);

		}

	@Override
	public void uninstallUI(JComponent c) {
		//clean up, remove all components etc...
		c.removeAll();
		c.setLayout(null);
		mouseListener = null;
		mouseMotionListener = null;
	}

	// :::: Interface/Superclass 1 ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	// :::: Interface/Superclass 2 ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	// ====================================================================================================
	// ==== Public Methods ================================================================================
	// ====================================================================================================

	// ====================================================================================================
	// ==== Accessors =====================================================================================
	// ====================================================================================================

	// ====================================================================================================
	// ==== Private/Protected/Package Methods =============================================================
	// ====================================================================================================
	/**
	 * 
	 */
	protected void dragDone() {
		//important - after dragging 
		lastDragPoint = null;
		dragSource = null;
	} // dragDone()
			
	// ====================================================================================================
	// ==== Bean variables and methods ====================================================================
	// ====================================================================================================


	// ====================================================================================================
	// ==== Classes =======================================================================================
	// ====================================================================================================

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

} // AccordionUI class

// $Id$
