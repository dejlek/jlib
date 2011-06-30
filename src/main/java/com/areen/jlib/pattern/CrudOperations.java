/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jlib.pattern;

import java.util.ArrayList;

/**
 *
 * @author dejan
 */
public interface CrudOperations<T extends ValueObject> {
    public void setOldValue(Object argOldValue);
    // CREATE -------------------------------------------
    public T    create();
    public T    create(T argSO);
    // READ ---------------------------------------------
    public ArrayList<T> readAll();
    // UPDATE -------------------------------------------
    public T    update(T argOld);
    public T    update(T argOld, int argFieldNumber); /// byte would be better here, but Java is stupid, we would have to cast argument to byte all the time...
    // DELETE -------------------------------------------
    public boolean delete(T argSO);
} // CrudOperations<T> interface

// $Id$
