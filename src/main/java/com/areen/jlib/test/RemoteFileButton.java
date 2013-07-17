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
 *   Mateusz Dykiert
 * Contributors (in chronological order): 
 *   
 */
package com.areen.jlib.test;

import com.areen.jlib.api.RemoteFile;
import com.areen.jlib.gui.GuiTools;
import com.areen.jlib.util.MIMEUtil;
import com.areen.jlib.util.MIMEUtil.MIMEType;
import com.areen.jlib.util.StringUtility;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 * JComponent made for the sole purpose of maintaining various remote files.
 * 
 * What it does:
 *   1) It indicates clearly whether remote document is uploaded or not.
 *   2) It is capable of triggering upload of file either by drag&drop, or by clicking on the button and
 *      invoking the file select dialog so user can pick file to upload.
 *   3) It has a button to delete the remote file in the case wrong file has been uploaded.
 * 
 * TODO: This class was made long before the ADM and ReqDocPanel, therefore it was designed with different
 *       things in mind. After we designed ADM model it was obvious that we have to either refactor
 *       RemoteFileButton, or write a completely new, similar set of classes.
 * 
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
    private JPopupMenu popupMenu;
    private static ImageIcon fileUploadedImageIcon;
    private static ImageIcon fileNotUploadedImageIcon;
    private RemoteFile remoteFile;

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
    
    public RemoteFileButton(RemoteFile argRemoteFile) {
        super();
        remoteFile = argRemoteFile;
        setBorder(BorderFactory.createRaisedBevelBorder());
        model = new RemoteFileButtonModel(remoteFile);
        model.setCaption(remoteFile.getDescription());
        model.addPropertyChangeListener(this);
                
        layout = new BorderLayout();
        layout.setHgap(3);
        layout.setVgap(3);
        setLayout(layout);
        
        // ::::: ICON :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        iconLabel = new JLabel();
        iconLabel.setIcon(fileNotUploadedImageIcon);
        add(iconLabel, BorderLayout.WEST);
        /*
        if (uploadEnabled) {
            iconLabel.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e); //To change body of generated methods, choose Tools | Templates.
                    iconLabelMouseClick();
                }
            });
        }
        */
        // ::::: TITLE ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        textLabel = new JLabel(model.getCaption());
        add(textLabel, BorderLayout.CENTER);
        
        // ::::: Popup Menu :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        popupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("New Version");
        menuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleNewVersion();
            }
        });
        popupMenu.add(menuItem);
   
        menuItem = new JMenuItem("Delete");
        menuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleDelete();
            }
        });
        popupMenu.add(menuItem);
        
        menuItem = new JMenuItem("Properties");
        menuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remoteFile.showProperties();
            }
        });
        popupMenu.add(menuItem);
                
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(textLabel, e.getX(), e.getY());
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(textLabel, e.getX(), e.getY());
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e); //To change body of generated methods, choose Tools | Templates.
                if (SwingUtilities.isLeftMouseButton(e)) {
                    handleOpen();
                }
            }
        });
    } // RemoteFileButton constructor (default)

    // ====================================================================================================
    // ==== Interface/Superclass Methods ==================================================================
    // ====================================================================================================
    
    // ::::: PropertyChangeListener :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Model has been changed, let's update the view
        if (model.isUploaded()) {
            // handle property change for a document
            refreshIcon();
            refreshToolTip();
        }
    }

    // ::::: DropTargetListener :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        setBorder(BorderFactory.createLineBorder(Color.BLUE));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
        resetDropIndicator();
        setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        Transferable t = dtde.getTransferable();
        System.out.println(Arrays.toString(t.getTransferDataFlavors()));
        DataFlavor fileListDataFlavor = DataFlavor.javaFileListFlavor;
    
        if (t.isDataFlavorSupported(fileListDataFlavor)) {
            try {
                int ret = JOptionPane.showConfirmDialog(GuiTools.getWindow(this), 
                        "Are you sure to create a new version of " + remoteFile.getDescription() + "?", 
                        "New version confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
                if (ret == JOptionPane.NO_OPTION) {
                    resetDropIndicator();
                    return;
                }
                
                dtde.acceptDrop(DnDConstants.ACTION_COPY);
                System.out.println("drop accepted");

                List fileList = (List) t.getTransferData(fileListDataFlavor);
                File[] tempFiles = new File[fileList.size()];
                fileList.toArray(tempFiles);
                final File[] files = tempFiles;
                remoteFile.newVersion(files[0]);
                dtde.getDropTargetContext().dropComplete(true);
                System.out.println("drop complete");
            } catch (UnsupportedFlavorException ex) {
                Logger.getLogger(RemoteFileButton.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(RemoteFileButton.class.getName()).log(Level.SEVERE, null, ex);
            }
        } // if
    

        resetDropIndicator();
    }
    
    // ====================================================================================================
    // ==== Public Methods ================================================================================
    // ====================================================================================================
    
    public void refreshToolTip() {
        System.out.println("Refresh tool top : " + getToolTipText() + " -> " + remoteFile.getToolTip());
        setToolTipText(remoteFile.getToolTip());
        repaint();
    }
    
    public void refreshIcon() {
        MIMEType mTy = MIMEUtil.getMIMEType(remoteFile.getMimeType());
        ImageIcon icon = MIMEUtil.getIcon(mTy, remoteFile.getExtension());
        iconLabel.setIcon(icon);

        repaint();
        if (getParent() != null) {
            getParent().validate();
        }
    }
    
    // ====================================================================================================
    // ==== Accessors =====================================================================================
    // ====================================================================================================
    
    public RemoteFile getRemoteFile() {
        return remoteFile;
    }
    
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
    private void resetDropIndicator() {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        setBorder(BorderFactory.createRaisedBevelBorder());
    }
    
    private void handleDelete() {
        // show confirmation message
        int answer = JOptionPane.showConfirmDialog(GuiTools.getWindow(this), 
                "Are you sure you want to delete "
                + getToolTipText() + " (" + remoteFile.getDescription() + ") file?", 
                "Confirm to delete the file",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        // if confirmed delete the file and remove the button from the parent component
        if (answer == JOptionPane.OK_OPTION) {
            if (remoteFile.delete()) {
                // remove this component from the parent
                Container parent = getParent();
                Window w = GuiTools.getWindow(this);
                parent.remove(this);
                // sometimes it doesn't refresh so force validation of the window
                parent.repaint();
                parent.validate();
            }
        }
    }
            
    private void handleNewVersion() {        
        FileDialog fd = null;
        Window w = GuiTools.getWindow(RemoteFileButton.this);
        if (w instanceof Frame) {
            fd = new FileDialog((Frame) w);
        } else {
            fd = new FileDialog((JFrame) w);
        }
        
        fd.setMode(FileDialog.LOAD);
        // fd.setMultipleMode(false); Java 1.7 feature...
        fd.setTitle("Select file");
        fd.setFile(StringUtility.generateExtensionList(remoteFile.getAllowedExtensions()));
        fd.setVisible(true);
        String fileName = fd.getFile();
        String dir = fd.getDirectory();
        
        // if user has clicked ok
        if (fileName != null && !fileName.contains("*")) {
            model.setLocalPath(fileName);
            remoteFile.newVersion(new File(dir + File.separator + fileName)); 
        }
    }
    
    private void handleOpen() {
        remoteFile.open();
    }
    
    // ====================================================================================================
    // ==== Main function =================================================================================
    // ====================================================================================================
    
    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        TestFile tf = new TestFile();
        tf.setDescription("Test");
        frame.add(new RemoteFileButton(tf), BorderLayout.NORTH);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                frame.setVisible(true);
            }
            
        });

    } // main() method
    
} // RemoteFileButton class

// $Id$
