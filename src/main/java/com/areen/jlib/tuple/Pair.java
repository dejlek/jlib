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

package com.areen.jlib.tuple;

import java.io.Serializable;
import java.util.Map;

/**
 * This class is slightly modified version of the Pair class from this thread:
 * http://stackoverflow.com/questions/156275/what-is-the-equivalent-of-the-c-pairl-r-in-java
 * As suggested in the thread above, I have made first & second to be final members.
 * I have made it into an Map.Entry<K,V> type, so it is suitable to be an element
 * of any Java Hash map...
 *
 * @author Dejan Lekic - http://dejan.lekic.org
 * @param <KeyT> A type of the first element in the Pair.
 * @param <ValueT> A type of the second element in the Pair.
 */
public class Pair<KeyT, ValueT> implements Map.Entry<KeyT, ValueT>, Serializable {

    protected KeyT first;
    protected ValueT second;

    public Pair(final KeyT argFirst, final ValueT argSecond) {
        super();
        this.first = argFirst;
        this.second = argSecond;
    }
    
    @Override
    public int hashCode() {
        int result = 17; // or any other prime number
        result = 31 * result + first.hashCode();
        result = 31 * result + second.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof Pair) {
            Pair otherPair = (Pair) other;
            return ((this.first == otherPair.first
                    || (this.first != null && otherPair.first != null
                    && this.first.equals(otherPair.first)))
                    && (this.second == otherPair.second
                    || (this.second != null && otherPair.second != null
                    && this.second.equals(otherPair.second))));
        } // if
        return false;
    } // equals() method

    @Override
    public String toString() {
        // previously we used " - " as a separator. Now we will use the 0x1f character, called the UNIT
        // SEPARATOR to separate two fields in a String object. See the `Sise` class for more information.
        return first + "\u001f" + second;
    }

    public KeyT getFirst() {
        return first;
    }

    public void setFirst(final KeyT argFirst) {
        this.first = argFirst;
    }

    public ValueT getSecond() {
        return second;
    }

    public void setSecond(final ValueT argSecond) {
        this.second = argSecond;
    }

    @Override
    public ValueT setValue(final ValueT argNewValue) {
        ValueT oldValue = second;
        second = argNewValue;
        return oldValue;
    }

    @Override
    public ValueT getValue() {
        return second;
    }

    @Override
    public KeyT getKey() {
        return first;
    }
} // Pair class

// $Id$
