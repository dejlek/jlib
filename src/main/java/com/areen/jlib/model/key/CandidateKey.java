/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.areen.jlib.fmodelkey;

import com.areen.jlib.model.key.SimpleKey;

/**
 * An implementation of this interface should be the same as the PrimaryKey - it should not allow null 
 * attributes, and that is all it does. It simply just checks for nulls, that is the only extra 
 * functionality.
 * 
 * PrimaryKey should extend this interface, or implement it, we will see.
 * 
 * @author dejan
 */
public interface CandidateKey<T> extends SimpleKey<T> {
    
}
