/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.test;

import com.areen.jlib.api.RemoteFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used for testing RemoteFileButton, RemoteFileModel, etc.
 * 
 * @author Dejan
 */
public class TestFile implements RemoteFile {
    private String dir = "C:/dejan/tmp";
    private String path = null;
    private String description = "";

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String argPath) {
        if (argPath == null) {
            return;
        }
        if (argPath.isEmpty()) {
            return;
        }
        
        path = argPath;
    } // setPath() method

    @Override
    public boolean exists() {
        if (path == null) {
            return false;
        } else {
            boolean ret = false;
            String fileName = dir + path;
            File f = new File(fileName);
            System.out.println("exists(): " + f.getParentFile());
            ret = f.exists() && f.isFile();
            return ret;
        } // else
    }

    @Override
    public boolean canWrite() {
        if (path == null) {
            return false;
        } else {
            boolean ret = false;
            String fileName = dir + path;
            File f = new File(fileName);
            ret = f.exists() && f.isFile() && f.canWrite();
            return ret;
        } // else
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String argDesc) {
        description = argDesc;
    }

    @Override
    public boolean upload(File argFile) {
        boolean ret = false;
        
        File destFile = new File(getPath());
        
        FileChannel sfc = null;
        FileChannel dfc = null;
        if (!destFile.exists()) {
            try {
                destFile.createNewFile();
                sfc = new FileInputStream(argFile).getChannel();
                dfc = new FileOutputStream(destFile).getChannel();
                dfc.transferFrom(sfc, 0, sfc.size());
                
            } catch (IOException ex) {
                Logger.getLogger(TestFile.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (sfc != null) {
                    try {
                        sfc.close();
                    } catch (IOException ex) {
                        Logger.getLogger(TestFile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } // if
                if (dfc != null) {
                    try {
                        dfc.close();
                    } catch (IOException ex) {
                        Logger.getLogger(TestFile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } // if
                ret = true;
            } // finally
        } // if
        
        return ret;
    } // upload() file
    
    @Override
    public boolean delete() {
        return true;
    }

} // TestFile class
