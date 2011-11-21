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

    public SmallArrayInt(int argNob) throws SmallArrayException {
        super(argNob);
        length = (byte) (32 / argNob);
    }

    public void set(int argIdx, int argValue) throws SmallArrayException, ArrayIndexOutOfBoundsException {
        int maxVal = (int) Math.pow(2.0, numberOfBits);
        if (length < argIdx) {
            throw new ArrayIndexOutOfBoundsException(argIdx);
        }
        if (argValue >= maxVal) {
            throw new SmallArrayException("Value is greater than the maximum allowed for an element. Got "
                    + argValue + ", but can handle values up to " + maxVal);
        }
        data = (int) Math.pow(2.0, 32) - 1;
        for (int j = 0; j < 4; j++) {
            for (int i = 7; i >= 0; i--) {
                System.out.print(i);
            }
        }

        System.out.println();
        System.out.println(wlz(6));
        System.out.println(wlz(data));
        data = 0x71fff5f0;
        System.out.println(wlz(data));
        System.out.println(wlz(data >> (argIdx * numberOfBits)));
        System.out.println(wlz(data | ((int) Math.pow(2.0, argIdx * numberOfBits)) - 1));
    } // set() method

    public static void main(String[] args) throws SmallArrayException {
        SmallArrayInt sai = new SmallArrayInt(3);
        sai.set(3, 5);
    }

    public String wlz(int argValue) {
        String binString = Integer.toBinaryString(argValue);
        return "00000000000000000000000000000000".substring(binString.length())
                + binString + " : " + argValue;
    }

} // SmallArrayInt class

// $Id$
