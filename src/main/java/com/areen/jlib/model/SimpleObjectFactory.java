/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.model;

/**
 *
 * @author dejan
 */
public interface SimpleObjectFactory<T extends SimpleObject> {

    public T create();

} // SimpleObjectFactory interface

