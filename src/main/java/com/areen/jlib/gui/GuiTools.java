/**
 * Project: jlib
 * Version: $Id$
 * License: SPL
 *
 * This file is best viewed with 110 columns.
 * 34567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
 *
 * Authors (in chronological order):
 *   Dejan Lekic - http://dejan.lekic.org
 * Contributors (in chronological order):
 *   -
 */

package com.areen.jlib.gui;

import com.areen.jlib.gui.fcb.ComboBoxFilter;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 *
 * @author Dejan
 */
public class GuiTools {
    private static Component parentFrame = null;
    static final Logger LOGGER = Logger.getLogger(ComboBoxFilter.class.getCanonicalName());
    
    /**
     * Use this method if you want your application to stop whenever a resource is missing.
     * 
     * Example:
     *   URL url = GuiTools.getResource(myJFrame, "/icons/help.png");
     *   If /icons/help.png file is missing, a dialog will appear with the alert, and application will exit.
     *
     * @param argClass
     * @param argResource
     * @return
     */
    public static URL getResource(Class<?> argClass, String argResource) {
        URL url = argClass.getResource(argResource);
        if (url == null) {
            if (parentFrame != null) {
                parentFrame.setVisible(false);
            } // if
            JOptionPane.showMessageDialog(GuiTools.parentFrame, "Resource '" + argResource + "' is missing.\n"
                    + "Application will now exit. Please contact developers and report this problem."
                    , "Resource missing!", JOptionPane.ERROR_MESSAGE);
            System.exit(111); // We will exit with code 111 to notify (possibly) some other apps about
                              // the error.
        } // if
        return url;
    } // getResource() method

    /**
     * 
     * @param argComponent
     */
    public static void setParentComponent(Component argComponent) {
        GuiTools.parentFrame = argComponent;
    } // setParentComponent() method

    /**
     * Use this method when you want to set font of several JComponent objects to be bold.
     * @param argComponents 
     */
    public static void makeBold(JComponent... argComponents) {
        for (JComponent component : argComponents) {
            Font newFont = new Font(component.getFont().getName(), Font.BOLD, component.getFont().getSize());
            component.setFont(newFont);
        } // foreach
    } // makeBold() method
    
    /**
     * 
     * @param argComponent
     * @return
     */
    public static JFrame getFrame(JComponent argComponent) {
        boolean found = false;
        JFrame result = null;
        Container container = argComponent.getParent();
        
        if (container == null) {
            return result;
        }
        
        while (!found) {
            if (container instanceof JFrame) {
                result = (JFrame) container;
                found = true;
            } else if (container instanceof JDialog) {
                JDialog dlg = (JDialog) container;
                Window ownerWindow = dlg.getOwner();
                if (ownerWindow instanceof JFrame) {
                    result = (JFrame) dlg.getOwner(); /* MD - this is not correct! 
                     * JDialog inherits from Frame not JFRame! */
                } // if
                found = true;
            } else {
                container = container.getParent();
            } // else
        } // while
        return result; // we should never reach this line
    } // getFrame() method
    
    /**
     * TODO: The get frame method is not entirely correct. We need to change occurrences where we use it
     * to getWindow...
     * @param argComponent
     * @return 
     */
    public static Window getWindow(JComponent argComponent) {
         boolean found = false;
        Window result = null;
        Container container = argComponent.getParent();
        
        if (container == null) {
            return result;
        }
        
        while (!found) {
            if (container instanceof JFrame) {
                result = (JFrame) container;
                found = true;
            } else if (container instanceof JDialog) {
                JDialog dlg = (JDialog) container;
                result = (Frame) dlg.getOwner();
                found = true;
            } else {
                container = container.getParent();
            } // else
        } // while
        return result; // we should never reach this line
    }
    
    /**
     * Use this method to create a screenshot of the JFrame object argFrame.
     * @param argPrefix 
     * @param argFrame JFrame you want to make screenshot of.
     * @return File containing the screenshot.
     */
    public static File makeScreenshot(String argPrefix, JFrame argFrame) {
        String prefix = (argPrefix == null) ? "screenshot" : argPrefix;
        Rectangle rec = argFrame.getBounds();
        BufferedImage frameImage = new BufferedImage(rec.width, rec.height, BufferedImage.TYPE_INT_ARGB);
        argFrame.paint(frameImage.getGraphics());
        int x = argFrame.getX();
        int y = argFrame.getY();
        int screenShotWidth = rec.width;
        int screenShotHeight = rec.height;
        Window[] frames = argFrame.getWindows();
        for (Window frame : frames) {
            if (frame.isVisible()) {
                int frameX = frame.getX();
                if (x > frameX) {
                    x = frameX;
                } // if
                int frameY = frame.getY();
                if (y > frameY) {
                    y = frameY;
                } // if
            } // if
        } // for
        for (Window frame : frames) {
            if (frame.isVisible()) {
                int frameX = frame.getX();
                if (screenShotWidth < (frameX + frame.getWidth())) {
                    screenShotWidth = frameX + frame.getWidth();
                } // if
                int frameY = frame.getY();
                if (screenShotHeight < (frameY + frame.getHeight())) {
                    screenShotHeight = frameY + frame.getHeight();
                } // if
            } // if
        } // for
        screenShotWidth = screenShotWidth - x;
        screenShotHeight = screenShotHeight - y;
        BufferedImage screenShotImage = new BufferedImage(screenShotWidth, screenShotHeight, 
                BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = screenShotImage.getGraphics();
        graphics.drawImage(frameImage, rec.x - x, rec.y - y, argFrame);
        for (Window frame : frames) {
            BufferedImage currentFrameImage = new BufferedImage(frame.getWidth(), frame.getHeight(), 
                    BufferedImage.TYPE_INT_ARGB);
            frame.paint(currentFrameImage.getGraphics());
            graphics.drawImage(currentFrameImage, frame.getX() - x, frame.getY() - y, argFrame);
        } // for
        File temp = null;
        try {
            // Create temp file.
            temp = File.createTempFile(prefix, ".png");
            
            // Use the ImageIO API to write the bufferedImage to a temporary file
            ImageIO.write(screenShotImage, "png", temp);

            // Delete temp file when program exits.
            temp.deleteOnExit();
            
        } catch (IOException ioe) {
            LOGGER.debug(ioe.toString());
        } // catch
        return temp;
    } // makeScreenshot() method

} // GuiTools

// $Id$
