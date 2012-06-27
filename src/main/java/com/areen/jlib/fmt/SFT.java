
/**
 * This is a class from my personal Java library.
 * Author: Dejan Lekic - http://dejan.lekic.org
 */
package com.areen.jlib.fmt;

//PMDUnusedImports: import java.util.ArrayDeque;
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

    //PMDUnusedPrivateField: private ArrayDeque<Object> stack; /// Stack used for parsing
    private LinkedList<Object> queue; /// Here we store all values
    private String text;
    private int position;
    //PMDUnusedPrivateField: private State state = State.START;
    int start = 0;
    int end = 0;

    // in order to use it as a stack, use: addFirst(e), removeFirst(e), peekFirst(e)

    /**
     * 
     */
    public SFT() {
        //PMDUnusedPrivateField: stack = new ArrayDeque<Object>();
        queue = new LinkedList<Object>();
        text = "";
        position = 0;
    } // SFT() method

    /**
     * 
     * @param argText
     */
    public SFT(final String argText) {
        this();
        text = argText;
    } // SFT() method

    /**
     * 
     */
    public void parse() {
        int pos = lookAhead('(');
        if (pos == -1) {
            //PMDUnusedPrivateField: state = State.ERROR;
            return;
        } //PMDEmptyIfStmt: else {
            //PMDUnusedPrivateField: state = State.BEGIN_ELEMENT;
        //PMDEmptyIfStmt: } // else
        ++position;
        start = position;
        //PMDUnusedPrivateField: state = State.TYPE;
        end = lookAhead(' ');
        String val = text.substring(start, end);
        queue.add(val);

        while (end != -1) {
            ++position;
            //PMDUnusedPrivateField: state = State.VALUE;
            start = position;
            end = lookAhead(' ');
            if (end != -1) {
                queue.add(text.substring(start, end));
            } // if
        } // while

        if (end == -1) {
            end = lookAhead(')');
            //PMDEmptyIfStmt: if (end == -1) {
                //PMDUnusedPrivateField: state = State.ERROR;
            //PMDEmptyIfStmt: } else {
            if (end != -1) { //PMDEmptyIfStmt introduced check instead
                //end = end - 1;
                //PMDUnusedPrivateField: state = State.VALUE;
                queue.add(text.substring(start, end));
                //PMDUnusedPrivateField: state = State.END_ELEMENT;
            } // else
        } // if
    } // parse() method

    /**
     * Scan until argWhat is found.
     * @param argWhat
     * @return
     */
    private int lookAhead(final char argWhat) {
        int returnPosition = -1; //PMDOnlyOneReturn introduced local variable 
        for (int i = position; i < text.length(); i++) {
            position = i;
            if (text.charAt(position) == argWhat) {
                //PMDOnlyOneReturn: return position;
                returnPosition = position; //PMDOnlyOneReturn utilised local variable 
            } // if
        } // for
        //PMDOnlyOneReturn: return -1;
        return returnPosition; //PMDOnlyOneReturn utilised local variable
    } // lookAhead() method

    /**
     * Useful when developer needs to check the first element in the deque (typically to determine a type).
     * @return
     */
    public Object peek() {
        return queue.peek();
    } // peek() method

    /**
     * 
     */
    public void dump() {
        for (Object obj : queue) {
            System.out.println("[" + obj + "]");
        } // for
    } // dump() method

    /**
     * 
     * @param args
     */
    public static void main(final String[] args) {
        SFT sft = new SFT("(User LED01 Dejan Lekic 2000.22)");
        sft.parse();
        sft.dump();
    } // main() method

} // SFT class

// $Id$
