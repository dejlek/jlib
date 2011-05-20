
package jlib.model;

/**
 * The reason we have this class is the fact that (stupid) Java can't have static method declarations in
 * an interface. (static modifier not allowed in method declarations of an interface)
 * 
 * @author dejan
 */
public abstract class AbstractSimpleObject {
    
    /// Returns a number of fields inside underlying ValueObject
    public static byte getNumberOfFields() { return 0; }
    
} // AbstractSimpleObject class

// $Id$
