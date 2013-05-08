/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author Dejan
 */
public class RemoteFileButton extends JComponent {
    
    public RemoteFileButton() {
        super();
        setBorder(new BevelBorder(BevelBorder.RAISED));
        setLayout(new BorderLayout());
        add(new JButton("[#]"), BorderLayout.WEST);
        add(new JLabel("Blah"), BorderLayout.CENTER);
        add(new JButton("[x]"), BorderLayout.EAST);
    }
            
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

    }
    
}
