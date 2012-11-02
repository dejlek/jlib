/**
 * Project: jlib
 * Version: $Id: ComboBoxLink.java 494 2012-10-10 10:59:03Z dejan $
 * License: SPL
 *
 * This file is best viewed with 110 columns.
 * 34567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
 *
 * Authors (in chronological order):
 *   Santhosh Kumar
 * Contributors:
 *   Dejan Lekic
 */

package com.areen.jlib.gui;

/**
 * MySwing: Advanced Swing Utilites
 * Copyright (C) 2005  Santhosh Kumar T
 * <p/>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * CompomentTitleBorder is a border which can allow to place any component in it.
 * 
 * NOTE: border is not a visual component so we can't add a component) 
 * 
 * Code taken from the following page: http://www.javalobby.org/java/forums/t33048.html
 * 
 * @author Santhosh Kumar
 */
public class ComponentTitledBorder implements Border, MouseListener, MouseMotionListener, SwingConstants {
    private int offset = 5;
    private Component comp;
    private JComponent container;
    private Rectangle rect;
    private Border border;
    private boolean mouseEntered = false;
 
    public ComponentTitledBorder(Component argComp, JComponent argContainer, Border argBorder) {
        comp = argComp;
        container = argContainer;
        border = argBorder;
        argContainer.addMouseListener(this);
        argContainer.addMouseMotionListener(this);
    }
 
    @Override
    public boolean isBorderOpaque() {
        return true;
    }
 
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Insets borderInsets = border.getBorderInsets(c);
        Insets insets = getBorderInsets(c);
        int temp = (insets.top - borderInsets.top) / 2;
        border.paintBorder(c, g, x, y + temp, width, height - temp);
        Dimension size = comp.getPreferredSize();
        rect = new Rectangle(offset, 0, size.width, size.height);
        SwingUtilities.paintComponent(g, comp, (Container) c, rect);
    }
 
    @Override
    public Insets getBorderInsets(Component c) {
        Dimension size = comp.getPreferredSize();
        Insets insets = border.getBorderInsets(c);
        insets.top = Math.max(insets.top, size.height);
        return insets;
    }
 
    private void dispatchEvent(MouseEvent me) {
        if (rect != null && rect.contains(me.getX(), me.getY())) {
            dispatchEvent(me, me.getID());
        }
    }
 
    private void dispatchEvent(MouseEvent me, int id) {
        Point pt = me.getPoint();
        pt.translate(-offset, 0);
 
        comp.setSize(rect.width, rect.height);
        comp.dispatchEvent(new MouseEvent(comp, id, me.getWhen(),
                me.getModifiers(), pt.x, pt.y, me.getClickCount(),
                me.isPopupTrigger(), me.getButton()));
        if (!comp.isValid()) {
            container.repaint();
        }
    }
 
    @Override
    public void mouseClicked(MouseEvent me) {
        dispatchEvent(me);
    }
 
    @Override
    public void mouseEntered(MouseEvent me) {
    }
 
    @Override
    public void mouseExited(MouseEvent me) {
        if (mouseEntered) {
            mouseEntered = false;
            dispatchEvent(me, MouseEvent.MOUSE_EXITED);
        }
    }
 
    @Override
    public void mousePressed(MouseEvent me) {
        dispatchEvent(me);
    }
 
    @Override
    public void mouseReleased(MouseEvent me) {
        dispatchEvent(me);
    }
 
    @Override
    public void mouseDragged(MouseEvent e) {
    }
 
    @Override
    public void mouseMoved(MouseEvent me) {
        if (rect == null) {
            return;
        }
 
        if (!mouseEntered && rect.contains(me.getX(), me.getY())) {
            mouseEntered = true;
            dispatchEvent(me, MouseEvent.MOUSE_ENTERED);
        } else if (mouseEntered) {
            if (rect.contains(me.getX(), me.getY())) {
                mouseEntered = false;
                dispatchEvent(me, MouseEvent.MOUSE_EXITED);
            } else {
                dispatchEvent(me, MouseEvent.MOUSE_MOVED);
            }
        }
    } // mouseMoved() method
    
} // ComponentTitledBorder class

// $Id: ComponentTitledBorder.java 253 2012-05-24 09:54:41Z mehjabeen $
