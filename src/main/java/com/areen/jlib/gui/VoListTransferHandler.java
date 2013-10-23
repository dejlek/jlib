/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.areen.jlib.gui;

import com.areen.jlib.gui.model.VoListModel;
import com.areen.jlib.model.SimpleObject;
import java.awt.datatransfer.Transferable;
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
        VoListModel<T> model = (VoListModel<T>) list.getModel();
        return new TransferableSO<T>((T[]) model.getPickedItems());
    }
    
}
