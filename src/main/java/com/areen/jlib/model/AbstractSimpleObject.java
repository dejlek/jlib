
package com.areen.jlib.model;

/**
 * The reason we have this class is the fact that (stupid) Java can't have static method declarations in
 * an interface. (static modifier not allowed in method declarations of an interface)
 * 
 * @author dejan
 */
public abstract class AbstractSimpleObject {
    /// Abstract constructor - subclasses should override it
    public AbstractSimpleObject() { }
    
    public Object get(int argIndex) { return null; }
    public Object set(int argIndex, Object argValue) { return null; }
    
} // AbstractSimpleObject class

// $Id$
