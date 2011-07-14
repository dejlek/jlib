/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.pattern;

/**
 *
 * @author dejan
 */
public interface CrudOperations<T extends ValueObject> {
    public void setOldValue(Object argOldValue);
    public void setArgs(String argInput); /// Use this method to specify parameters to the CRUD object.
    // CREATE -------------------------------------------
    public T       create();
    public T       create(T argVo);
    public T[]     create(T[] argVos); /// Used for duplicating rows
    // READ ---------------------------------------------
    public T[]     readAll();
    // UPDATE -------------------------------------------
    public T       update(T argOld);
    public T       update(T argOld, int argFieldNumber); /// byte would be better here, but Java is stupid, we would have to cast argument to byte all the time...
    // DELETE -------------------------------------------
    public boolean delete(T argVo);
    public boolean delete(T[] argVos); /// Used to delete a group of rows
} // CrudOperations<T> interface

// $Id$
