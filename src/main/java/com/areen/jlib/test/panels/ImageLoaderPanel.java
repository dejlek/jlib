/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.test.panels;

import com.areen.jlib.gui.ImageLoaderSW;
import com.areen.jlib.gui.PictureDialog;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author Dejan
 */
public class ImageLoaderPanel extends javax.swing.JPanel {

    /**
     * Creates new form ImageLoaderPanel
     */
    public ImageLoaderPanel() {
        initComponents();
        try {
            ImageLoaderSW ilsw = new ImageLoaderSW(imageLabel,
                    new URL("http://www.areen-online.co.uk/staffnet/prj/9999/1234-t.jpg"));
            ilsw.execute();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ImageLoaderPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public JLabel getImageLabel() {
        return imageLabel;
    }

    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this
     * code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imageLabel = new javax.swing.JLabel();

        imageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imageLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void imageLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imageLabelMouseClicked
        PictureDialog pd = new PictureDialog(null, true);
        pd.setURL("http://www.areen-online.co.uk/staffnet/prj/9999/1234-p.jpg");
        pd.setVisible(true);
    }//GEN-LAST:event_imageLabelMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel imageLabel;
    // End of variables declaration//GEN-END:variables

} // ImageLoaderPanel class

// $Id$
