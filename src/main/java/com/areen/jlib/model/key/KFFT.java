/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.areen.jlib.model.key;

import com.areen.jlib.model.SimpleObject;
import com.areen.jlib.pattern.ValueObject;

/**
 * A very convenient class that will be used to create SimpleUniqueKeyFactory objects for us.
 * NOTE: If you want to use this class to generate key-factory for use with ValueObjects, you must provide
 * a prototype object by calling setPrototype() method.
 * 
 * @author dejan
 * @param <T>
 * @param <S>
 */
public final class KFFT<T, S extends SimpleObject> 
        implements SimpleUniqueKeyFactory<T, UniqueKey<T>, S> {
    
    private ValueObject.Field[] fields = null;
    private int[] fieldIndexes = null;
    private boolean byFields = false;
    private int size = 0;
    private S prototype = null;
    
    public KFFT(ValueObject.Field[] argFields) {
        fields = argFields;
        size = argFields.length;
        byFields = true;
    }
    
    public KFFT(int[] argFieldIndexes) {
        fieldIndexes = argFieldIndexes;
        size = argFieldIndexes.length;
        byFields = false;
    }
    
    /**
     * Sets the prototype - object that is used to manipulate S.VO objects.
     * 
     * @param argPrototype 
     */
    public void setPrototype(S argPrototype) {
        prototype = argPrototype;
    }

    @Override
    public UniqueKey<T> create(S argSO) {
        T[] attribs = (T[]) new Object[size];
        for (int i = 0; i < size; i++) {
            attribs[i] = get(i, argSO);
        }
        return new UniqueKey<T>(attribs);
    }

    @Override
    public UniqueKey<T> create(ValueObject argTO) {
        T[] attribs = (T[]) new Object[size];
        
        if (prototype == null) {
            // If we do not have a prototype, KFFT will always return an UniqueKey object filled with
            // null attributes.
            for (int i = 0; i < size; i++) {
                attribs[i] = null;
            }
        } else {
            // If we do have a prototype, we will use it to extract fields.
            for (int i = 0; i < size; i++) {
                S so = (S) prototype.create();
                so.setValue(argTO);
                attribs[i] = get(i, so);
            }
        } // else
        
        return new UniqueKey<T>(attribs);
    }
    
    private T get(int argIndex, S argSO) {
        if (byFields) {
            return (T) argSO.get(fieldIndexes[argIndex]);
        } else {
            return (T) argSO.get(fields[argIndex]);
        }
    } // get() method
    
} // KFFT class

// $Id$
