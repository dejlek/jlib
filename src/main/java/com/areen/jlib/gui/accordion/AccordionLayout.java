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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import com.areen.jlib.gui.accordion.AccordionModel.AccordionPane;

/**
 * Layout manager responsible for specifing positions and sizes of each pane in an Accordion
 * 
 * @author Matthew
 */
public class AccordionLayout implements LayoutManager {

	// ====================================================================================================
	// ==== Variables =====================================================================================
	// ====================================================================================================

	// :::::: PRIVATE/PROTECTED :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	private AccordionModel model;

	// ====================================================================================================
	// ==== Constructors ==================================================================================
	// ====================================================================================================

	public AccordionLayout(AccordionModel argModel) {
		this.model = argModel;
	} // AccordionLayoutManager constructor (default)

	// ====================================================================================================
	// ==== Interface/Superclass Methods ==================================================================
	// ====================================================================================================

	@Override
	public void addLayoutComponent(String string, Component container) {
		
		System.out.println("added new :D");
	//	TODO Auto-generated method stub
	}

	@Override
	public void layoutContainer(Container container) {
		int currentX = 0;
		int currentY = 0;
		int width = 0;
		int height = 0;
		int paneNumber = 0; // number of the pane = index in the model! 
		// Separators do not count to it.

		Dimension dimensions; 
		AccordionPane pane;

		boolean horizontal = model.isHorizontal();

		// get components
		Component[] components = container.getComponents();

		// compute empty space - total space to be divided among panels which work 
		// by weights - no width/height specified 
		int emptySpace = getAvailableSpace(container);

	//	int expandedCount = model.getExpanded().size();
		
		//lay out each component
		for (Component c : components) {

			//if we are dealing with title pane
			if (c instanceof TitledPane) {
				pane = model.getPaneAt(paneNumber);
				dimensions = model.getPaneAt(paneNumber).getDimension();

				//if in a row keep height the same and increment X
				if (horizontal) {
					// titled pane expanded and there are more than one pane expanded
					// set the width otherwise it's set size will be ignored!
					if (pane.isExpanded()) {
						if (dimensions != null) {
							width = dimensions.width;
						} else {
							width = (int) (model.getWeightShare(paneNumber) * emptySpace);
						}
					} else { // collapsed
						width = model.titleSize;
					} //else

					c.setBounds(currentX, 0, width, container.getHeight());

					//update the next X position
					currentX += width;
				} else { // column - height differs, X stays the same (Y increases)
					if (pane.isExpanded()) { //expanded titled pane  and there is more than one expanded
						if (dimensions != null) {
							height = dimensions.height;
						} else {
							height = (int) (model.getWeightShare(paneNumber) * emptySpace);
						}
					} else { // collapsed
						height = model.titleSize;
					} //else

					c.setBounds(0, currentY, container.getWidth(), height);

					//update the next Y position
					currentY += height;
				} //else

				// increment pane number
				paneNumber++;
			} else { //dealing with separators
				//horizontal row
				if (horizontal) {
					width = model.separatorSize;
					height = container.getHeight(); // as high as the whole container

					c.setBounds(currentX, 0, width, height);

					//update the next X position
					currentX += width;
				} else { //all components in a column
					width = container.getWidth();
					height = model.separatorSize;

					c.setBounds(0, currentY, width, height);

					//update the next Y position
					currentY += height;
				} // else
			} // else
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container c) {
		return model.calculateMinimumSize();
	}

	@Override
	public Dimension preferredLayoutSize(Container container) {
		int width = 0;
		int height = 0;

		Component[] cs = container.getComponents();
	
		//calculate sizes
		for (Component c : cs) {
			if (model.isHorizontal()) {
				width += c.getPreferredSize().getWidth();
			} else {
				height += c.getPreferredSize().getHeight();
			}
		} // for

		//calculate remaining dimension
		if (model.isHorizontal()) {
			height = model.calculateHighestTitledPane();
		} else {
			width = model.calculateWidestTitledPane();
		}

		return new Dimension(width, height);
	}

	@Override
	public void removeLayoutComponent(Component c) {
		// TODO Auto-generated method stub
	}

	// ====================================================================================================
	// ==== Private/Protected/Package Methods =============================================================
	// ====================================================================================================

	/**
	 * Calculates available space in the container. It takes total height or width depending on 
	 * the orientation of the layout. If column it will return height remaining, width otherwise.
	 * It only looks for the space to be taken by expanded panes and collapsed specified size. 
	 * @return
	 */
	private int getAvailableSpace(Container c) {
		//calculate
		if (model.isHorizontal()) {
			return c.getWidth() - model.getOccupiedHorizontalSpace();
		} else {
			return c.getHeight() - model.getOccupiedVerticalSpace();
		} //else

	}
} // AccordionLayoutManager class

// $Id$
