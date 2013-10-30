/**
 * Project: JLIB
 * Version: $Id$ 
 * License: (see below block, or the LICENSE file)
 **************************************************************************************** LICENSE BEGIN *****
 * Copyright (c) 2009-2013, JLIB AUTHORS (see the AUTHORS.txt file for the list of the individuals)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided 
 * that the following conditions are met:
 * 
 *  1) Redistributions of source code must retain the above copyright notice, this list of conditions and the 
 *     following disclaimer.
 *  2) Redistributions in binary form must reproduce the above copyright notice, this list of conditions and 
 *     the following disclaimer in the documentation and/or other materials provided with the distribution.
 *  3) Neither the names of the organisations involved in the JLIB project, nor the names of their 
 *     contributors to the JLIB project may be used to endorse or promote products derived from this software 
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED 
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A 
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR 
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 ****************************************************************************************** LICENSE END *****
 * 
 * This file is best viewed with 110 columns.
12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 * 
 * In chronological order
 * Author(s):
 *   Dejan Lekic , http://dejan.lekic.org
 * Contributor(s):
 *   -
 */
package com.areen.jlib.gui;

import com.areen.jlib.model.SimpleObject;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Dejan
 */
public class TransferableSO<T extends SimpleObject> implements Transferable {
    T[] simpleObjects; /// NOTE: It might be null!
    
    List<DataFlavor> flavorList;
    DataFlavor[] flavors;
    
    private static final Class<?> RC1 = SimpleObject[].class; // Representation class
    private static final String RC1N = SimpleObject[].class.getCanonicalName(); // Human readable name
    private static final DataFlavor SIMPLE_OBJECT_ARRAY_DATA_FLAVOR = new DataFlavor(RC1, RC1N);
    
    public TransferableSO(ArrayList<T> argData) {
        flavorList = new ArrayList<DataFlavor>();
        if (!argData.isEmpty()) {
            T so = argData.get(0);

            simpleObjects = argData.toArray((T[]) so.create(argData.size()));
        } // if
        
        // Let's make list of supported flavors
        flavors = new DataFlavor[1];
        flavors[0] = SIMPLE_OBJECT_ARRAY_DATA_FLAVOR;
        
        flavorList = Arrays.asList(flavors);
    } // TransferableSO constructor

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavorList.contains(flavor);
    }

    @Override
    public synchronized Object getTransferData(DataFlavor flavor) 
            throws UnsupportedFlavorException, IOException {
        Object ret = null;
        if (isDataFlavorSupported(flavor)) {
            ret = simpleObjects;
        } // getTransferData() method
        
        return ret;
    }
    
} // TransferableSO class

// $Id$
