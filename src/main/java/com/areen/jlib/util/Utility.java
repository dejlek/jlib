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
package com.areen.jlib.util;

import com.areen.jlib.model.SimpleObject;
import com.areen.jlib.tuple.Pair;
import eu.medsea.mimeutil.MimeUtil2;
import eu.medsea.mimeutil.detector.OpendesktopMimeDetector;
import eu.medsea.mimeutil.detector.WindowsRegistryMimeDetector;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;


/**
 * Various utility functions.
 * 
 * @author dejan
 */
public class Utility {
    
    /**
     * A helper constructor to prevents calls from asubclass.
     */
    protected Utility() { 
        throw new UnsupportedOperationException(); 
    } // Utility constructor

    /**
     * A convenience function which should be used when we need a line number where the exception has been
     * thrown.
     * 
     * @return String File name, plus the line number where the exception has been thrown.
     */
    public static String getLineInfo() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getFileName() + ": Line " + ste.getLineNumber();
    } // getLineInfo() method

    /**
     * Function to export csv file.
     * 
     * TODO: (Dejan) we should remove Iterator(s), and use foreach loops instead.
     * 
     * @param file A String containing the file-name information (with path).
     * @param h1 A String object with the first header line.
     * @param h2 A String object with the second header line.
     * @param headings A Vector with column headings as String objects.
     * @param data A Vector of vectors to be exported to the CSV file.
     */
    public static void exportToCSV(final String file, final String h1, final String h2,
                                   final Vector<String> headings, final Vector<Vector<String>> data) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            PrintStream ps = new PrintStream(out);

            ps.println(h1);
            ps.println(h2);
            ps.println();

            //print headings
            Iterator<String> hi = headings.iterator();

            int i = 0;
            while (hi.hasNext()) {
                ps.print(hi.next());

                if (i < headings.size() - 1) {
                    ps.print(",");
                } else {
                    ps.println();
                } // else
                i++;
            } // while

            // print the data
            Iterator<Vector<String>> di = data.iterator();
            Iterator<String> si = null;

            while (di.hasNext()) {
                // get iterator
                si = di.next().iterator();
                i = 0;
                while (si.hasNext()) {
                    ps.print(si.next());

                    if (i < headings.size() - 1) {
                        ps.print(",");
                    } else {
                        ps.println();
                    } // else

                    i++;
                } // while
            } // while

            ps.close();
        } catch (IOException e) {
            e.printStackTrace();
        } // catch
    } // exportToCSV() method

    /**
     * Utility function which should be used when developer has a value as a String, and has to assign
     * the value to a field of a SimpleObject argSo. As you know all SimpleObject members are statically
     * typed, so argValue has to be converted to appropriate type.
     * 
     * @param argSo SimpleObject which field we have to set.
     * @param argValue The value as a String.
     * @param argIndex Index of the SO field to which we assign the value argValue.
     */
    public static void set(final SimpleObject argSo, final String argValue, final int argIndex) {
        try {
            if (argSo.getFieldClass(argIndex) == Double.class) {
                argSo.set(argIndex, Double.parseDouble(argValue));
            } else if (argSo.getFieldClass(argIndex) == String.class) {
                argSo.set(argIndex, argValue.toString());
            } else if (argSo.getFieldClass(argIndex) == Integer.class) {
                argSo.set(argIndex, Integer.parseInt(argValue));
            } else if (argSo.getFieldClass(argIndex) == Float.class) {
                argSo.set(argIndex, Float.parseFloat(argValue));
            } //end of try
        } catch (NumberFormatException numberFormat) {
            System.out.println("DEBUG: NumberFormat Error");
             if (argSo.getFieldClass(argIndex) == Double.class) {
                argSo.set(argIndex, 0.0);
            } else if (argSo.getFieldClass(argIndex) == Float.class) {
                argSo.set(argIndex, 0.0f);  
            } else if (argSo.getFieldClass(argIndex) == Integer.class) {
                argSo.set(argIndex, 0);
            } // else if
        } // end catch
    } // set() method

    /**
     * A utility method to compare two objects, both of type argClass and see if they have equal content 
     * or not.
     * 
     * @param argFirst First Object.
     * @param argSecond Second Object.
     * @param argClass The type.
     * @return 0 if they are equal, some other value otherwise.
     */
    public static int compare(Object argFirst, Object argSecond, Class argClass) {
        // NULL cases
        if (argFirst == null && argSecond == null) {
            return 0;
        }
        if (argFirst == null && argSecond != null) {
            return -1;
        }
        if (argFirst != null && argSecond == null) {
            return 1;
        }

        // if argFirst and argSecond references refer to the same object, return 0
        if (argFirst == argSecond) {
            return 0;
        }
        
        // else, make comparison for each type...
        if (argClass == Byte.class) {
            Byte a = (Byte) argFirst;
            Byte b = (Byte) argSecond;
            return a.compareTo(b);
        }
        if (argClass == Short.class) {
            Short a = (Short) argFirst;
            Short b = (Short) argSecond;
            return a.compareTo(b);
        }
        if (argClass == Integer.class) {
            Integer a = (Integer) argFirst;
            Integer b = (Integer) argSecond;
            return a.compareTo(b);
        }
        if (argClass == Long.class) {
            Long a = (Long) argFirst;
            Long b = (Long) argSecond;
            return a.compareTo(b);
        }
        if (argClass == Float.class) {
            Float a = (Float) argFirst;
            Float b = (Float) argSecond;
            return a.compareTo(b);
        }
        if (argClass == Double.class) {
            Double a = (Double) argFirst;
            Double b = (Double) argSecond;
            return a.compareTo(b);
        }
        if (argClass == Date.class) {
            Date a = (Date) argFirst;
            Date b = (Date) argSecond;
            return a.compareTo(b);
        }
        
        // What if one of the objects is null?
        if (argSecond == null) {
            return 1;
        }
        if (argFirst == null) {
            return -1;
        }
        
        // fall back to using Strings...
        return argFirst.toString().compareTo(argSecond.toString());
    } // compare() method

    /**
     * Use this method whenever you have to convert an array of Objects to a String.
     */
    public static String oa2string(Object[] argObjectArray, String argSeparator) {
        if (argObjectArray == null) {
            return "";
        } // if
        if (argObjectArray.length == 0) {
            return "";
        } // if
        if (argObjectArray.length == 1) {
            return argObjectArray[0].toString();
        } // if
        String ret = "";
        if (argObjectArray[0] != null) {
            ret = argObjectArray[0].toString();
        }
        for (int i = 1; i < argObjectArray.length; i++) {
            if (argObjectArray[i] != null) {
                ret += argSeparator + argObjectArray[i].toString();
            } // if
        } // for 
        return ret;
    } // oa2string() method

    /**
     * Checks whether character argCharacter is printable or not.
     * The code is taken from the following StackOverflow thread:
     * http://stackoverflow.com/questions/220547/printable-char-in-java
     * 
     * @param argCharacter
     * @return 
     */
    public static boolean isPrintableChar(char argCharacter) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(argCharacter);
        return (!Character.isISOControl(argCharacter))
                && argCharacter != KeyEvent.CHAR_UNDEFINED
                && block != null
                && block != Character.UnicodeBlock.SPECIALS;
    } // isPrintableChar() method

    /**
     * Use this method to convert any Number object to a double.
     * 
     * NOTE: This method may become deprecated because user can simply do:
     * 
     * Number num = (Number) object; // object is an instance of any Number subtype.
     * double value = num.doubleValue();
     * 
     * @param argSource
     * @return 
     */
    public static double toDouble(Object argSource) {
        double ret = Double.NaN;
        Class argClass = argSource.getClass();
        
        if (argClass == Byte.class) {
            Byte val = (Byte) argSource;
            ret = val.doubleValue();
        }
        if (argClass == Short.class) {
            Short val = (Short) argSource;
            ret = val.doubleValue();
        }
        if (argClass == Integer.class) {
            Integer val = (Integer) argSource;
            ret = val.doubleValue();
        }
        if (argClass == Long.class) {
            Long val = (Long) argSource;
            ret = val.doubleValue();
        }
        if (argClass == Float.class) {
            Float val = (Float) argSource;
            ret = val.doubleValue();
        }
        if (argClass == Double.class) {
            ret = (Double) argSource;
        }
        return ret;
    } // toDouble() method

    /**
     * This method is useful when you want to convert an array of Objects into a Pair. argColumns array 
     * contains indexes of those elements we want concatenated in the pair's second element.
     * @param argArray
     * @param argColumns
     * @param argDelim String delimiter.
     * @return 
     */
    public static Pair<String, String> pairOfStrings(Object[] argArray, int[] argColumns, String argDelim) {
        Pair<String, String> pair = new Pair<String, String>(argArray[argColumns[0]].toString(), 
                argArray[argColumns[1]].toString());
        
        StringBuilder sb = new StringBuilder(argArray[argColumns[1]].toString());
        for (int i = 2; i < argColumns.length; i++) {
            sb.append(argDelim);
            sb.append(argArray[argColumns[i]]);
        } // foreach
        pair.setSecond(sb.toString());

        return pair;
    } // pairOfString() method

    /**
     * Java 7 has Files.propeContentType(path) . However we can't use it yet, so we have to do a workaround.
     * guessContentTypeFromStream() "understands" only few MIME types thought...
     * 
     * @param argFileName
     * @return 
     */
    public static String getMimeType(String argFileName) {
        File file = new File(argFileName);
        MimeUtil2 checker = new MimeUtil2();
        String mimeclass = null;

        if (System.getProperty("os.name").startsWith("Windows")) {
            checker.registerMimeDetector(WindowsRegistryMimeDetector.class.getCanonicalName());
            mimeclass = checker.getMimeTypes(file).toString();
            checker.unregisterMimeDetector(WindowsRegistryMimeDetector.class.getCanonicalName());
        } else {
            checker.registerMimeDetector(OpendesktopMimeDetector.class.getCanonicalName());
            mimeclass = checker.getMimeTypes(file).toString();
            checker.unregisterMimeDetector(OpendesktopMimeDetector.class.getCanonicalName());
        }

        if (mimeclass == null || mimeclass.equalsIgnoreCase("application/octet-stream")) {
            // fall back to get by extension
            mimeclass = MIMEUtil.getType(argFileName.substring(argFileName.lastIndexOf(".")));
        }
               
        return mimeclass;
    } // getMimeType() method

} // Utility class

// $Id$
