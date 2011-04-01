/**
 * $Id: Utility.java 840 2010-03-23 14:10:25Z dyki $
 *
 * Copyright (c) 2009-2010 Areen Design Services Ltd
 * 282 King Street; London, W6 0SJ; United Kingdom
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Areen Design Services Ltd ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Areen Design Services Ltd.
 */

package jlib.util;

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
    }
	
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
	}
}