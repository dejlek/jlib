/**
 * Project: jlib
 * Version: $Id$
 * License: SPL
 * 
 * Authors (in chronological order):
 *   Dejan Lekic - http://dejan.lekic.org
 *   Mateusz Dykiert
 * Contributors (in chronological order):
 *   -
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
    boolean canDelete();
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
    void showProperties();
    String getToolTip();
    String getAuxText();
} // RemoteFile interface

// $Id$