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

import java.awt.BorderLayout;
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
public class RemoteFileButton extends JComponent {
    private RemoteFileButtonModel model;
    
    public RemoteFileButton() {
        super();
        model = new RemoteFileButtonModel();
        setBorder(new BevelBorder(BevelBorder.RAISED));
        setLayout(new BorderLayout());
        add(new JButton("[#]"), BorderLayout.WEST);
        add(new JLabel("Blah"), BorderLayout.CENTER);
        add(new JButton("[x]"), BorderLayout.EAST);
    } // RemoteFileButton constructor (default)
            
    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.add(new RemoteFileButton(), BorderLayout.NORTH);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                frame.setVisible(true);
            }
            
        });

    } // main() method
    
} // RemoteFileButton class

// $Id$
