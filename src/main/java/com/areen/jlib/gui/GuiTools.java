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

import java.awt.Component;
import java.awt.Font;
import java.net.URL;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 *
 * @author Dejan
 */
public class GuiTools {
    private static Component parentFrame = null;
    
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
            }
            JOptionPane.showMessageDialog(GuiTools.parentFrame, "Resource '" + argResource + "' is missing.\n"
                    + "Application will now exit. Please contact developers and report this problem."
                    , "Resource missing!", JOptionPane.ERROR_MESSAGE);
            System.exit(111); // We will exit with code 111 to notify (possibly) some other apps about
                              // the error.
        } // if
        return url;
    } // getResource() method

    public static void setParentComponent(Component argComponent) {
        GuiTools.parentFrame = argComponent;
    }

    /**
     * Use this method when you want to set font of several JComponent objects to be bold.
     */
    public static void makeBold(JComponent... argComponents) {
        for (JComponent component : argComponents) {
            Font newFont = new Font(component.getFont().getName(), Font.BOLD, component.getFont().getSize());
            component.setFont(newFont);
        } // foreach
    } // makeBold() method

} // GuiTools

// $Id$
