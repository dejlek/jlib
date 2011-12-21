/**
 * $Id: AreenTablePanel.java 295 2011-12-20 17:01:01Z mehjabeen $
 *
 * Copyright (c) 2009-2010 Areen Design Services Ltd
 * 23 Eyot Gardens; London; W6 9TR
 * http://www.areen.com
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of
 * Areen Design Services Ltd ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Areen Design Services Ltd.
 * 
 * This file is best viewed with 110 columns.
12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 * 
 * Author(s) in chronological order:
 *   Dejan Lekic , http://dejan.lekic.org
 * Contributor(s):
 *   -
 */

package com.areen.jlib.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * An implementation of a filtered combo-box with ability to insert new text.
 *
 * Based on Java code by Exterminator13 ( http://snippets.dzone.com/posts/show/7633 )
 *
 * @author Exterminator13
 * @author Dejan Lekic , http://dejan.lekic.org
 */
public class FilteredComboBox extends JComboBox {

    // ======================================================================================================
    //  Setting variables
    // ======================================================================================================

    private boolean addingToTopEnabled = true;
    
    // ======================================================================================================
    //  Variables
    // ======================================================================================================
    
    private static final Logger LOGGER = Logger.getLogger(FilteredComboBox.class);
    private FilteredComboBoxTestModel model;
    private final JTextComponent textComponent = (JTextComponent) getEditor().getEditorComponent();
    private boolean modelFilling = false;
    private boolean updatePopup;
    private boolean layingOut = false;
    private AutoCompleteDocument autoCompleteDocument;
    private String previousPattern = null;

    // ======================================================================================================
    //  Constructors
    // ======================================================================================================
    
    public FilteredComboBox() {
        model = new FilteredComboBoxTestModel();
        setEditable(true);

        LOGGER.debug("setPattern() called from constructor");
        setPattern(null);
        updatePopup = false;

        autoCompleteDocument = new AutoCompleteDocument();
        autoCompleteDocument.setAddingEnabled(addingToTopEnabled);
        textComponent.setDocument(autoCompleteDocument);
        setModel(model);
        setSelectedItem(null);

        new Timer(20, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (updatePopup && isDisplayable()) {
                    setPopupVisible(false);
                    if (model.getSize() > 0) {
                        setPopupVisible(true);
                    }
                    updatePopup = false;
                }
            }
        }).start();
    }

    public FilteredComboBox(String[] argComboItems) {
        model = new FilteredComboBoxTestModel(argComboItems);

        setEditable(true);

        LOGGER.debug("setPattern() called from constructor");
        setPattern(null);
        updatePopup = false;

        autoCompleteDocument = new AutoCompleteDocument();
        autoCompleteDocument.setAddingEnabled(addingToTopEnabled);
        textComponent.setDocument(autoCompleteDocument);
        setModel(model);
        setSelectedItem(null);

        new Timer(20, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (updatePopup && isDisplayable()) {
                    setPopupVisible(false);
                    if (model.getSize() > 0) {
                        setPopupVisible(true);
                    }
                    updatePopup = false;
                }
            }
        }).start();
    }

    // ======================================================================================================
    //  Superclass/interface methods
    // ======================================================================================================
    
    /**
     * {@inheritDoc }
     */
    @Override
    public void doLayout() {
        try {
            layingOut = true;
            super.doLayout();
        } finally {
            layingOut = false;
        } // finally
    } // doLayout() method

    /**
     * {@inheritDoc }
     *
     * @return Dimension object containing a much better dimension than the one from the JComboBox.
     */
    @Override
    public Dimension getSize() {
        Dimension dim = super.getSize();
        if (!layingOut) {
            dim.width = Math.max(dim.width, getPreferredSize().width);
        }
        return dim;
    } //  getSize() method



    /**
     * An internal class that deals with FilteredComboBox entries as a Document.
     */
    private class AutoCompleteDocument extends PlainDocument {

        boolean arrowKeyPressed = false;
        private boolean addingEnabled = false;

        public AutoCompleteDocument() {
            textComponent.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_ENTER) {
                        LOGGER.debug("[key listener] enter key pressed");
                        //there is no such element in the model for now
                        String text = textComponent.getText();
                        if (!model.data.contains(text)) {
                            LOGGER.debug("addToTop() called from keyPressed()");
                            if (addingEnabled) {
                                addToTop(text);
                            } // if
                        } // if
                    } else if (key == KeyEvent.VK_UP
                            || key == KeyEvent.VK_DOWN) {
                        arrowKeyPressed = true;
                        LOGGER.debug("arrow key pressed");
                    }
                }
            });
        }

        void updateModel() throws BadLocationException {
            String textToMatch = getText(0, getLength());
            LOGGER.debug("setPattern() called from updateModel()");
            setPattern(textToMatch);
        }

        @Override
        public void remove(int offs, int len) throws BadLocationException {

            if (modelFilling) {
                LOGGER.debug("[remove] model is being filled now");
                return;
            }

            super.remove(offs, len);
            if (arrowKeyPressed) {
                arrowKeyPressed = false;
                LOGGER.debug("[remove] arrow key was pressed, updateModel() was NOT called");
            } else {
                LOGGER.debug("[remove] calling updateModel()");
                updateModel();
            }
            clearSelection();
        }

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

            if (modelFilling) {
                LOGGER.debug("[insert] model is being filled now");
                return;
            }

            // insert the string into the document
            super.insertString(offs, str, a);

//            if (enterKeyPressed) {
//                logger.debug("[insertString] enter key was pressed");
//                enterKeyPressed = false;
//                return;
//            }

            String text = getText(0, getLength());
            if (arrowKeyPressed) {
                LOGGER.debug("[insert] arrow key was pressed, updateModel() was NOT called");
                model.setSelectedItem(text);
                LOGGER.debug(String.format("[insert] model.setSelectedItem(%s)", text));
                arrowKeyPressed = false;
            } else if (!text.equals(getSelectedItem())) {
                LOGGER.debug("[insert] calling updateModel()");
                updateModel();
            }

            clearSelection();
        }

        public boolean isAddingEnabled() {
            return addingEnabled;
        }

        public void setAddingEnabled(boolean argAddingEnabled) {
            addingEnabled = argAddingEnabled;
        }
    } // AutoCompleteDocument class (inner)

    public void setText(String text) {
        if (model.data.contains(text)) {
            setSelectedItem(text);
        } else {
            addToTop(text);
            setSelectedIndex(0);
        }
    }

    public String getText() {
        return getEditor().getItem().toString();
    }

    private void setPattern(String pattern) {

        if (pattern != null && pattern.trim().isEmpty()) {
            pattern = null;
        }

        if (previousPattern == null && pattern == null
                || pattern != null && pattern.equals(previousPattern)) {
            LOGGER.debug("[setPatter] pattern is the same as previous: " + previousPattern);
            return;
        }

        previousPattern = pattern;

        modelFilling = true;
//        logger.debug("setPattern(): start");

        model.setPattern(pattern);

        if (LOGGER.isDebugEnabled()) {
            StringBuilder b = new StringBuilder(100);
            b.append("pattern filter '").append(pattern == null ? "null" : pattern).append("' set:\n");
            for (int i = 0; i < model.getSize(); i++) {
                b.append(", ").append('[').append(model.getElementAt(i)).append(']');
            }
            int ind = b.indexOf(", ");
            if (ind != -1) {
                b.delete(ind, ind + 2);
            }
//            b.append('\n');
            LOGGER.debug(b);
        }
//        logger.debug("setPattern(): end");
        modelFilling = false;
        if (pattern != null) {
            updatePopup = true;
        }
    } // setPattern() method

    private void clearSelection() {
        int i = getText().length();
        textComponent.setSelectionStart(i);
        textComponent.setSelectionEnd(i);
    }

//    @Override
//    public void setSelectedItem(Object anObject) {
//        super.setSelectedItem(anObject);
//        clearSelection();
//    }
    public synchronized void addToTop(String aString) {
        model.addToTop(aString);
    }

    public boolean isAddingToTopEnabled() {
        return addingToTopEnabled;
    }

    /**
     * Use this method to disable adding of items to the top of the FilteredComboBox.
     *
     * @param addingToTopEnabled
     */
    public void setAddingToTopEnabled(boolean argAddingToTopEnabled) {
        addingToTopEnabled = argAddingToTopEnabled;
        autoCompleteDocument.setAddingEnabled(addingToTopEnabled);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {

//                Logger root = Logger.getRootLogger();
//                root.addAppender(new ConsoleAppender(new PatternLayout("%d{ISO8601} [%5p] %m at %l%n")));
                Logger root = Logger.getRootLogger();
                root.addAppender(new ConsoleAppender(new PatternLayout("%d{ISO8601} %m at %L%n")));

//                BasicConfigurator.configure();

                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new GridLayout(3, 1));
                final JLabel label = new JLabel("label ");
                frame.add(label);
                final FilteredComboBox combo = new FilteredComboBox();
//                combo.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
//
//                    @Override
//                    public void keyReleased(KeyEvent e) {
//                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                            String text = combo.getEditor().getItem().toString();
//                            if(text.isEmpty())
//                                return;
//                            combo.addToTop(text);
//                        }
//                    }
//                });
                frame.add(combo);
                JComboBox combo2 = new JComboBox(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"});
                combo2.setEditable(true);
                frame.add(combo2);
                frame.pack();
                frame.setSize(500, frame.getHeight());
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    } // main() method
    
    // ======================================================================================================
    //  Nested classes
    // ======================================================================================================

    /**
     * A basic model for the FilteredComboBox .
     */
    private class FilteredComboBoxTestModel extends AbstractListModel implements ComboBoxModel {

//        String pattern;
        String selected;
        final String delimiter = ";;;";
        final int limit = 20;

        /**
         * This nested class holds all strings that are filtered by the FilteredComboBox
         */
        class Data {

            private List<String> list = new ArrayList<String>(limit);
            private List<String> lowercase = new ArrayList<String>(limit);
            private List<String> filtered;

            void add(String s) {
                list.add(s);
                lowercase.add(s.toLowerCase());
            }

            void addToTop(String s) {
                list.add(0, s);
                lowercase.add(0, s.toLowerCase());
            }

            void remove(int index) {
                list.remove(index);
                lowercase.remove(index);
            }

            List<String> getList() {
                return list;
            }

            List<String> getFiltered() {
                if (filtered == null) {
                    filtered = list;
                }
                return filtered;
            }

            int size() {
                return list.size();
            }

            void setPattern(String pattern) {
                if (pattern == null || pattern.isEmpty()) {
                    filtered = list;
                    FilteredComboBox.this.setSelectedItem(model.getElementAt(0));
                    LOGGER.debug(String.format("[setPattern] combo.setSelectedItem(null)"));
                } else {
                    filtered = new ArrayList<String>(limit);
                    pattern = pattern.toLowerCase();
                    for (int i = 0; i < lowercase.size(); i++) {
                        //case insensitive search
                        if (lowercase.get(i).contains(pattern)) {
                            filtered.add(list.get(i));
                        }
                    }
                    FilteredComboBox.this.setSelectedItem(pattern);
                    LOGGER.debug(String.format("[setPattern] combo.setSelectedItem(%s)", pattern));
                }
                LOGGER.debug(String.format("pattern:'%s', filtered: %s", pattern, filtered));
            }

            boolean contains(String s) {
                if (s == null || s.trim().isEmpty()) {
                    return true;
                }
                s = s.toLowerCase();
                for (String item : lowercase) {
                    if (item.equals(s)) {
                        return true;
                    }
                }
                return false;
            }
        } // Data class (nested)
        
        FilteredComboBoxTestModel.Data data = new FilteredComboBoxTestModel.Data();

        public FilteredComboBoxTestModel() {
            readData();
        }

        public FilteredComboBoxTestModel(String[] argValues) {
            for (String el : argValues) {
                data.add(el);
            }
        }

        void readData() {
            String[] countries = {
                "Afghanistan",
                "Albania",
                "Algeria",
                "Andorra",
                "Angola",
                "Argentina",
                "Armenia",
                "Austria",
                "Azerbaijan",
                "Bahamas",
                "Bahrain",
                "Bangladesh",
                "Barbados",
                "Belarus",
                "Belgium",
                "Benin",
                "Bhutan",
                "Bolivia",
                "Bosnia & Herzegovina",
                "Botswana",
                "Brazil",
                "Bulgaria",
                "Burkina Faso",
                "Burma",
                "Burundi",
                "Cambodia",
                "Cameroon",
                "Canada",
                "China",
                "Colombia",
                "Comoros",
                "Congo",
                "Croatia",
                "Cuba",
                "Cyprus",
                "Czech Republic",
                "Denmark",
                "Georgia",
                "Germany",
                "Ghana",
                "Great Britain",
                "Greece",
                "Somalia",
                "Spain",
                "Sri Lanka",
                "Sudan",
                "Suriname",
                "Swaziland",
                "Sweden",
                "Switzerland",
                "Syria",
                "Uganda",
                "Ukraine",
                "United Arab Emirates",
                "United Kingdom",
                "United States",
                "Uruguay",
                "Uzbekistan",
                "Vanuatu",
                "Venezuela",
                "Vietnam",
                "Yemen",
                "Zaire",
                "Zambia",
                "Zimbabwe"};

            for (String country : countries) {
                data.add(country);
            }
        }
        boolean isThreadStarted = false;

        void writeData() {
            StringBuilder b = new StringBuilder(limit * 60);

            for (String url : data.getList()) {
                b.append(delimiter).append(url);
            }
            b.delete(0, delimiter.length());

            //waiting thread is already being run
            if (isThreadStarted) {
                return;
            }

            //we do saving in different thread
            //for optimization reasons (saving may take much time)
            new Thread(new Runnable() {

                @Override
                public void run() {
                    //we do sleep because saving operation
                    //may occur more than one per waiting period
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        LOGGER.debug(ex);
                    }
                    //we need this synchronization to
                    //synchronize with FilteredComboBox.addElement method
                    //(race condition may occur)
                    synchronized (FilteredComboBox.this) {

                        //HERE MUST BE SAVING OPERATION
                        //(SAVING INTO FILE OR SOMETHING)
                        //don't forget replace readData() method
                        //to read saved data when creating bean

                        isThreadStarted = false;
                    }
                }
            }).start();
            isThreadStarted = true;
        }

        public void setPattern(String pattern) {

            int size1 = getSize();

            data.setPattern(pattern);

            int size2 = getSize();

            if (size1 < size2) {
                fireIntervalAdded(this, size1, size2 - 1);
                fireContentsChanged(this, 0, size1 - 1);
            } else if (size1 > size2) {
                fireIntervalRemoved(this, size2, size1 - 1);
                fireContentsChanged(this, 0, size2 - 1);
            }
        }

        public void addToTop(String aString) {
            if (aString == null || data.contains(aString)) {
                return;
            }
            if (data.size() == 0) {
                data.add(aString);
            } else {
                data.addToTop(aString);
            }

            while (data.size() > limit) {
                int index = data.size() - 1;
                data.remove(index);
            }

            setPattern(null);
            model.setSelectedItem(aString);
            LOGGER.debug(String.format("[addToTop] model.setSelectedItem(%s)", aString));

            //saving into options
            if (data.size() > 0) {
                writeData();
            }
        }

        @Override
        public Object getSelectedItem() {
            return selected;
        }

        @Override
        public void setSelectedItem(Object anObject) {
            if ((selected != null && !selected.equals(anObject))
                    || selected == null && anObject != null) {
                selected = (String) anObject;
                fireContentsChanged(this, -1, -1);
            }
        }

        @Override
        public int getSize() {
            return data.getFiltered().size();
        }

        @Override
        public Object getElementAt(int index) {
            return data.getFiltered().get(index);
        }
    }
    
} // FilteredComboBox class

// $Id$
