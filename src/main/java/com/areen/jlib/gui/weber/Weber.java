/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.gui.weber;

import javax.swing.event.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
A simple Web Weber with minimal functionality.
@author Jose M. Vidal
 */
public class Weber {

    JPanel panel;
    String title;
    String currentUrl;
    JEditorPane jep;
    LinkFollower linkFollower;

    /** Set the page.
    @param jep the pane on which to display the url
    @param url the url to display */
    protected static void setPage(JEditorPane jep, String argUrl) {
        try {
            jep.setPage(argUrl);
        } catch (IOException e) {
            System.err.println(argUrl);
            System.err.println(e);
            System.exit(-1);
        }

    }

    /** An inner class which listens for keypresses on the Back button. */
    class BackButtonListener implements ActionListener {

        protected JEditorPane jep;
        protected JLabel label;
        protected JButton backButton;
        protected Vector history;

        public BackButtonListener(JEditorPane jep, JButton backButton, Vector history, JLabel label) {
            this.jep = jep;
            this.backButton = backButton;
            this.history = history;
            this.label = label;
        }

        /** The action is to show the last url in the history.
        @param e the event*/
        public void actionPerformed(ActionEvent e) {
            try {
                //the current page is the last, remove it
                String curl = (String) history.lastElement();
                history.removeElement(curl);

                curl = (String) history.lastElement();
                System.out.println("Back to " + curl);
                setPage(jep, curl);
                label.setText("<html><b>URL:</b> " + curl);
                if (history.size() == 1) {
                    backButton.setEnabled(false);
                }
            } catch (Exception ex) {
                System.out.println("Exception " + ex);
            }
        }
    }

    /** An inner class that listens for hyperlinkEvent.*/
    class LinkFollower implements HyperlinkListener {

        protected JEditorPane jep;
        protected JLabel label;
        protected JButton backButton;
        protected Vector history;
        private String currentUrl;

        public LinkFollower(JEditorPane jep, JButton backButton, Vector history, JLabel label) {
            this.jep = jep;
            this.backButton = backButton;
            this.history = history;
            this.label = label;
        }
        
        public String getCurrentUrl() {
            return currentUrl;
        }

        /** The action is to show the page of the URL the user clicked on.
        @param evt the event. We only care when its type is ACTIVATED. */
        public void hyperlinkUpdate(HyperlinkEvent evt) {
            if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    currentUrl = evt.getURL().toString();
                    history.add(currentUrl);
                    backButton.setEnabled(true);
                    System.out.println("Going to " + currentUrl);
                    setPage(jep, currentUrl);
                    Object ojb = jep.getDocument().getProperty("title");
                    if (ojb != null) {
                        title = (String) ojb;
                    }
                    label.setText("<html><b>URL:</b> " + currentUrl + " <b>TITLE:</b>" + title);
                } catch (Exception e) {
                    System.out.println("ERROR: Trouble fetching url");
                } // catch
            } // if
        } // hyperlinkUpdate() method
    } // LinkFollower class (nested)

    /** The contructor runs the browser. It displays the main frame with the
    fetched initialPage
    @param initialPage the first page to show */
    public Weber(Container argContainer, String initialPage) {
        title = "N/A";

        /** A vector of String containing the past urls */
        final Vector history = new Vector();
        history.add(initialPage);

        // set up the editor pane
        jep = new JEditorPane();
        jep.setEditable(false);
        setPage(jep, initialPage);
        Object ojb = jep.getDocument().getProperty("title");
        if (ojb != null) {
            title = (String) ojb;
        }

        // set up the window
        JScrollPane scrollPane = new JScrollPane(jep);

        //Label where we show the url
        JLabel label = new JLabel("<html><b>URL:</b> " + initialPage);


        JButton backButton = new JButton("Back");
        backButton.setActionCommand("back");
        backButton.setToolTipText("Go to previous page");
        backButton.setEnabled(false);
        backButton.addActionListener(new BackButtonListener(jep, backButton, history, label));

        
        JButton refreshButton = new JButton("Reload");
        refreshButton.setActionCommand("reload");
        refreshButton.setToolTipText("Reload the page");
        refreshButton.addActionListener(new ActionListener() {
        
            @Override
            public void actionPerformed(ActionEvent e) {
                setPage(jep, history.lastElement().toString());
            }
        });

        // A toolbar to hold all our buttons
        JToolBar toolBar = new JToolBar();
        toolBar.add(backButton);
        toolBar.add(refreshButton);
        
        linkFollower = new LinkFollower(jep, backButton, history, label);
        jep.addHyperlinkListener(linkFollower);

        //Set up the toolbar and scrollbar in the contentpane of the frame
        panel = (JPanel) argContainer;
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(500, 400));
        panel.add(toolBar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(label, BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return panel;
    }

    public String getTitle() {
        return title;
    }

    /** Create a Weber object. Use the command-line url if given */
    public static void main(String[] args) {
        String initialPage = new String("http://www.google.com");

        if (args.length > 0) {
            initialPage = args[0];
        }

        JFrame f = new JFrame("Simple Web Browser");
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        //Exit the program when user closes window.
        f.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        }); // WindowListener

        Weber b = new Weber(f.getContentPane(), initialPage);
        f.pack();
        f.setSize(640, 360);
        f.setVisible(true);
    }
    
} // Weber class

// $Id$
