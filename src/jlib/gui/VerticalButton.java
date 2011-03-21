/*
 * Project: jlib
 * VCS: $Id$
 * License: Public Domain
 */

package jlib.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

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

    public VerticalButton(String caption, boolean clockwise) {
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
        }
        g.drawString(caption, 2, -6);

        Icon icon = new ImageIcon(bi);
        setIcon(icon);

        setMargin(new Insets(15, 2, 15, 2));
        setActionCommand(caption);
    } // VerticalButton constructor

    public VerticalButton(String caption) {
        this(caption, false);
    } // VerticalButton constructor

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JFrame frame = new JFrame("Vertical Button Demo");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new FlowLayout());
                frame.add(new VerticalButton("Vertical Up", false));
                frame.add(new VerticalButton("Vertical Down", true));
                frame.pack();
                frame.setVisible(true);
            }
        });
    } // main() method

} // VerticalButton class

// $Id$
