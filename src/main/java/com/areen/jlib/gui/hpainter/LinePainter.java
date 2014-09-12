/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.areen.jlib.gui.hpainter;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

/*
 *  Track the movement of the Caret by painting a background line at the
 *  current caret position.
 *
 *  http://tips4java.wordpress.com/2008/10/29/line-painter/
 *  Author: Rob Camick
 */
public class LinePainter
        implements Highlighter.HighlightPainter, CaretListener, MouseListener, MouseMotionListener {

    private JTextComponent component;

    private Color color;

    private Rectangle lastView;

    /*
     *  The line color will be calculated automatically by attempting
     *  to make the current selection lighter by a factor of 1.2.
     *
     *  @param component  text component that requires background line painting
     */
    public LinePainter(JTextComponent argComponent) {
        this(argComponent, null);
        setLighter(argComponent.getSelectionColor());
    }

    /*
     *  Manually control the line color
     *
     *  @param component  text component that requires background line painting
     *  @param color      the color of the background line
     */
    public LinePainter(JTextComponent argComponent, Color argColor) {
        component = argComponent;
        setColor(argColor);

		//  Add listeners so we know when to change highlighting
        argComponent.addCaretListener(this);
        argComponent.addMouseListener(this);
        argComponent.addMouseMotionListener(this);

		//  Turn highlighting on by adding a dummy highlight
        try {
            argComponent.getHighlighter().addHighlight(0, 0, this);
        } catch (BadLocationException ble) {
            // TODO: implement proper handler
        }
    }

    /*
     *	You can reset the line color at any time
     *
     *  @param color  the color of the background line
     */
    public void setColor(Color argColor) {
        color = argColor;
    }

    /*
     *  Calculate the line color by making the selection color lighter
     *
     *  @return the color of the background line
     */
    public void setLighter(Color argColor) {
        int red = Math.min(255, (int) (argColor.getRed() * 1.2));
        int green = Math.min(255, (int) (argColor.getGreen() * 1.2));
        int blue = Math.min(255, (int) (argColor.getBlue() * 1.2));
        setColor(new Color(red, green, blue));
    }

	//  Paint the background highlight
    public void paint(Graphics g, int p0, int p1, Shape bounds, JTextComponent c) {
        try {
            Rectangle r = c.modelToView(c.getCaretPosition());
            g.setColor(color);
            g.fillRect(0, r.y, c.getWidth(), r.height);

            if (lastView == null) {
                lastView = r;
            }
        } catch (BadLocationException ble) {
            // TODO: implement proper handler
        }
    }

    /*
     *   Caret position has changed, remove the highlight
     */
    private void resetHighlight() {
		//  Use invokeLater to make sure updates to the Document are completed,
        //  otherwise Undo processing causes the modelToView method to loop.

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    int offset = component.getCaretPosition();
                    Rectangle currentView = component.modelToView(offset);

					//  Remove the highlighting from the previously highlighted line
                    if (lastView.y != currentView.y) {
                        component.repaint(0, lastView.y, component.getWidth(), lastView.height);
                        lastView = currentView;
                    }
                } catch (BadLocationException ble) {
                    // TODO: implement proper handler
                }
            }
        });
    }

	//  Implement CaretListener
    public void caretUpdate(CaretEvent e) {
        resetHighlight();
    }

	//  Implement MouseListener
    public void mousePressed(MouseEvent e) {
        resetHighlight();
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

	//  Implement MouseMotionListener
    public void mouseDragged(MouseEvent e) {
        resetHighlight();
    }

    public void mouseMoved(MouseEvent e) {
    }
}
