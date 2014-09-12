/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.areen.jlib.gui.attrib;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * 
 * @author dejan
 */
public class AttributesEditor 
        extends AttributesEditableView
        implements PropertyChangeListener, FocusListener, KeyListener {
    /* Class implementation comment (if any)
     * 
     */
    
    // ====================================================================================================
    // ==== Variables =====================================================================================
    // ====================================================================================================
    
    // :::::: CONFIGURATION VARIABLES :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    // :::::: PUBLIC ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    
    // :::::: PRIVATE/PROTECTED :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    ArrayList<ActionListener> actionListeners;
    Action keyboardAction;
    
    // ====================================================================================================
    // ==== Constructors ==================================================================================
    // ====================================================================================================

    public AttributesEditor(boolean argInTable) {
        super();
        setInTable(argInTable);
        actionListeners = new ArrayList<ActionListener>();
        
        editorComponent = true;
        setFocusable(true);
        setBackground(Color.white);
        
        // Let's copy the border from the TextField
        Border b = (Border) UIManager.getLookAndFeelDefaults().get("TextField.border");
        setBorder(b);

        // Let's copy the insets from the TextField
        Insets insets = (Insets) UIManager.getLookAndFeelDefaults().get("TextField.margin");
        getInsets().set(insets.top, insets.left, insets.bottom, insets.right);
        
        // We need to use key listener for "normal" keys (like "Y" or "N")...
        addKeyListener(this);
        
        initKeyboardBindings();
        
        // listeners...
        addFocusListener(this);
    }

    public AttributesEditor(AttributesModel argModel, boolean argInTable) {
        this(argInTable);
        setOpaque(true);
        setFocusable(true);
        setBackground(Color.white);
        repaint();
        
        setModel(argModel);
    }
    
    public AttributesEditor(AttributesModel argModel) {
        this(argModel, false);
    }

    // ====================================================================================================
    // ==== Interface/Superclass Methods ==================================================================
    // ====================================================================================================
    
    // :::: <Superclass> method overrides :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    // :::: PropertyChangeListener method implementations :::::::::::::::::::::::::::::::::::::::::::::::::
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("attributes")) {
            String str = (String) evt.getNewValue();
            updateView();
        }
    }
    
    // :::: FocusListener method implementations ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    @Override
    public void focusGained(FocusEvent e) {
        int sel = model.getSelectedAttributeIndex();
        if (sel < 0) {
            model.setSelectedAttributeIndex(0);
        }
        sel = model.getSelectedAttributeIndex();
        labels[sel].setBackground(model.getSelectedBackgroundColor());
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (!isInTable()) {
            model.setSelectedAttributeIndex(-1);
        }
        updateView();
    }
    
    // :::: KeyListener method implementations ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    @Override
    public void keyTyped(KeyEvent e) {
        // Weird thing is, arrow keys do not trigger keyTyped() to be called, 
        // so we have to use keyPressed here.
    }

    /**
     * Weird thing is, arrow keys do not trigger keyTyped() to be called, so we have to use keyPressed here.
     * @param e 
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        int idx = model.getSelectedAttributeIndex();
        char chr = Character.toUpperCase(e.getKeyChar());
        if (model.isValid(idx, chr)) {
            model.setValue(idx, chr);
        }
    } // keyPressed() method

    @Override
    public void keyReleased(KeyEvent e) {
        // nothing
    }

    // ====================================================================================================
    // ==== Public Methods ================================================================================
    // ====================================================================================================
    
    public synchronized void addActionListener(ActionListener argActionListener) {
        actionListeners.add(argActionListener);
    }
    
    public synchronized void removeActionListener(ActionListener argActionListener) {
        actionListeners.remove(argActionListener);
    }
    
    // ====================================================================================================
    // ==== Accessors =====================================================================================
    // ====================================================================================================
    
    // ====================================================================================================
    // ==== Private/Protected/Package Methods =============================================================
    // ====================================================================================================

    private void handleLeft() {
        model.previousAttribute();
        updateView();
    }
    
    private void handleRight() {
        model.nextAttribute();
        updateView();
    }
    
    private void handleUp() {
        model.previous(model.getSelectedAttributeIndex());
    }
    
    private void handleDown() {
        model.next(model.getSelectedAttributeIndex());
    }
    
    private void handleSpace() {
        handleDown();
    }
    
    private void handleEnter() {
        ActionEvent av = new ActionEvent(this, 0, "commit_changes");
        for (ActionListener al : actionListeners) {
            al.actionPerformed(av);
        }
    }
    
    private void handleHome() {
        model.setSelectedAttributeIndex(0);
        updateView();
    }
    
    private void handleEnd() {
        model.setSelectedAttributeIndex(model.getNumberOfAttributes() - 1);
        updateView();
    }
    
    private void initKeyboardBindings() {
        InputMap im = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap am = getActionMap();
        
        Action tmpAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                handleLeft();
            }
            
        };
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "attributes-left");
        am.put("attributes-left", tmpAction);
        
        tmpAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                handleRight();
            }
            
        };
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "attributes-right");
        am.put("attributes-right", tmpAction);
        
        if (!isInTable()) {
            /* If the editor is used as a cell editor, we do not want to togle values with the
             * up and down arrow-keys.
             */
            
            tmpAction = new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    handleUp();
                }

            };
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "attributes-up");
            am.put("attributes-up", tmpAction);

            tmpAction = new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    handleDown();
                }

            };
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "attributes-down");
            am.put("attributes-down", tmpAction);
        }
        
        tmpAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                handleSpace();
            }
            
        };
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "attributes-space");
        am.put("attributes-space", tmpAction);
        
        tmpAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                handleEnter();
            }
            
        };
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "attributes-enter");
        am.put("attributes-enter", tmpAction);
        
        tmpAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                handleHome();
            }
            
        };
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), "attributes-home");
        am.put("attributes-home", tmpAction);
        
        tmpAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                handleEnd();
            }
            
        };
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), "attributes-end");
        am.put("attributes-end", tmpAction);
    }
    
} // AttributesEditor class
