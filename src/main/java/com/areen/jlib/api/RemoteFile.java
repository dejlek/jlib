/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.api;

import java.io.File;

/**
 * An interface for every kind of file (virtual).
 * 
 * @author Dejan
 */
public interface RemoteFile {
    String getPath();
    void setPath(String argPath);
    boolean exists();
    boolean canWrite();
    String getDescription();
    void setDescription(String argDesc);
    boolean upload(File argFile);
    boolean delete();
    void open();
    boolean newVersion(File argFile);
    String getExtension();
    String getMimeType();
    String[] getAllowedExtensions();
    void setAllowedExtensions(String[] extensions);
} // JFile interface
