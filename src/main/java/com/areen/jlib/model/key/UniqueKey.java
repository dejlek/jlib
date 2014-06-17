/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @param <T> This type parameter is useful only if the key has only one attribute or has two or more 
 * attributes of the same type. If it has two or more attributes of different types T should be ignored.
 */
public class UniqueKey<T> implements SimpleKey<T> {
    /**
     * We store attribute values in this array.
     */
    T[] attributes;

    /**
     * Default constructor.
     * 
     * PS. I accepts a single object too.
     * 
     * @param argAttributes 
     */
    public UniqueKey(T... argAttributes) {
        attributes = argAttributes;
    }
    
    @Override
    public boolean equals(final Object other) {
        boolean ret = false;
        
        if (other instanceof SimpleKey) {
            SimpleKey<T> tkey = (SimpleKey<T>) other;
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
    public T getAttribute(int argIndex) {
        return attributes[argIndex];
    }

    @Override
    public int size() {
        return attributes.length;
    }

    @Override
    public T[] getAttributes() {
        return attributes;
    }
    
    @Override
    public String toString() {
        return "(UniqueKey " + Arrays.toString(attributes) + ")";
    }

    @Override
    public T getAttribute() {
        return getAttribute(0);
    }
    
} // UniqueKey class

// $Id$
