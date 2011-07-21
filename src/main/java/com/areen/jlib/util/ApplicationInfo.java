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
    
    public ApplicationInfo(String argVendor) {
        vendor = argVendor;
    }
    
    public ApplicationInfo(String argVendor, String argName) {
        this(argVendor);
        name = argName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    
} // ApplicationInfo class

// $Id$
