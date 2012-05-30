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
    protected byte length;  // The number of elements in the array - it is a fixed
                            // number, calculated in the constructor.

    /**
     * Class Constructor.
     * @author dejan
     * @param argNob specifies the number of bits being used for representation
     * @throws SmallArrayException  
     */
    public SmallArray(final int argNob) throws SmallArrayException {
        if (argNob > 64) {
            throw new SmallArrayException("Number of bits is too large.");
        } // if

        numberOfBits = (byte) argNob;
        length = 0;
    } // SmallArray constructor

    /**
     * Accessor method to return the number of bits being used for representation.
     * @return 
     * @author dejan
     */
    public byte getNumberOfBits() {
        return numberOfBits;
    } // getNumberOfBits() method

    /**
     * Registers/changes the number of bits used for representation.
     * @author dejan
     * @param argNob specifies the number of bits being used for representation
     * @throws SmallArrayException  
     */
    public void setNumberOfBits(final byte argNob) throws SmallArrayException {
        if (argNob > 64) {
            throw new SmallArrayException("Number of bits is too large.");
        } // if

        this.numberOfBits = argNob;
    } // setNumberOfBits() method

    /**
     * Accessor methor to return the length of the array.
     * @return 
     * @author dejan
     */
    public byte size() {
        return length;
    } // size() method

} // SmallArray abstract class

// $Id$
