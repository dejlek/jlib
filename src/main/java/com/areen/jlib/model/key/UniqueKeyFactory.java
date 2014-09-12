/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.areen.jlib.model.key;

import com.areen.jlib.model.SimpleObject;

/**
 * This interface exists for convenience when we know the UniqueKey is going to be composed of two or more
 * attributes.
 * 
 * @author dejan
 * @param <U>
 * @param <S>
 */
public interface UniqueKeyFactory<U extends UniqueKey<Object>, S extends SimpleObject>  
        extends SimpleUniqueKeyFactory<Object, U, S> {
    
} // UniqueKeyFactoryUS interface
