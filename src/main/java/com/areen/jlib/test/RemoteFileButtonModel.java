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

import com.areen.jlib.api.RemoteFile;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 * The model for the RemoteFileButton JComponent implementation.
 * 
 * @author Dejan
 */
public class RemoteFileButtonModel implements Serializable {
    
    private String remotePath = ""; /// Remote file path
    private String localPath = ""; /// Remote file path
    private String type = "application/octet-stream"; /// MIME type of the file - by default it is unknown
    private boolean uploaded = false;
    private String caption = "";
    private RemoteFile jfile;
    
    public RemoteFileButtonModel(RemoteFile argFile) {
        jfile = argFile;
        
        if (jfile.exists()) {
            uploaded = true;
            remotePath = jfile.getPath();
        }
    } // RemoteFileButtonModel constructor
    
    @Override
    public String toString() {
        return "(`" + caption 
                + "` `"
                + localPath 
                + "` `"
                + remotePath
                + "` "
                + type
                + ")";
    } // toString() method
    
    public String getRemotePath() {
        return remotePath;
    }

    /**
     * argPath is a string in the form "/dir/subdir/filename.ext" containing the full path, with file name,
     * of the remote file.
     * 
     * Example:
     * <pre>
     * setRemotePath("/static/gfx/some_picture.png");
     * </pre>
     * @param argPath 
     */
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
        if (!localPath.equals(argLocalPath)) {
            localPath = argLocalPath;
            setModelChanged(true);
        }
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String argCaption) {
        caption = argCaption;
    }

    // ====================================================================================================
    // ==== Bean variables and methods ====================================================================
    // ====================================================================================================

    // These are the common building-blocks we need for handy property change support

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    
    // ::::: modelChanged property ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    private boolean modelChanged = false;
    public static final String PROP_MODELCHANGED = "modelChanged";

    /**
     * Get the value of modelChanged.
     * User probably never needs this method...
     *
     * @return the value of modelChanged
     */
    public boolean isModelChanged() {
        return modelChanged;
    }

    /**
     * Set the value of modelChanged
     *
     * @param argModelChanged new value of modelChanged
     */
    public void setModelChanged(boolean argModelChanged) {
        boolean oldModelChanged = modelChanged;
        modelChanged = argModelChanged;
        propertyChangeSupport.firePropertyChange(PROP_MODELCHANGED, oldModelChanged, argModelChanged);
        
        if (modelChanged) {
            // we change back the "modelChanged" state to false so we can trigger the modelChanged event
            // again when something is changed in the model.
            modelChanged = false;
        } // if
    } // setModelChanged() method

} // RemoveFileButtonModel class

// $Id$
