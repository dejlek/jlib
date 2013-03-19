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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Class to load and update GUI specific preferences.
 *  
 * @author Matthew
 */
public class AccordionPrefs {
	/* Class implementation comment (if any)
	 * 
	 */

	// ====================================================================================================
	// ==== Variables =====================================================================================
	// ====================================================================================================

	// :::::: CONFIGURATION VARIABLES :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	// :::::: PUBLIC ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	static final String PREF_ACCORDION_EXPANDED = "/test_dialog/accordion_expanded";
	static final String PREF_ACCORDION_SIZE = "/test_dialog/accordion_sizes";

	// :::::: PRIVATE/PROTECTED :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	
	private Accordion accordion;
	private HashMap<String, String> prefsMap;

	// ====================================================================================================
	// ==== Constructors ==================================================================================
	// ====================================================================================================

	public AccordionPrefs(Accordion argAccordion, HashMap<String, String> argPrefsMap) {
		accordion = argAccordion;
		prefsMap = argPrefsMap;
	} // Template constructor (default)

	// ====================================================================================================
	// ==== Public Methods ================================================================================
	// ====================================================================================================

	/**
	 * Load user preferences
	 */
	public void loadPrefs() {
		// load expanded states
		String expandedString = prefsMap.get(PREF_ACCORDION_EXPANDED);

		// if the string is not stored give error message  
		if (expandedString == null) {
			System.out.println("AccordionPrefs.loadPrefs() - expanded states don't exist!");
		} else { //load
			loadExpanded(expandedString);
		}

		// load AccordionPane sizes
		String sizesString = prefsMap.get(PREF_ACCORDION_SIZE);

		// if the string is not stored give error message  
		if (sizesString == null) {
			System.out.println("AccordionPrefs.loadPrefs() - sizes don't exist!");
		} else { //load
			loadSizes(sizesString);
		}
	}

	/**
	 * Update user preferences
	 */
	public void updatePrefs() {
		// update expanded states
		prefsMap.put(PREF_ACCORDION_EXPANDED, getExpanded());

		// load AccordionPane sizes
		prefsMap.put(PREF_ACCORDION_SIZE, getSizes());

		System.out.println(prefsMap.size());
	}

	// ====================================================================================================
	// ==== Private/Protected/Package Methods =============================================================
	// ====================================================================================================

	/**
	 * Sets the states of each pane within the Accordion.
	 * String "expanded" looks like this : "0,0,1,1" where 1 is expanded and 0 is collapsed 
	 * @param expanded
	 */
	private void loadExpanded(String expanded) {
		// get individual states
		String[] states = expanded.split(",");

		// iterate through and set the states
		for (int i = 0; i < states.length; i++) {
			// parse the string
			int state = Integer.parseInt(states[i]);

			//expand when 1 and collapse when 0
			switch (state) {
			case 1:
				// expand
				accordion.expand(i);
				break;

			case 0:
				// collapse
				accordion.collapse(i);
				break;    		
			
                        default:
                            break;
                        }
		}
	} // loadExpanded()

	/**
	 * Get the states of each pane
	 * Return will looks like this : "0,0,1,1" where 1 is expanded and 0 is collapsed 
	 * @param expanded
	 */
	private String getExpanded() {
		StringBuilder buffer = new StringBuilder();

		// get expanded panes
		LinkedList<Integer> expanded = accordion.getExpandedPanes();

		// get number of panes
		int panes = accordion.getModel().getPaneCount();

		//get an array for each state of a pane
		int[] states = new int[panes];

		Iterator<Integer> it = expanded.iterator();

		//iterate through the state list, and set set 1 when expanded
		while (it.hasNext()) {
			states[it.next().intValue()] = 1;
		}

		// iterate through states and generate the string
		for (int i = 0; i < panes; i++) {
			buffer.append(states[i]);

			//if not the last add ','
			if (i != panes - 1) {
				buffer.append(",");
			}
		}

		return buffer.toString();
	} // getExpanded()

	/**
	 * Sets the size of each pane within the Accordion.
	 * String sizes will looks like this : "233, 123, 0" where 0 = null
	 * In horizontal Accordion we set width and height when vertical 
	 * @param expanded
	 */
	private void loadSizes(String savedSizes) {
		// get individual sizes
		String[] sizes = savedSizes.split(",");

		Dimension d = null;
		int size;

		// determine whether the accordion is horizontal or vertical
		boolean horizontal = accordion.getModel().isHorizontal();

		// iterate through and set the sizes
		for (int i = 0; i < sizes.length; i++) {
			//parse the size
			size = Integer.parseInt(sizes[i]);

			//if size = 0 then set dimension to null
			if (size == 0) {
				d = null;
				// if horizontal take width
			} else if (horizontal) {
				d = new Dimension(size, 0);
			} else { //if vertical take height
				d = new Dimension(0, size);
			}

			//set the size
			accordion.setPaneSize(i, d);
		}
	} // loadSizes()

	/**
	 * Get the sizes of each pane, width when horizontal = true
	 * Return will looks like this : "233, 123, 0" where 0 = null
	 * @param expanded
	 */
	private String getSizes() {
		StringBuilder buffer = new StringBuilder();

		//if horizontal we store width, height otherwise
		boolean horizontal = accordion.getModel().isHorizontal();

		// get the sizes
		Dimension[] dimensions = accordion.getDimensions();

		//temporary variables
		int size;
		Dimension d;

		// iterate through states and generate the string
		for (int i = 0; i < dimensions.length; i++) {
			//get dimensions
			d = dimensions[i];

			//sometimes there is no size set when the pane hasn't been resized
			if (d != null) {
				if (horizontal) {
					size = (int) dimensions[i].getWidth();
				} else {
					size = (int) dimensions[i].getHeight();
				} 
			} else {
				size = 0;
                        }
                        
			//append the size
			buffer.append(size);

			//if not the last add ','
			if (i != dimensions.length - 1) {
				buffer.append(",");
			}
		}

		return buffer.toString();
	} // getExpanded()

} // AccordionPrefs class

// $Id$
