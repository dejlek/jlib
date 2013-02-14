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

/**
 * Every ListCellRenderer that implements this interface will have certain set of methods that can be used
 * to configure their behaviour.
 * 
 * @author Dejan
 */
public interface ConfigurableListCellRenderer {
    int getConfig();
    void setConfig(int argConfig);
} // ConfigurableListCellRenderer interface

// $Id$
