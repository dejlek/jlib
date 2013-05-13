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
import com.areen.jlib.gui.GuiTools;
import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

/**
 * JComponent made for the sole purpose of maintaining various remote files.
 * 
 * What it does:
 *   1) It indicates clearly whether remote document is uploaded or not.
 *   2) It is capable of triggering upload of file either by drag&drop, or by clicking on the button and
 *      invoking the file select dialog so user can pick file to upload.
 *   3) It has a button to delete the remote file in the case wrong file has been uploaded.
 * @author Dejan
 */
public class RemoteFileButton 
        extends JComponent 
        implements PropertyChangeListener, DropTargetListener {
    
    // ====================================================================================================
    // ==== Variables =====================================================================================
    // ====================================================================================================
    
    // :::::: PRIVATE/PROTECTED :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    private RemoteFileButtonModel model;
    private BorderLayout layout;
    private JLabel iconLabel;
    private JLabel textLabel;
    private JButton deleteButton;
    private static ImageIcon fileUploadedImageIcon;
    private static ImageIcon fileNotUploadedImageIcon;
    RemoteFile remoteFile;
    
    // ====================================================================================================
    // ==== Constructors ==================================================================================
    // ====================================================================================================
    
    static {
        URL iconUrl = GuiTools.getResource(RemoteFileButton.class, 
                "/com/areen/jlib/res/icons/file_not_available.png");
        fileNotUploadedImageIcon = new ImageIcon(iconUrl);
        iconUrl = GuiTools.getResource(RemoteFileButton.class, 
                "/com/areen/jlib/res/icons/yes.png");
        fileUploadedImageIcon = new ImageIcon(iconUrl);
    } // static constructor
    
    public RemoteFileButton(String argText) {
        super();
        
        model = new RemoteFileButtonModel(new TestFile());
        model.setCaption(argText);
        model.addPropertyChangeListener(this);
        
        setBorder(new BevelBorder(BevelBorder.RAISED));
        
        layout = new BorderLayout();
        layout.setHgap(3);
        layout.setVgap(3);
        setLayout(layout);
        
        // ::::: ICON :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        iconLabel = new JLabel();
        iconLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        iconLabel.setIcon(fileNotUploadedImageIcon);
        add(iconLabel, BorderLayout.WEST);
        iconLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e); //To change body of generated methods, choose Tools | Templates.
                iconLabelMouseClick();
                
            }            
        });
        
        // ::::: TITLE ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        textLabel = new JLabel(argText);
        add(textLabel, BorderLayout.CENTER);
        
        // ::::: Delete button ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        deleteButton = new JButton("X");
        add(deleteButton, BorderLayout.EAST);
        
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e); //To change body of generated methods, choose Tools | Templates.
                System.out.println(e.paramString());
            }            
        });
    } // RemoteFileButton constructor (default)

    // ====================================================================================================
    // ==== Interface/Superclass Methods ==================================================================
    // ====================================================================================================
    
    // ::::: PropertyChangeListener :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("dfsdfasdfa");
        iconLabel.setIcon(fileUploadedImageIcon);
        // Model has been changed, let's update the view
        if (model.isUploaded()) {
            iconLabel.setIcon(fileUploadedImageIcon);
        }
    }

    // ::::: DropTargetListener :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        System.out.println("D&D");
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        System.out.println("D&D");
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        System.out.println("D&D");
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        System.out.println("D&D");
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        System.out.println("D&D");
    }
    
    // ====================================================================================================
    // ==== Public Methods ================================================================================
    // ====================================================================================================
    
    // ====================================================================================================
    // ==== Accessors =====================================================================================
    // ====================================================================================================
    
    public String getText() {
        return textLabel.getText();
    }
    
    public void setText(String argText) {
        textLabel.setText(argText);
    }
    
    public RemoteFileButtonModel getModel() {
        return model;
    }
    
    public void setModel(RemoteFileButtonModel argModel) {
        model = argModel;
    }
    
    // ====================================================================================================
    // ==== Private/Protected/Package Methods =============================================================
    // ====================================================================================================
    
    private void iconLabelMouseClick() {
        System.out.println("iconLabelMouseClick()");
        
        FileDialog fd = new FileDialog(GuiTools.getFrame(RemoteFileButton.this));
        fd.setMode(FileDialog.LOAD);
        fd.setMultipleMode(false);
        fd.setTitle("Select file");
        fd.setVisible(true);
        model.setLocalPath(fd.getFile());
        System.out.println(model);
    }
    
    private void textLabelMouseClick() {
        System.out.println("textLabelMouseClick()");
    }
    
    // ====================================================================================================
    // ==== Main function =================================================================================
    // ====================================================================================================
    
    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(new RemoteFileButton("Test"), BorderLayout.NORTH);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                frame.setVisible(true);
            }
            
        });

    } // main() method
    
} // RemoteFileButton class

// $Id$
