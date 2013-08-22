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
 * Author(s) in chronological order: Mateusz Dykiert , http://dykiert.org Contributor(s): -
 */
package com.areen.jlib.gui.accordion;

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
    static final String PREF_ACCORDION_SIZE = "/test_dialog/accordion_weights";
    // :::::: PRIVATE/PROTECTED :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private Accordion accordion;
    private HashMap<String, String> prefsMap;
    private String key;
    private String expandedKey;
    private String weightsKey;

    // ====================================================================================================
    // ==== Constructors ==================================================================================
    // ====================================================================================================
    public AccordionPrefs(Accordion argAccordion, HashMap<String, String> argPrefsMap, String argKey) {
        accordion = argAccordion;
        prefsMap = argPrefsMap;
        key = argKey;

        expandedKey = key + "/" + accordion.getName() + "_expanded";
        weightsKey = key + "/" + accordion.getName() + "_weights";
    } // AccordionPrefs constructor (default)

    // ====================================================================================================
    // ==== Public Methods ================================================================================
    // ====================================================================================================
    /**
     * Load user preferences
     */
    public void loadPrefs() {
        // load expanded states
        //String expandedString = prefsMap.get(PREF_ACCORDION_EXPANDED);
        String expandedString = prefsMap.get(expandedKey);

        // if the string is not stored give error message  
        if (expandedString == null) {
            System.out.println("AccordionPrefs.loadPrefs() - expanded states don't exist!");
        } else { //load
            loadExpanded(expandedString);
        }

        // load AccordionPane sizes
        //String sizesString = prefsMap.get(PREF_ACCORDION_SIZE);
        String weightsString = prefsMap.get(weightsKey);

        // if the string is not stored give error message  
        if (weightsString == null) {
            System.out.println("AccordionPrefs.loadPrefs() - weights don't exist!");
        } else { //load
            loadWeights(weightsString);
        }
    }

    /**
     * Update user preferences
     */
    public void updatePrefs() {
        // update expanded states
        //     System.out.println("EXPANDED KEY: ...... " + expandedKey);
        prefsMap.put(expandedKey, getExpanded());

        // load AccordionPane weights
        prefsMap.put(weightsKey, getWeights());
    }

    // ====================================================================================================
    // ==== Private/Protected/Package Methods =============================================================
    // ====================================================================================================
    /**
     * Sets the states of each pane within the Accordion. String "expanded" looks like this :
     * "0,0,1,1" where 1 is expanded and 0 is collapsed
     *
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
     * Get the states of each pane Return will looks like this : "0,0,1,1" where 1 is expanded and 0
     * is collapsed
     *
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
     * Sets the weight of each pane within the Accordion. String sizes will looks like this : "0.2,
     * 0.5, 0.2"
     *
     * @param savedWeights
     */
    private void loadWeights(String savedWeights) {
        // get individual sizes
        String[] sizes = savedWeights.split(",");

        double weight;

        // iterate through and set the sizes
        for (int i = 0; i < sizes.length; i++) {
            //parse the size
            weight = Double.parseDouble(sizes[i]);

            //set the size
            accordion.setPaneWeight(i, weight);
        }
    } // loadSizes()

    /**
     * Get the sizes of each pane, width when horizontal = true Return will looks like this : "0.2,
     * 0.5, 0.5" where 0 = null
     */
    private String getWeights() {
        StringBuilder buffer = new StringBuilder();
        // get the sizes
        double[] weights = accordion.getWeights();

        // iterate through states and generate the string
        for (int i = 0; i < weights.length; i++) {
            //append the size
            buffer.append(weights[i]);

            //if not the last add ','
            if (i != weights.length - 1) {
                buffer.append(",");
            }
        }

        return buffer.toString();
    } // getWeights()
} // AccordionPrefs class

// $Id$
