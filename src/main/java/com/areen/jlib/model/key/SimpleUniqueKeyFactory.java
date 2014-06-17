/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.areen.jlib.model.key;

import com.areen.jlib.model.key.UniqueKey;
import com.areen.jlib.model.SimpleObject;
import com.areen.jlib.pattern.ValueObject;

/**
 * This factory is useful when developer knows the key will either be simple (only one attribute) or have
 * attributes of the same type T.
 * 
 * @author dejan
 * @param <T> Type of the keys attribute(s).
 * @param <K> An object of UniqueKey<T> type that our get() methods will create for us.
 * @param <S> An object of SimpleObject type. Our get() methods will extract keys of type K from objects of
 * S type.
 */
public interface SimpleUniqueKeyFactory<T, K extends UniqueKey<T>, S extends SimpleObject> {
    K create(S argSO);
    K create(ValueObject argTO);
} // UniqueKeyFactory interface
