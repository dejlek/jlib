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
package com.areen.jlib.model;

import com.areen.jlib.model.key.UniqueKey;
import com.areen.jlib.pattern.ValueObject;
import java.io.Serializable;

/**
 * Every class which has an underlying VO (transfer object) should implement this
 * interface. SimpleObject makes sure some VO-related methods are provided.
 * You can say that SimpleObject is a wrapper for its own transfer object (VO).
 * SimpleObject types are most often going to be used as simple models or as a
 * way to obtain/use underlying transfer object as a model...
 * 
 * What this basically means is - a SimpleObject implementation is a WRAPPER of the underlying ValueObject.
 * It is also a FACTORY of the ValueObject.
 * 
 * SimpleObject is also a factory. It creates ValueObjects of type T, and also UniqueKeys as well.
 * 
 * (PS. we may change the name ValueObject to something else. Definitely not TransferObject. Perhaps
 * DataObject or just Struct...)
 *
 * @author dejan
 * @param <T> A ValueObject implementation this SimpleObject interface deals with.
 */
public interface SimpleObject<T extends ValueObject> extends Cloneable, Serializable {
    
    /// This nested interface is here so we do not have to include ValueObject in type lists.
    interface TO extends ValueObject { };
    
    Object            get(int argIndex);
    Object            get(final ValueObject.Field argField); //reserved for the future
    Class<?>          getFieldClass(int argIndex);
    int               getNumberOfFields(); /// This should be static, but stupid Java does not allow it!
    String[]          getTitles();
    T                 getValue();
    
    Object            set(int argIndex, Object argValue);
    Object            set(ValueObject.Field argField, Object argValue); //reserved for the future
    void              setTitles(String[] argTitles);
    void              setValue(T argVo);
    
    // ::::: factory methods :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    // ValueObject
    T[]               newArray(int argNumberOfElements);
    T                 newValue();
    
    // SimpleObject - basically, the following two are part of the Prototype design pattern
    //                (however, they do not necessarily have to clone the object)
    SimpleObject<T>   create();
    SimpleObject<T>[] create(int argNumberOfElements);
    
    SimpleObject<T>   clone() throws CloneNotSupportedException;
    
    // ::::: key generator methods :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    /**
     * Use this method to get the (default) unique key of the given ValueObject (TransferObject).
     * 
     * @param argValueObject A Value/Transfer object from which we want to generate a UniqueKey.
     * @return UniqueKey object which can be used to uniquely identify the SimpleObject.
     */
    UniqueKey         createKey(T argValueObject);
    
    /** createKey(SO) is not needed. If we have SO, we use its createKey() method instead.
    UniqueKey         createKey(SimpleObject<T> argSimpleObject);
     
     * @param argValueObject **/
    
    /**
     * Use this method to get the (default) unique key of the SimpleObject.
     * This method is provided for convenience. Behind the scene implementation will most likely call
     * return createKey(getValue());
     * 
     * Once we use Java 8 we may make this method to be the default method, with trivial implementation:
     * <pre>default UniqueKey createKey() { return createKey(getValue()); };</pre>
     * 
     * @return UniqueKey object which can be used to uniquely identify the SimpleObject.
     */
    UniqueKey         createKey();
    
} // SimpleObject interface

// $Id$
