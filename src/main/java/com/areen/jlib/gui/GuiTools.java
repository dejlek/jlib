/**
 * $Id$
 *
 * Copyright (c) 2009-2010 Areen Design Services Ltd
 * 23 Eyot Gardens; London; W6 9TR
 * http://www.areen.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Areen Design Services Ltd ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Areen Design Services Ltd.
 *
 * This file is best viewed with 110 columns.
12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 *
 * Author(s) in chronological order:
 *   Dejan Lekic , http://dejan.lekic.org
 * Contributor(s):
 *   -
 */

package com.areen.jlib.gui;

import java.net.URL;
import javax.swing.JOptionPane;

/**
 *
 * @author Dejan
 */
public class GuiTools {

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
            JOptionPane.showMessageDialog(null, "Resource '" + argResource + "' is missing.\n"
                    + "Application will now exit. Please contact developers and report this problem."
                    , "Resource missing!", JOptionPane.ERROR_MESSAGE);
            System.exit(111); // We will exit with code 111 to notify (possibly) some other apps about
                              // the error.
        } // if
        return url;
    } // getResource() method

} // GuiTools

// $Id$
