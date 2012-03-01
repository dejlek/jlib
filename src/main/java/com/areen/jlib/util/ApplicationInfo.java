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
package com.areen.jlib.util;

/**
 * Use this class to keep information about the application.
 *
 * @author dejan
 */
public class ApplicationInfo {
    private String vendor; /// Name of the vendor
    private String name; /// Name of the application

    public ApplicationInfo() {
    }

    public ApplicationInfo(final String argVendor) {
        vendor = argVendor;
    }

    public ApplicationInfo(final String argVendor, final String argName) {
        this(argVendor);
        name = argName;
    }

    public String getName() {
        return name;
    }

    public void setName(final String argName) {
        this.name = argName;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(final String argVendor) {
        this.vendor = argVendor;
    }

} // ApplicationInfo class

// $Id$
