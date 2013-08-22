/**
 * $Id$
 *
 * Copyright (c) 2009-2012 Areen Design Services Ltd 23 Eyot Gardens; London; W6 9TR
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Panel with two components: title and main panel
 *
 * @author Matthew
 */
public class TitledPane extends JPanel {

    /* 
     * TitledPane is a panel with two components, title and the main panel.
     */
    // ====================================================================================================
    // ==== Variables =====================================================================================
    // ====================================================================================================
    boolean horizontal;

    // ====================================================================================================
    // ==== Constructors ==================================================================================
    // ====================================================================================================
    /**
     *
     */
    public TitledPane(String string, boolean argHorizontal) {
        this.horizontal = argHorizontal;

        setLayout(new BorderLayout());

        RotatableTitle title = new RotatableTitle(string, horizontal, 12);
        JPanel panel = new JPanel();

        if (horizontal) {
            add(title, BorderLayout.WEST);
        } else {
            add(title, BorderLayout.NORTH);
        }

        add(panel, BorderLayout.CENTER);


    }

    public TitledPane(String titleString, JComponent component, boolean argHorizontal) {
        this.horizontal = argHorizontal;

        RotatableTitle title = new RotatableTitle(titleString, horizontal, 12);

        setLayout(new BorderLayout());

        if (horizontal) {
            add(title, BorderLayout.WEST);
        } else {
            add(title, BorderLayout.NORTH);
        }

        add(component, BorderLayout.CENTER);

    } // TitledPane constructor (String, JComponent)

    // ====================================================================================================
    // ==== Public Methods ================================================================================
    // ====================================================================================================
    /**
     * Switch and rotate the component - it only does rotate if different state is set.
     */
    public void setExpanded(boolean expanded) {
        RotatableTitle title = (RotatableTitle) ((BorderLayout) getLayout())
                .getLayoutComponent(BorderLayout.NORTH);

        //we only need to trigger a change when pane in horizontal accordion is detected
        if (horizontal) {
            if (expanded) {
                //check if there is anything in the north panel
                title = (RotatableTitle) ((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.WEST);

                //only if we have to move the panel from WEST to NORTH (not previously expanded),
                if (title != null) {
                    this.remove(title);
                    add(title, BorderLayout.NORTH);
                }
            } else {
                title = (RotatableTitle) ((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.NORTH);

                //only if the panel wasn't collapsed already move the title to the west pane
                if (title != null) {
                    this.remove(title);
                    add(title, BorderLayout.WEST);
                }
            }
        }

        // rotate only if we have changed the state we want to rotate. If the same state is set
        // title will be null! 
        if (title != null) {
            title.rotate(expanded);
        }
    }

    /**
     * Set colour to the title component
     */
    public void setTitleBackground(Color colour) {
        getTitle().setBackground(colour);
    }

    /**
     * Set title font size
     *
     * @param fontSize
     */
    public void setTitleFontSize(int fontSize) {
        ((RotatableTitle) getTitle()).setFontSize(fontSize);

    }

    // ====================================================================================================
    // ==== Accessors =====================================================================================
    // ====================================================================================================
    /* Get title of the pane
     * @return
     */
    public JComponent getTitle() {
        //check if there is anything in the north panel
        JComponent c = (JComponent) ((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.NORTH);

        //if no component is found in the north placement
        if (c == null) {
            return (JComponent) ((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.WEST);
        } else {
            return c;
        }
    } // getTitle()

    /**
     * Set the title component
     *
     * @param title
     */
    public void setTitle(Component title, boolean onTop) {
        title.setName("title");

        if (onTop) {
            add(title, BorderLayout.NORTH);
        } else {
            add(title, BorderLayout.WEST);
        }
    } // setTitle()

    /**
     * Returns central pane component
     *
     * @return
     */
    public Component get() {
        return ((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.CENTER);
    } // get()

    /**
     * Sets central pane component
     *
     * @param mainComponent
     */
    public void setMainComponent(Component mainComponent) {
        mainComponent.setName("mainPanel");
        add(mainComponent, BorderLayout.CENTER);
    } // setMainComponent()	
} // TitlePanel class

// $Id$