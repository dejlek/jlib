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
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
        
        while (!found) {
            if (!(container instanceof JPanel)) {
                if (container instanceof JFrame) {
                    //PMDOnlyOneReturn: return (JFrame) container;
                    result = (JFrame) container; //PMDOnlyOneReturn utilised local variable
                    found = true; //PMDOnlyOneReturn ensure break point
                } else {
                    result = null; //PMDOnlyOneReturn utilised local variable
                } // else
            } // if
            container = container.getParent();
        } // while
        return result; // we should never reach this line
    } // getFrame() method
    
    /**
     * Use this method to create a screenshot of the JFrame object argFrame.
     * @param argPrefix 
     * @param argFrame JFrame you want to make screenshot of.
     * @return File containing the screenshot.
     */
    public static File makeScreenshot(String argPrefix, JFrame argFrame) {
        String prefix = (argPrefix == null) ? "screenshot" : argPrefix;
        Rectangle rec = argFrame.getBounds();
        BufferedImage bufferedImage = new BufferedImage(rec.width, rec.height, BufferedImage.TYPE_INT_ARGB);
        argFrame.paint(bufferedImage.getGraphics());
        
        File temp = null;
        try {
            // Create temp file.
            temp = File.createTempFile(prefix, ".png");
            
            // Use the ImageIO API to write the bufferedImage to a temporary file
            ImageIO.write(bufferedImage, "png", temp);

            // Delete temp file when program exits.
            temp.deleteOnExit();
            
        } catch (IOException ioe) {
            LOGGER.debug(ioe.toString());
        } // catch
        return temp;
    } // makeScreenshot() method

} // GuiTools

// $Id$
