package com.areen.jlib.tuple;

import java.util.Map;

/**
 * This class is slightly modified version of the Pair class from this thread:
 * http://stackoverflow.com/questions/156275/what-is-the-equivalent-of-the-c-pairl-r-in-java
 * As suggested in the thread above, I have made first & second to be final members.
 * I have made it into an Map.Entry<K,V> type, so it is suitable to be an element
 * of any Java Hash map...
 * 
 * @author Dejan Lekic - http://dejan.lekic.org
 */
public class Pair<KeyT, ValueT> implements Map.Entry<KeyT, ValueT> {

    protected KeyT first;
    protected ValueT second;

    public Pair(KeyT first, ValueT second) {
        super();
        this.first = first;
        this.second = second;
    }

    @Override
    public int hashCode() {
        int hashFirst = first != null ? first.hashCode() : 0;
        int hashSecond = second != null ? second.hashCode() : 0;

        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Pair) {
            Pair otherPair = (Pair) other;
            return ((this.first == otherPair.first
                    || (this.first != null && otherPair.first != null
                    && this.first.equals(otherPair.first)))
                    && (this.second == otherPair.second
                    || (this.second != null && otherPair.second != null
                    && this.second.equals(otherPair.second))));
        } // if
        return false;
    } // equals() method

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    public KeyT getFirst() {
        return first;
    }

    public void setFirst(KeyT first) {
        this.first = first;
    }

    public ValueT getSecond() {
        return second;
    }

    public void setSecond(ValueT second) {
        this.second = second;
    }

    @Override
    public ValueT setValue(ValueT argNewValue) {
        ValueT oldValue = second;
        second = argNewValue;
        return oldValue;
    }

    @Override
    public ValueT getValue() {
        return second;
    }

    @Override
    public KeyT getKey() {
        return first;
    }
} // Pair class

// $Id$
