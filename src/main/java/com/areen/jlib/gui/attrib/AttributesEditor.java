/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.areen.jlib.gui.attrib;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
    
    // ====================================================================================================
    // ==== Constructors ==================================================================================
    // ====================================================================================================

    public AttributesEditor() {
        super();
        editorComponent = true;
        setFocusable(true);
        setBackground(Color.white);
        
        // Let's copy the border from the TextField
        Border b = (Border) UIManager.getLookAndFeelDefaults().get("TextField.border");
        setBorder(b);

        // Let's copy the insets from the TextField
        Insets insets = (Insets) UIManager.getLookAndFeelDefaults().get("TextField.margin");
        getInsets().set(insets.top, insets.left, insets.bottom, insets.right);
        
        addKeyListener(this);
        
        // listeners...
        addFocusListener(this);
    }

    public AttributesEditor(AttributesModel argModel) {
        this();
        setOpaque(true);
        setFocusable(true);
        setBackground(Color.white);
        repaint();
        
        setModel(argModel);
    }

    // ====================================================================================================
    // ==== Interface/Superclass Methods ==================================================================
    // ====================================================================================================
    
    // :::: <Superclass> method overrides :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    // :::: PropertyChangeListener method implementations :::::::::::::::::::::::::::::::::::::::::::::::::
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String str = (String) evt.getNewValue();
        System.out.println("CALL: AttributesEditor.propertyChange() : " + str);
        updateView();
    }
    
    // :::: FocusListener method implementations ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    @Override
    public void focusGained(FocusEvent e) {
        System.out.println("CALL: AttributesEditor.focusGained()");
        int sel = model.getSelectedAttributeIndex();
        if (sel < 0) {
            model.setSelectedAttributeIndex(0);
        }
        sel = model.getSelectedAttributeIndex();
        labels[sel].setBackground(model.getSelectedBackgroundColor());
    }

    @Override
    public void focusLost(FocusEvent e) {
        System.out.println("CALL: AttributesEditor.focusLost()");
        model.setSelectedAttributeIndex(-1);
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
        
        switch (key) {
            case KeyEvent.VK_UP:
                System.out.println("up");
                model.next(model.getSelectedAttributeIndex());
                break;
            case KeyEvent.VK_DOWN:
                System.out.println("down");
                model.next(model.getSelectedAttributeIndex());
                break;
            case KeyEvent.VK_LEFT:
                System.out.println("left");
                model.previousAttribute();
                updateView();
                break;                
            case KeyEvent.VK_RIGHT:
                System.out.println("right");
                model.nextAttribute();
                updateView();
                break;
            default:
                int idx = model.getSelectedAttributeIndex();
                char chr = Character.toUpperCase(e.getKeyChar());
                if (model.isValid(idx, chr)) {
                    model.setValue(idx, chr);
                }
        } // switch
    } // keyPressed() method

    @Override
    public void keyReleased(KeyEvent e) {
        // nothing
    }

    // ====================================================================================================
    // ==== Public Methods ================================================================================
    // ====================================================================================================
    
    // ====================================================================================================
    // ==== Accessors =====================================================================================
    // ====================================================================================================
    
    // ====================================================================================================
    // ==== Private/Protected/Package Methods =============================================================
    // ====================================================================================================
    
} // AttributesEditor class
