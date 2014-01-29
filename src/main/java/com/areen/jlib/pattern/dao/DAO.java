/**
 * Project: JLIB
 * Version: $Id$ 
 * License: (see below block, or the accompanied COPYING.txt file)
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
package com.areen.jlib.pattern.dao;

import com.areen.jlib.pattern.ValueObject;
import java.io.Serializable;

/**
 * An implementation of a simple, generic DAO pattern.
 * 
 * TODO: do a research to exploit possibility to have PK inside the ValueObject itself or at least something
 * similar to what we did with the Fields interface.
 * 
 * @author Dejan
 */
public interface DAO<PK extends Serializable, T extends ValueObject> {
    /**
     * Persist the argNewInstance object into the persistent storage.
     * 
     * @param argNewInstance
     * @return 
     */
    PK create(T argNewInstance);
    
    /**
     * Get an object of type T by its key argKey.
     * 
     * @param argKey
     * @return 
     */
    T read(PK argKey);
    
    /**
     * Save changes to the persistent object.
     * 
     * @param argTransientObject 
     */
    void update(T argTransientObject);
    
    /**
     * Remove an object from persistent storage.
     * 
     * @param argPersistentObject 
     */
    void delete(T argPersistentObject);
    
} // DAO interface

// $Id$
