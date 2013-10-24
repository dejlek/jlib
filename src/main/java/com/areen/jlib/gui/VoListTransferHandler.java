/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.areen.jlib.gui;

import com.areen.jlib.model.SimpleObject;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 *
 * @author Dejan
 */
public class VoListTransferHandler<T extends SimpleObject> extends TransferHandler {

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY_OR_MOVE;
    }

    @Override
    public Transferable createTransferable(JComponent argComponent) {
        VoList<T> list =  (VoList<T>) argComponent;
        ArrayList<T> objects = list.getMarkedItems();
        System.out.print("num: " + objects.size() + " ");
        System.out.println(objects.get(0).getClass().getCanonicalName());
        return new TransferableSO<T>(objects);
    } // createTransferable() method
        
} // VoListTransferHandler class
