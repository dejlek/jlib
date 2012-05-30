/**
 * Project: jlib
 * Version: $Id$
 * License: SPL
 *
 * This file is best viewed with 110 columns.
 * 34567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
 *
 * Authors (in chronological order):
 *   Darryl Burke
 *   Dejan Lekic - http://dejan.lekic.org
 * Contributors (in chronological order):
 *   -
 */

package com.areen.jlib.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 * A simple class if we need a JButton with vertical text.
 * The source code has been taken from this thread:
 *   http://forums.sun.com/thread.jspa?threadID=5264675
 *
 * Additional changes by: dejan
 *
 * @author Darryl Burke
 */
public class VerticalButton extends JButton {

    /**
     * 
     * @param caption
     * @param clockwise
     */
    public VerticalButton(final String caption, final boolean clockwise) {
        Font f = getFont();
        FontMetrics fm = getFontMetrics(f);
        int captionHeight = fm.getHeight();
        int captionWidth = fm.stringWidth(caption);
        BufferedImage bi = new BufferedImage(captionHeight + 4,
                captionWidth + 4, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();

        g.setColor(new Color(0, 0, 0, 0)); // transparent
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());

        g.setColor(getForeground());
        g.setFont(f);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (clockwise) {
            g.rotate(Math.PI / 2);
        } else {
            g.rotate(-Math.PI / 2);
            g.translate(-bi.getHeight(), bi.getWidth());
        } // else
        g.drawString(caption, 2, -6);

        Icon icon = new ImageIcon(bi);
        setIcon(icon);

        setMargin(new Insets(15, 2, 15, 2));
        setActionCommand(caption);
    } // VerticalButton constructor

    /**
     * 
     * @param caption
     */
    public VerticalButton(final String caption) {
        this(caption, false);
    } // VerticalButton constructor

    /**
     * 
     * @param args
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame frame = new JFrame("Vertical Button Demo");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new FlowLayout());
                frame.add(new VerticalButton("Vertical Up", false));
                frame.add(new VerticalButton("Vertical Down", true));
                frame.pack();
                frame.setVisible(true);
            } // run() method
        });
    } // main() method

} // VerticalButton class

// $Id$
