/**
 * $Id$
 *
 * Copyright (c) 2009-2014 Areen Design Services Ltd
 * 23 Eyot Gardens; London; W6 9TR
 * http://www.areen.com
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of Areen Design Services Ltd ("Confidential 
 * Information").  You shall not disclose such Confidential Information and shall use it only in accordance 
 * with the terms of the license agreement you entered into with Areen Design Services Ltd.
 * 
 * This file is best viewed with 110 columns.
12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 * 
 * Author(s) in chronological order:
 *   Dejan Lekic , http://dejan.lekic.org
 * Contributor(s):
 *   -
 */

package com.areen.jlib.model.key;

import java.io.Serializable;

/**
 * This is an interface for all kinds of keys (unique, candidate, simple, composite, compound)...
 * 
 * If an implementation is a SIMPLE KEY, the size will naturally be 1.
 * 
 * Originally I named it TableKey, but then I realised it may be used not just in tables, but for anything.
 * Reason is simple - key factory can make key from *anything* for whatever purpose...
 * 
 * @author dejan
 * @param <T> Type of the key attribute. If the key contains more than one attribute, Object should be used.
 * In other words, ignore the T type parameter in that case.
 */
public interface SimpleKey<T> extends Serializable {
    
    /**
     * Use this method to get the value of keys attribute with index argIndex.
     * 
     * Say we have an implementation UniqueKey interface which has 3 attributes, and object looks like:
     *    (MyKey 4215 "United Kingdom" M)
     * getAttribute(1) will return "United Kingom".
     * 
     * NOTE: Attribute indexes are zero-based.
     * 
     * @param argIndex
     * @return 
     */
    T getAttribute(int argIndex);
    
    /**
     * Useful method when key is a simple key (only one attribute). Then use this method to grab the value.
     * This is basically an alias for getAttribute(0).
     * @return 
     */
    T getAttribute();
    
    /**
     * Use this method to obtain the number of attributes this key is composed of.
     * 
     * NOTE: SimpleKey has only one attribute.
     * 
     * @return 
     * @see SimpleKey
     */
    int size();

    /**
     * Convenience method to get values of all attributes.
     * @return 
     */
    T[] getAttributes();

} // UniqueKey interface

// $Id$
