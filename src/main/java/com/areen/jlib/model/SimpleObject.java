package com.areen.jlib.model;

import com.areen.jlib.pattern.ValueObject;

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
 * @author dejan
 */
public interface SimpleObject<T extends ValueObject> extends Cloneable {
    Object   get(int argIndex);
    Class<?> getFieldClass(int argIndex);
    byte     getNumberOfFields(); /// This should be static, but stupid Java does not allow it!
    String[] getTitles();
    T        getValue();
    
    Object   set(int argIndex, Object argValue);
    void     setTitles(String[] argTitles);
    void     setValue(T argVo);
    
    // ::::: factory methods :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    // ValueObject
    T[]               newArray(int argNumberOfElements);
    T                 newValue();
    
    // SimpleObject - basically, the following two are part of the Prototype design pattern
    //                (however, they do not have to clone the object though)
    SimpleObject<T>   create();
    SimpleObject<T>[] create(int argNumberOfElements);
    
    SimpleObject<T>   clone() throws CloneNotSupportedException;
} // SimpleObject interface

// $Id$
