/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.areen.jlib.pattern;

/**
 * An interface for ISession and its implementations.
 * @author Dejan
 */
public interface JlibSession {
    
    /**
     * An interface to be implemented by SID's nested enum type.
     * Typically A SID implementation (Session ID) would look like:
     * <pre>
     * public final class SID implements ISession<SID.Attribute>, Serializable {
     *   public static final enum Attribute implements JlibSession.Attribute {
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
    interface Attribute { }
    
}
