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
import java.util.ArrayList;

/**
 *
 * @author Dejan
 */
public class TransferableSO<T extends SimpleObject> implements Transferable {
    ArrayList<T> simpleObjects;
    
    private static final Class<?> RC1 = SimpleObject[].class; // Representation class
    private static final String RC1N = SimpleObject[].class.getCanonicalName(); // Human readable name
    private static final DataFlavor SIMPLE_OBJECT_ARRAY_DATA_FLAVOR = new DataFlavor(RC1, RC1N);
    
    public TransferableSO(ArrayList<T> argData) {
        simpleObjects = argData;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] ret = new DataFlavor[1];
        ret[0] = SIMPLE_OBJECT_ARRAY_DATA_FLAVOR;
        
        return ret;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        boolean ret = false;
        
        if ((flavor.getRepresentationClass() == RC1) && (flavor.getHumanPresentableName().equals(RC1N))) {
            ret = true;
        }
                
        return ret; //flavor.equals(getTransferDataFlavors()[0]);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        Object ret = null;
        if (isDataFlavorSupported(flavor)) {
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
