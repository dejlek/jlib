/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.areen.jlib.gui;

import com.areen.jlib.model.SimpleObject;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 *
 * @author Dejan
 */
public class TransferableSO<T extends SimpleObject> implements Transferable {
    T[] simpleObjects;
    
    public TransferableSO(T[] argData) {
        simpleObjects = argData;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] ret = new DataFlavor[1];
        ret[0] = new DataFlavor(SimpleObject[].class, SimpleObject[].class.getCanonicalName());
        return ret;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(getTransferDataFlavors()[0]);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        Object ret = null;
        if (flavor.equals(getTransferDataFlavors()[0])) {
            ret = simpleObjects;
        }
        return ret;
    }
    
    /*
    If we want to implement move , we need this:
    void exportDone(JComponent c, Transferable t, int action) {
        if (action == MOVE) {
            c.removeSelection();
        }
    }
    */

}
