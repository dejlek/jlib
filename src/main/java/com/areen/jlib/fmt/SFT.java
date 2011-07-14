
/**
 * This is a class from my personal Java library.
 * Author: Dejan Lekic - http://dejan.lekic.org
 */
package com.areen.jlib.fmt;

import java.util.ArrayDeque;
import java.util.LinkedList;

/**
 * NOTE: Here we talk about "TAG" but we actually mean "ELEMENT". I decided to use the term TAG because it is
 * shorter....
 * 
 * @author dejan
 */
public class SFT {
    enum State {
        START,         /// before we start scanning
        BEGIN_ELEMENT, /// we encountered a "(" so we are at the beginning of an element
        TYPE,          /// we are inside the string which should contain the name of the type
        VALUE,         /// we got the new value
        END_ELEMENT,   /// we encountered a ")"
        ERROR
    } // State enum
    
    private ArrayDeque<Object> stack; /// Stack used for parsing
    private LinkedList<Object> queue; /// Here we store all values
    private String text;
    private int position;
    private State state = State.START;
    int start = 0;
    int end = 0;
    
    // in order to use it as a stack, use: addFirst(e), removeFirst(e), peekFirst(e)
    
    public SFT() {
        stack = new ArrayDeque<Object>();
        queue = new LinkedList<Object>();
        text = "";
        position = 0;
    }
    
    public SFT(String argText) {
        this();
        text = argText;
    }
    
    public void parse() {
        int pos = lookAhead('(');
        if (pos == -1) {
            state = State.ERROR;
            return;
        } else {
            state = State.BEGIN_ELEMENT;
        } // else
        ++position;
        start = position;
        state = State.TYPE;
        end = lookAhead(' ');
        String val = text.substring(start, end);
        queue.add(val);
        
        while (end != -1) {
            ++position;
            state = State.VALUE;
            start = position;
            end = lookAhead(' ');
            if (end != -1) {
                queue.add(text.substring(start, end));
            } // if
        }
        
        if (end == -1) {
            end = lookAhead(')');
            if (end == -1) {
                state = State.ERROR;
            } else {
                //end = end - 1;
                state = State.VALUE;
                queue.add(text.substring(start, end));
                state = State.END_ELEMENT;
            } // else
        } // if
    } // parse()

    /**
     * Scan until argWhat is found.
     * @param argWhat
     * @return 
     */
    private int lookAhead(char argWhat) {
        for (int i = position; i < text.length(); i++) {
            position = i;
            if (text.charAt(position) == argWhat) {
                return position;
            } // if
        } // for
        return -1;
    } // lookAhead() method
    
    /**
     * Useful when developer needs to check the first element in the deque (typically to determine a type).
     * @return 
     */
    public Object peek() {
        return queue.peek();
    }

    public void dump() {
        for (Object obj: queue) {
            System.out.println("[" + obj + "]");
        }
    }
    
    public static void main(String[] args) {
        SFT sft = new SFT("(User LED01 Dejan Lekic 2000.22)");
        sft.parse();
        sft.dump();
    }
    
} // SFT class

// $Id$
