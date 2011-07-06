package com.areen.jlib.model;

import com.areen.jlib.pattern.ValueObject;

/**
 * Every class which has an underlying VO (transfer object) should implement this
 * interface. SimpleObject makes sure some VO-related methods are provided.
 * You can say that SimpleObject is a wrapper for its own transfer object (VO).
 * SimpleObject types are most often going to be used as simple models or as a
 * way to obtain/use underlying transfer object as a model...
 *
 * @author dejan
 */
public interface SimpleObject<T extends ValueObject> {
    public Object   get(int argIndex);
    public Class<?> getFieldClass(int argIndex);
    public byte     getNumberOfFields(); /// This should be static, but stupid Java does not allow it!
    public String[] getTitles();
    public T        getValue();
    public Object   set(int argIndex, Object argValue);
    public void     setTitles(String[] argTitles);
    public void     setValue(T argVo);
} // SimpleObject interface

// $Id$
