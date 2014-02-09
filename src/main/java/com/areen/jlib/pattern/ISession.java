/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.areen.jlib.pattern;

/**
 * An interface to be implemented by objects that hold session data.
 * 
 * @author Dejan
 * @param <A> Attribute type. Should be an enum.
 */
public interface ISession<A extends ISession.VariableName> {
    
    /**
     * An interface to be implemented ISession implementation's nested enum type.
     * Typically an ISession implementation would look like:
     * <pre>
     * public final class SID implements ISession<SID.Attribute>, Serializable {
     *   public static enum Attribute implements JlibSession.Attribute {
     *     USER_ID,
     *     PASSWORD,
     *     DATABASE
     *   }
     *   Object[] values;
     * 
     *   // ... rest of the SID class ...
     *   
     * }
     * </pre>
     */
    public interface VariableName { } 
    
    Object get(final A argAttribute);
    void set(final A argAttribute, final Object argValue);

} // ISession interface

// $Id$
