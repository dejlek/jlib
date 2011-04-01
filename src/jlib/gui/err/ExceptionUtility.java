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
 * This file is best viewed with 128 columns.
12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678
 * 
 * Author(s):
 *   Dejan Lekic , http://dejan.lekic.org
 * Contributor(s):
 *   -
 */

package jlib.gui.err;

import java.awt.Frame;


/**
 *
 * @author dejan
 */
public class ExceptionUtility {

	/**
	 * Call it via:
	 * <pre>ExceptionUtility.exceptionDialog(myFrame, myException, Utility.getLineInf());</pre>
	 * @param argFrame
	 * @param argException
	 * @param argLineInfo
	 */
	public static void exceptionDialog(Frame argFrame, Exception argException, String argLineInfo) {
		ExceptionDialog ed = new ExceptionDialog(argFrame, true);
		String exceptionString = argException + "\n:::::::::::::::::::::::::::::::::\n";
		StackTraceElement[] elements = argException.getStackTrace();
		for (int i = 0; i<elements.length; i++) {
			exceptionString += elements[i].toString() + "\n";
		}
		ed.setInfo(argLineInfo);
		ed.setExceptionText(exceptionString);
		ed.setLocationRelativeTo(argFrame);
		ed.setVisible(true);
	} // exceptionDialog() method
} // ExceptionUtility class

// $Id$
