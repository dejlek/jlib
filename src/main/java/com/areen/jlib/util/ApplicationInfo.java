/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
