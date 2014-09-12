/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.ds;

import com.areen.jlib.exceptions.SmallArrayException;

/**
 *
 * @author dejan
 */
public class SmallArrayInt extends SmallArray {
    int data;

    /**
     * 
     * @param argNob
     * @throws SmallArrayException
     */
    public SmallArrayInt(final int argNob) throws SmallArrayException {
        super(argNob);
        length = (byte) (32 / argNob);
    } // SmallArrayInt() method

    /**
     * 
     * @param argIdx
     * @param argValue
     * @throws SmallArrayException
     * @throws ArrayIndexOutOfBoundsException
     */
    public void set(final int argIdx, final int argValue)
            throws SmallArrayException {
        int maxVal = (int) Math.pow(2.0, numberOfBits);
        if (length < argIdx) {
            throw new ArrayIndexOutOfBoundsException(argIdx);
        } // if
        if (argValue >= maxVal) {
            throw new SmallArrayException("Value is greater than the maximum allowed for an element. Got "
                    + argValue + ", but can handle values up to " + maxVal);
        } // if
        
        data = (int) Math.pow(2.0, 32) - 1;

        System.out.println();
        System.out.println(wlz(6));
        System.out.println(wlz(data));
        data = 0x71fff5f0;
        System.out.println(wlz(data));
        System.out.println(wlz(data >> (argIdx * numberOfBits)));
    } // set() method

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            SmallArrayInt sai = new SmallArrayInt(3);
            sai.set(3, 5);
        } catch (SmallArrayException sae) {
            // TODO: implement proper handler
        } // catch
    } // main() method

    /**
     * 
     * @param argValue
     * @return
     */
    public String wlz(final int argValue) {
        String binString = Integer.toBinaryString(argValue);
        return "00000000000000000000000000000000".substring(binString.length())
                + binString + " : " + argValue;
    } // wlz() method

} // SmallArrayInt class

// $Id$
