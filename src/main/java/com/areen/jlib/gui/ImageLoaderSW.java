/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.gui;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

/**
 *
 * @author Dejan
 */
public class ImageLoaderSW extends SwingWorker<Image, Void> {
    JLabel imageLabel;
    URL webAddress;
    
    public ImageLoaderSW(JLabel argLabel, URL argURL) {
        imageLabel = argLabel;
        webAddress = argURL;
    }
    
    @Override
    protected Image doInBackground() throws Exception {
        Image image = null;
        try {
            image = ImageIO.read(webAddress);
        } catch (IOException e) {
        	e.printStackTrace();
        }
        return image;
    } // diInBackground() method

    @Override
    protected void done() {
        try {
            Image image = get();
            if (image == null) {
                imageLabel.setIcon(new ImageIcon(GuiTools.getResource(getClass(), 
                        "/com/areen/jlib/res/icons/image_not_available.png")));
                imageLabel.setText("");
            } else {
                imageLabel.setIcon(new ImageIcon(image));
                imageLabel.setText("");
            }
        } catch (InterruptedException ex) {
            System.out.println(ex);
            //Logger.getLogger(ImageLoaderSW.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            System.out.println(ex);
            //Logger.getLogger(ImageLoaderSW.class.getName()).log(Level.SEVERE, null, ex);
        } // catch
    } // done() method
    
} // ImageLoaderSW class

// $Id$
