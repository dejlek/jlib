/**
 * Project: JLIB
 * Version: $Id$ 
 * License: (see below block, or the accompanied COPYING.txt file)
 **************************************************************************************** LICENSE BEGIN *****
 * Copyright (c) 2009-2014, JLIB AUTHORS (see the AUTHORS.txt file for the list of the individuals)
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
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * POSSIBILITY OF SUCH DAMAGE.
 ****************************************************************************************** LICENSE END *****
 * 
 * This file is best viewed with 110 columns.
 * 45678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 * 
 * In chronological order
 * Author(s):
 *   Dejan Lekic , http://dejan.lekic.org
 * Contributor(s):
 *   -
 */

package com.areen.jlib.model.key;

import java.util.Arrays;

/**
 * According to the Wikipedia ( http://en.wikipedia.org/wiki/Unique_key ), and personal logic, unique key
 * should allow NULL value(s).
 * 
 * If it has only one attribute, only one record may have NULL value, and other records must have UniqueKey
 * that is NOT null.
 * 
 * Naturally, if UniqueKey is composed of two or more attributes then there are few cases with NULL 
 * attributes:
 * 
 * 1) There may be only ONE UniqueKey with ALL attributes equal to NULL.
 * 2) All permutations are allowed.
 * 
 * NOTE2: UniqueKey implementation could, and will be used as key for Maps, therefore we list equals() and 
 * hashCode() methods here, so every implementation MUST implement these methods.
 * 
 * @author dejan
 * @param <KeyT> This type parameter is useful only if the key has only one attribute or has two or more 
 * attributes of the same type. If it has two or more attributes of different types T should be ignored.
 */
public class UniqueKey<KeyT> implements SimpleKey<KeyT> {
    /**
     * We store attribute values in this array.
     */
    KeyT[] attributes;

    /**
     * Default constructor.
     * 
     * PS. I accepts a single object too.
     * 
     * @param argAttributes 
     */
    public UniqueKey(KeyT... argAttributes) {
        attributes = argAttributes;
    }
    
    @Override
    public boolean equals(final Object other) {
        boolean ret = false;
        
        if (other instanceof SimpleKey) {
            SimpleKey<KeyT> tkey = (SimpleKey<KeyT>) other;
            return Arrays.equals(attributes, tkey.getAttributes());
        }
        
        return ret;
    } // equals() method
    
    @Override
    public int hashCode() {
        int result = 17; // or any other prime number
        for (Object obj : attributes) {
            result = 37 * result + (obj == null ? 0 : obj.hashCode());
        }
        return result;
    } // hashCode() method

    @Override
    public KeyT getAttribute(int argIndex) {
        return attributes[argIndex];
    }

    @Override
    public int size() {
        return attributes.length;
    }

    @Override
    public KeyT[] getAttributes() {
        return attributes;
    }
    
    @Override
    public String toString() {
        return "(UniqueKey " + Arrays.toString(attributes) + ")";
    }

    @Override
    public KeyT getAttribute() {
        return getAttribute(0);
    }
    
} // UniqueKey class

// $Id$
