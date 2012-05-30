/**
 * Project: jlib
 * Version: $Id$
 * License: SPL
 *
 * This file is best viewed with 110 columns.
 * 34567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
 *
 * Authors (in chronological order):
 *   Dejan Lekic - http://dejan.lekic.org
 * Contributors (in chronological order):
 *   -
 */

package com.areen.jlib.gui;

import java.awt.BorderLayout;
import java.io.CharArrayWriter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This class has been take from the following StackOverflow thread:
 * 
 * http://stackoverflow.com/questions/342990/create-java-console-inside-the-panel
 * @author dejan
 */
public class TextAreaOutputStream
        extends OutputStream {

    // *****************************************************************************
    // STATIC PROPERTIES
    // *****************************************************************************
    private static final byte[] LINE_SEP = System.getProperty("line.separator", "\n").getBytes();
    
    // *****************************************************************************
    // INSTANCE PROPERTIES
    // *****************************************************************************
    private JTextArea textArea;                               // target text area
    private int maxLines;                               // maximum lines allowed in text area
    private LinkedList lineLengths;                            // length of lines within text area
    private int curLength;                              // length of current line
    private byte[] oneByte;                                // array for write(int val);

    // *****************************************************************************
    // INSTANCE CONSTRUCTORS/INIT/CLOSE/FINALIZE
    // *****************************************************************************
    /**
     * 
     * @param ta
     */
    public TextAreaOutputStream(JTextArea ta) {
        this(ta, 1000);
    } // TextAreaOutputStream() method

    /**
     * 
     * @param ta
     * @param ml
     */
    public TextAreaOutputStream(JTextArea ta, int ml) {
        if (ml < 1) {
            throw new IllegalArgumentException("Maximum lines of " + ml 
                    + " in TextAreaOutputStream constructor is not permitted");
        } // if
        textArea = ta;
        maxLines = ml;
        lineLengths = new LinkedList();
        curLength = 0;
        oneByte = new byte[1];
    } // TextAreaOutputStream() method

    // *****************************************************************************
    // INSTANCE METHODS - ACCESSORS
    // *****************************************************************************
    /**
     * 
     */
    public synchronized void clear() {
        lineLengths = new LinkedList();
        curLength = 0;
        textArea.setText("");
    } // clear() method

    /**
     * Get the number of lines this TextArea will hold.
     * @return 
     */
    public synchronized int getMaximumLines() {
        return maxLines;
    } // getMaximumLines() method

    /**
     * Set the number of lines this TextArea will hold.
     * @param val 
     */
    public synchronized void setMaximumLines(int val) {
        maxLines = val;
    } // setMaximumLines() method

    // *****************************************************************************
    // INSTANCE METHODS
    // *****************************************************************************
    @Override
    public void close() {
        if (textArea != null) {
            textArea = null;
            lineLengths = null;
            oneByte = null;
        } // if
    } // close() method

    @Override
    public void flush() {
    } // flush() method

    @Override
    public void write(int val) {
        oneByte[0] = (byte) val;
        write(oneByte, 0, 1);
    } // write() method

    @Override
    public void write(byte[] ba) {
        write(ba, 0, ba.length);
    } // write() method

    @Override
    public synchronized void write(byte[] ba, int str, int len) {
        try {
            curLength += len;
            if (bytesEndWith(ba, str, len, LINE_SEP)) {
                lineLengths.addLast(new Integer(curLength));
                curLength = 0;
                if (lineLengths.size() > maxLines) {
                    textArea.replaceRange(null, 0, ((Integer) lineLengths.removeFirst()).intValue());
                } // if
            } // if
            for (int xa = 0; xa < 10; xa++) {
                try {
                    textArea.append(new String(ba, str, len));
                    break;
                } catch (Throwable thr) {
                    // sometimes throws a java.lang.Error: Interrupted attempt to aquire write lock
                    if (xa == 9) {
                        thr.printStackTrace();
                    } else {
                        Thread.sleep(200);
                    } // else
                } // catch
            } // for
        } catch (Throwable thr) {
            CharArrayWriter caw = new CharArrayWriter();
            thr.printStackTrace(new PrintWriter(caw, true));
            textArea.append(System.getProperty("line.separator", "\n"));
            textArea.append(caw.toString());
        } // catch
    } // write() method

    private boolean bytesEndWith(byte[] ba, int str, int len, byte[] ew) {
        if (len < LINE_SEP.length) {
            return false;
        } // if
        for (int xa = 0, xb = (str + len - LINE_SEP.length); xa < LINE_SEP.length; xa++, xb++) {
            if (LINE_SEP[xa] != ba[xb]) {
                return false;
            } // if
        } // for
        return true;
    } // bytesEndWith() method
   
    /**
     * 
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame();
        frame.add(new JLabel("Output"), BorderLayout.NORTH);

        JTextArea ta = new JTextArea();
        TextAreaOutputStream taos = new TextAreaOutputStream(ta, 60);
        PrintStream ps = new PrintStream(taos);
        System.setOut(ps);
        System.setErr(ps);

        frame.add(new JScrollPane(ta));

        frame.pack();
        frame.setVisible(true);

        for (int i = 0; i < 100; i++) {
            System.out.println(i);
            Thread.sleep(500);
        } // for
    } // main() method
    
} // TextAreaOutputStream class

// $Id$
