/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.areen.jlib.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.Icon;

/**
 *
 * @author dejan
 */
public class DummyIcon implements Icon {

    private String text;
    private Color fg;
    private Color bg;
    private int width;
    private int height;
    
    public DummyIcon(String argText, Color argFG, Color argBG, int argWidth) {
        text = argText;
        fg = argFG;
        bg = argBG;
        width = argWidth;
        height = argWidth;
    }
    
    public DummyIcon(String argText, Color argFG, Color argBG, int argWidth, int argHeight) {
        this(argText, argFG, argBG, argWidth);
        height = argHeight;
    }
    
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        /* TODO: we can adjust the size of the icon depending on the size
                 of the component!
        Dimension d = c.getSize();
        size 
        */
        System.out.println(x + "," + y);
        
        g.setColor(bg);
        g.fillRect(x, y, width, height);
        g.setColor(fg);
        g.setFont(Font.decode("Arial-BOLD-12"));
        g.drawString(text, x + 1, y + height - 1);
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }
    
} // DummyIcon class

// $Id$
