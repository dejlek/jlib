/**
 * Project: jlib
 * Version: $Id$
 * License: SPL
 * 
 * Authors (in chronological order):
 *   Dejan Lekic - http://dejan.lekic.org
 * Contributors (in chronological order):
 *   -
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
    private boolean pictureAvailable; /// Indicates whether picture exists on remote server or not.
    
    public ImageLoaderSW(JLabel argLabel, URL argURL) {
        imageLabel = argLabel;
        webAddress = argURL;
        pictureAvailable = false;
    }
    
    @Override
    protected Image doInBackground() throws Exception {
        Image image = null;
        try {
            image = ImageIO.read(webAddress);
        } catch (IOException e) {
        	//e.printStackTrace();
                System.err.println("ImageLoaderSW:IOException caught, cause: " + e.getCause());
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
                // we use this value to check whether we may show picture dialog or not
                imageLabel.setName("imageLabel(na)"); 
                pictureAvailable = false;
            } else {
                imageLabel.setIcon(new ImageIcon(image));
                imageLabel.setText("");
                imageLabel.setName("imageLabel(ok)");
                pictureAvailable = true;
            }
        } catch (InterruptedException ex) {
            System.out.println(ex);
            //Logger.getLogger(ImageLoaderSW.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            System.out.println(ex);
            //Logger.getLogger(ImageLoaderSW.class.getName()).log(Level.SEVERE, null, ex);
        } // catch
    } // done() method

    public boolean isPictureAvailable() {
        return pictureAvailable;
    }
    
} // ImageLoaderSW class

// $Id$
