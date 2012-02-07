/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.gui;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 * The reason why we have this class is to fix an annoying problem on Windows when "system" loon-and-feel is
 * in use. The arrow is disabled on Windows when combo-box is editable...
 *
 * @author Dejan
 */
public class ColorArrowUI extends BasicComboBoxUI {

    public static ComboBoxUI createUI(JComponent c) {
        return new ColorArrowUI();
    }

    @Override protected JButton createArrowButton() {
        return new BasicArrowButton(
            BasicArrowButton.SOUTH, // down arrow
            Color.LIGHT_GRAY, // background of the arrow button
            Color.WHITE, // west+north border color
            Color.DARK_GRAY,  // arrow color
            Color.DARK_GRAY); // east+south border color
    } // createArrowButton() method

} // ColorArrowUI class

// $Id$
