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
 * Contributor(s): 
 *   Dejan Lekic , http://dejan.lekic.org
 */
package com.areen.jlib.gui.accordion;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import com.areen.jlib.gui.accordion.AccordionModel.AccordionPane;
import javax.swing.JLabel;

/**
 * Layout manager responsible for specifing positions and sizes of each pane in an Accordion.
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
    
    // :::: LayoutManager methods :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    @Override
    public void addLayoutComponent(String string, Component container) {
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
        int emptySpace = model.getAvailableSpace(container);

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
                        if (!pane.isResizable() && dimensions != null) {
                            width = dimensions.width;
                        } else {
                            width = (int) (model.getWeightShare(paneNumber) * emptySpace);
                        }
                    } else { // collapsed
                        width = model.getTitleSize();
                    } //else

                    c.setBounds(currentX, 0, width, container.getHeight());

                    //update the next X position
                    currentX += width;
                } else { // column - height differs, X stays the same (Y increases)
                    if (pane.isExpanded()) { 
                        //expanded titled pane  and there is more than one expanded
                        if (!pane.isResizable() && dimensions != null) {
                            height = dimensions.height;
                        } else {
                            height = (int) (model.getWeightShare(paneNumber) * emptySpace);
                        }
                    } else { // collapsed
                        height = model.getTitleSize();
                    } //else

                    c.setBounds(0, currentY, container.getWidth(), height);

                    //update the next Y position
                    currentY += height;
                } //else

                // increment pane number
                paneNumber++;
            } else if (c instanceof JLabel) { //dealing with separators
                //horizontal row
                if (horizontal) {
                    width = model.getSeparatorSize();
                    height = container.getHeight(); // as high as the whole container
                    c.setBounds(currentX, 0, width, height);
                    ((JLabel) c).setAlignmentY(Component.CENTER_ALIGNMENT);
                    //update the next X position
                    currentX += width;
                } else { //all components in a column
                    width = container.getWidth();
                    height = model.getSeparatorSize();
                    ((JLabel) c).setAlignmentX(Component.CENTER_ALIGNMENT);
                    c.setBounds(0, currentY, width, height);

                    //update the next Y position
                    currentY += height;
                } // else
            } // else
        } // foreach
    } // layoutContainer() method

    /**
     * {@inheritDoc }
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        // Dimension d = model.calculateMinimumSize();
        // System.out.println("M " + d);
        // return d;
        return model.calculateMinimumSize();
    } // minimumLayoutSize() method.

    @Override
    public Dimension preferredLayoutSize(Container container) {
        int width = 0;
        int height = 0;

        //calculate remaining dimension
        if (model.isHorizontal()) {
            height = container.getHeight();
            width = model.calculateWidestTitledPane();
        } else {
            height = model.calculateHighestTitledPane();
            width = container.getWidth();
        }
        
        return new Dimension(width, height);
    }

    @Override
    public void removeLayoutComponent(Component c) {
        // TODO Auto-generated method stub
    }

} // AccordionLayoutManager class

// $Id$
