/**
 * Project: jlib
 * Version: $Id: ComponentTitledBorder.java 253 2012-05-24 09:54:41Z mehjabeen $
 * License: SPL
 *
 * This file is best viewed with 110 columns.
 * 34567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
 *
 * Authors (in chronological order):
 *   Mehjabeen Nujurally
 * Contributors (in chronological order):
 *   -
 */
package com.areen.jlib.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 *
 * @author mehjabeen
 */
public class ComponentTitledBorder implements Border, MouseListener, SwingConstants { 
       int offset = 5; 
       Component comp; 
       JComponent container; 
       Rectangle rect; 
       Border border; 
    
       public ComponentTitledBorder(Component argComp, JComponent argContainer, Border argBorder) { 
           this.comp = argComp; 
           this.container = argContainer; 
           this.border = argBorder; 
           container.addMouseListener(this); 
       } // ComponentTitledBorder constructor
    
       public boolean isBorderOpaque() { 
           return true; 
       } // isBorderOpaque() method
    
       public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) { 
           Insets borderInsets = border.getBorderInsets(c); 
           Insets insets = getBorderInsets(c); 
           int temp = (insets.top - borderInsets.top) / 2; 
           border.paintBorder(c, g, x, y + temp, width, height - temp); 
           Dimension size = comp.getPreferredSize(); 
           rect = new Rectangle(offset, 0, size.width, size.height); 
           SwingUtilities.paintComponent(g, comp, (Container) c, rect); 
       } // paintBorder() method
    
       public Insets getBorderInsets(Component c) { 
           Dimension size = comp.getPreferredSize(); 
           Insets insets = border.getBorderInsets(c); 
           insets.top = Math.max(insets.top, size.height); 
           return insets; 
       } // getBorderInsets() method
    
       private void dispatchEvent(MouseEvent me) { 
           if (rect != null && rect.contains(me.getX(), me.getY())) { 
               Point pt = me.getPoint(); 
               pt.translate(-offset, 0); 
               comp.setBounds(rect); 
               comp.dispatchEvent(new MouseEvent(comp, me.getID() 
                       , me.getWhen(), me.getModifiers() 
                       , pt.x, pt.y, me.getClickCount() 
                       , me.isPopupTrigger(), me.getButton())); 
               if (!comp.isValid())  {
                   container.repaint(); 
               } // if
           } // if
       } // dispatchEvent() method 
    
    @Override
       public void mouseClicked(MouseEvent me) { 
           dispatchEvent(me); 
       } // mouseClicked() method
    
    @Override
       public void mouseEntered(MouseEvent me) { 
           dispatchEvent(me); 
       } // mouseEntered() method
    
    @Override
       public void mouseExited(MouseEvent me) { 
           dispatchEvent(me); 
       } // mouseExited() method
    
    @Override
       public void mousePressed(MouseEvent me) { 
           dispatchEvent(me); 
       } // mousePressed() method
    
    @Override
       public void mouseReleased(MouseEvent me) { 
           dispatchEvent(me); 
       } // mouseReleased() method
  } // ComponentTitledBorder class

// $Id: ComponentTitledBorder.java 253 2012-05-24 09:54:41Z mehjabeen $