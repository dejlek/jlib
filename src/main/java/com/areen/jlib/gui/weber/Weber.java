/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.gui.weber;

import com.areen.jlib.gui.GuiTools;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;

/**
 * The Weber class is based on the original work by Jose M. Vidal with lots of
 * modifications by other author(s) listed below. Jose's work is licensed as public domain.
 *
 * @author Jose M. Vidal
 * @author Dejan Lekic
 */
public class Weber {

    JPanel panel;
    String title;
    String currentUrl;
    JEditorPane jep;
    LinkFollower linkFollower;

    private boolean addressBarEnabled = false;

    /** Set the page.
     * @param jep the pane on which to display the url
     * @param argUrl  
    */
    protected static void setPage(final JEditorPane jep, final String argUrl) {
        try {
            jep.setPage(argUrl);
        } catch (IOException e) {
            System.err.println(argUrl);
            System.err.println(e);
            //System.exit(-1);
        } // catch

    } // setPage() method

    /** An inner class which listens for keypresses on the Back button. */
    class BackButtonListener implements ActionListener {

        protected JEditorPane jep;
        protected JLabel label;
        protected JButton backButton;
        protected Vector history;

        public BackButtonListener(final JEditorPane argJep, final JButton argBackBtn,
                                  final Vector argHist, final JLabel argLbl) {
            this.jep = argJep;
            this.backButton = argBackBtn;
            this.history = argHist;
            this.label = argLbl;
        } // BackButtonListener() method

        /** The action is to show the last url in the history.
        @param e the event*/
        public void actionPerformed(final ActionEvent e) {
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
                } // if
            } catch (Exception ex) {
                System.out.println("Exception " + ex);
            } // catch
        }  // actionPerformed() method
    } // BackButtonListener inner class

    /** An inner class that listens for hyperlinkEvent.*/
    class LinkFollower implements HyperlinkListener {

        protected JEditorPane jep;
        protected JLabel label;
        protected JButton backButton;
        protected Vector history;
        private String currentUrl;

        public LinkFollower(final JEditorPane argJep, final JButton argBackBtn,
                            final Vector argHist, final JLabel argLbl) {
            this.jep = argJep;
            this.backButton = argBackBtn;
            this.history = argHist;
            this.label = argLbl;
        } // LinkFollower() method

        public String getCurrentUrl() {
            return currentUrl;
        } // getCurrentUrl() method

        /** The action is to show the page of the URL the user clicked on.
        @param evt the event. We only care when its type is ACTIVATED. */
        public void hyperlinkUpdate(final HyperlinkEvent evt) {
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
                    } // if
                    label.setText("<html><b>URL:</b> " + currentUrl + " <b>TITLE:</b>" + title);
                } catch (Exception e) {
                    System.out.println("ERROR: Trouble fetching url");
                } // catch
            } // if
        } // hyperlinkUpdate() method
    } // LinkFollower class (nested)

    /** The contructor runs the browser. It displays the main frame with the
    fetched initialPage
    * @param argContainer 
    * @param initialPage the first page to show */
    public Weber(final Container argContainer, final String initialPage) {
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
        } // if

        // set up the window
        JScrollPane scrollPane = new JScrollPane(jep);

        //Label where we show the url
        JLabel label = new JLabel("<html><b>URL:</b> " + initialPage);


//        JButton backButton = new JButton("Back");
        JButton backButton = new JButton(new javax.swing.ImageIcon(GuiTools.getResource(getClass(),
                "/icons/global_back.png")));
        backButton.setActionCommand("back");
        backButton.setToolTipText("Go to previous page");
//        backButton.setIcon(new javax.swing.ImageIcon(GuiTools.getResource(getClass(),
//                "/icons/global_back.png")));
        backButton.setEnabled(false);
        backButton.addActionListener(new BackButtonListener(jep, backButton, history, label));


//        JButton refreshButton = new JButton("Reload");
        JButton refreshButton = new JButton(new javax.swing.ImageIcon(GuiTools.getResource(getClass(),
                "/icons/global_refresh.png")));
        refreshButton.setActionCommand("reload");
        refreshButton.setToolTipText("Reload the page");
//        refreshButton.setIcon(new javax.swing.ImageIcon(GuiTools.getResource(getClass(),
//                "/icons/global_refresh.png")));
        refreshButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                Document doc = jep.getDocument();
                doc.putProperty(Document.StreamDescriptionProperty, null);
                setPage(jep, history.lastElement().toString());
            } // actionPerformed() method
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
        if (!addressBarEnabled) {
            label.setVisible(false);
        } // if

    } // Weber constructor

    /**
     * 
     * @return
     */
    public JPanel getPanel() {
        return panel;
    } // getPanel() method

    /**
     * 
     * @return
     */
    public String getTitle() {
        return title;
    } // getTitle() method

    /** Create a Weber object. Use the command-line url if given
     * @param args 
     */
    public static void main(final String[] args) {
        String initialPage = new String("https://www.areen-online.co.uk");

        if (args.length > 0) {
            initialPage = args[0];
        } // if

        JFrame f = new JFrame("Simple Web Browser");
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        //Exit the program when user closes window.
        f.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(final WindowEvent e) {
                System.exit(0);
            } // windowClosing() method
        }); // WindowListener

        Weber b = new Weber(f.getContentPane(), initialPage);
        f.pack();
        f.setSize(640, 360);
        f.setVisible(true);
    } // main() method

} // Weber class

// $Id$
