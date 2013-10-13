/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.areen.jlib.gui;

import com.areen.jlib.gui.model.VoListModel;
import com.areen.jlib.gui.renderer.VoListCellRenderer;
import com.areen.jlib.model.SimpleObject;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JList;

/**
 * JList that is aware of SimpleObjects and its VOs.
 * 
 * @author Dejan
 * 
 * @param <S> SimpleObject implementation we deal with.
 */
public class VoList<S extends SimpleObject> 
        extends JList
        implements KeyListener, MouseListener {
    
    VoListModel<S> model;
    VoListCellRenderer<S> listCellRenderer;
    
    public VoList(VoListModel argModel) {
        super(argModel);
        //setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        model = argModel;
        
        listCellRenderer = new VoListCellRenderer<S>("<b>$0</b><br/>$1, $2, $3, $4, $5, $6, $7, $8, $9");
        setCellRenderer(listCellRenderer);
        
        addKeyListener(this);
        addMouseListener(this);
    } // VoList constructor
    
    // ====================================================================================================
    // ==== Interface/Superclass Methods ==================================================================
    // ====================================================================================================
    
    // ::::: KeyListener :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (model.isEmpty()) {
            return;
        }
        
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            togglePickedItem(getSelectedIndex());
        }
    }
    
    public void setPickedItem(int argIndex) {
        model.setPickedItem(argIndex);
    }
    
    public void togglePickedItem(int argIndex) {
        model.togglePickedItem(argIndex);
    }
    
    public boolean isPickedItem(int argIndex) {
        return model.isPicked(argIndex);
    }

    // ::::: KeyListener :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    /**
     * {@inheritDoc }
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (model.isEmpty()) {
            return;
        }
        
        if ((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) > 0) {
            int index = locationToIndex(e.getPoint());
            setSelectedIndex(index);
            togglePickedItem(index);
        } // if
    } // mouseClicked() method

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
    // ::::: JList :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    @Override
    public VoListCellRenderer<S> getCellRenderer() {
        return listCellRenderer;
    }
    
} // VoList class

// $Id$
