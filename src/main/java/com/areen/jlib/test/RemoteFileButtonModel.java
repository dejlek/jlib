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
package com.areen.jlib.test;

import java.io.Serializable;

/**
 * The model for the RemoteFileButton JComponent implementation.
 * 
 * @author Dejan
 */
public class RemoteFileButtonModel implements Serializable {
    
    private String remotePath; /// Remote file path
    private String localPath; /// Remote file path
    private String type = "application/octet-stream"; /// MIME type of the file - by default it is unknown
    private boolean uploaded = false;
    
    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String argPath) {
        remotePath = argPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String argType) {
        type = argType;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean argUploaded) {
        uploaded = argUploaded;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String argLocalPath) {
        localPath = argLocalPath;
    }
    
} // RemoveFileButtonModel class

// $Id$
