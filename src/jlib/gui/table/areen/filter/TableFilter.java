/**
 * $Id: TableFilter.java 796 2010-03-14 20:14:18Z dejan $
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

package jlib.gui.table.areen.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Simple regex based table filter
 * @author dejan
 * @todo Add exception handling here!
 */
public class TableFilter extends BaseRowFilter {
	Pattern pattern;
	int columnIndex = 0;

	//TODO: Add exception handling here!
	public TableFilter() {
		pattern = Pattern.compile("^.*$");
	}

	public TableFilter(String argPattern) {
		pattern = Pattern.compile(argPattern);
	}

	public boolean check(Object argValue) {
		String str = (String)argValue.toString();
		Matcher m = pattern.matcher(str);
		return m.matches();
	} // check()

	public void setPattern(String argPattern) {
		// re-compile new pattern
		try {
			pattern = Pattern.compile(argPattern);
		} catch (PatternSyntaxException pse) {
			System.err.println("Bad regex pattern. \n" + pse);
		} //catch
	}

	@Override
	public void reset() {
		setPattern("^.*$");
	}


	public String toString() {
		return pattern.toString();
	}
}
