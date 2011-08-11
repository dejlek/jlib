/**
 * $Id$
 *
 * Copyright (c) 2009-2010 Areen Design Services Ltd
 * 23 Eyot Gardens; London; W6 9TR
 * http://www.areen.com
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of
 * Areen Design Services Ltd ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Areen Design Services Ltd.
 * 
 * This file is best viewed with 110 columns.
12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 * 
 * Author(s) in chronological order:
 *   Dejan Lekic , http://dejan.lekic.org
 * Contributor(s):
 *   -
 */

package com.areen.jlib.util;

import com.areen.jlib.model.SimpleObject;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author dejan
 */
public class Utility {

	public static String getLineInfo() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getFileName() + ": Line " + ste.getLineNumber();
    } // getLineInfo() method
	
	/**
	 * Function to export csv file
	 * @param file
	 * @param h1
	 * @param h2
	 * @param headings
	 * @param data
	 */
	public static void exportToCSV(String file, String h1, String h2, Vector<String> headings, Vector<Vector<String>> data){
		try {
			FileOutputStream out = new FileOutputStream(file);
			PrintStream ps = new PrintStream(out);
			
			ps.println(h1);
			ps.println(h2);
			ps.println();
			
			//print headings
			Iterator<String> hi = headings.iterator();
			
			int i = 0;
			while(hi.hasNext()){
				ps.print(hi.next());
				
				if(i<headings.size()-1)
					ps.print(",");
				else 
					ps.println();
				i++;
			}
			
			//print the data
			Iterator<Vector<String>> di = data.iterator();
			Iterator<String> si = null;
			
			while(di.hasNext()){
				//get iterator
				si = di.next().iterator();
				i=0;
				while(si.hasNext()){
					ps.print(si.next());
					
					if(i<headings.size()-1)
						ps.print(",");
					else 
						ps.println();
					
					i++;
				}
			}
			
			ps.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // exportToCSV() method
        
        public static void set(SimpleObject argSo, String argValue, int argIndex) {
            if (argSo.getFieldClass(argIndex) == Double.class) {
                argSo.set(argIndex, Double.parseDouble(argValue));
            } // if
        } // set() method
	
} // Utility class

// $Id$
