/**
 * Project: jlib
 * Version: $Id$
 * License: SPL
 *
 * This file is best viewed with 110 columns.
1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
 *
 * Authors (in chronological order):
 *  Mateusz Dykiert
 * Contributors (in chronological order):
 *   -
 */


package com.areen.jlib.gui;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * 
 * 
 * @author Matthew
 */
public class TextComponentPopUp extends JPopupMenu {
    /**
     * Key to be used in JComponents to retrieve object of this type
     */
    public static final String CLIENT_PROPERTY_KEY = "TextComponentPopupKey";
    
    /**
     * Character position
     */
    private int characterPosition;
    
    /**
     * Constructor
     */
    public TextComponentPopUp() {
        super();
        JMenuItem item = new JMenuItem("HELLO! !");
        this.add(item);
    }
    
    /**
     * Method to get character position of the mouse button click
     * @return 
     */
    public int getCharacterPosition() {
        return characterPosition;
    }
    
    /**
     * Set character position
     * @param position 
     */
    private void setCharacterPosition(int argCharacterPosition) {
        characterPosition = argCharacterPosition;
    }   
}
