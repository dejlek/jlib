/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.ds;

import com.areen.jlib.exceptions.SmallArrayException;

/**
 * This is the base class for all implementations of SmallArray. The idea behind SmallArray classes is that
 * we can treat int and long values as array of small values (0 - 2^numOfBits)
 * @author dejan
 */
public abstract class SmallArray {

    protected byte numberOfBits;
    protected byte length; /// The number of elements in the array - it is a fixed number, calculated in the constructor.
    
    public SmallArray(int argNob) throws SmallArrayException {
        if (argNob > 64)
            throw new SmallArrayException("Number of bits is too large.");
        
        numberOfBits = (byte) argNob;
        length = 0;
    } // SmallArray constructor

    public byte getNumberOfBits() {
        return numberOfBits;
    }

    public void setNumberOfBits(byte argNob) throws SmallArrayException {
        if (argNob > 64)
            throw new SmallArrayException("Number of bits is too large.");
        
        this.numberOfBits = argNob;
    } // setNumberOfBits() method

    public byte size() {
        return length;
    }

} // SmallArray abstract class

// $Id$
