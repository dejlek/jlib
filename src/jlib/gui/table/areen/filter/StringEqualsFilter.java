/**
 * $Id: StringEqualsFilter.java 796 2010-03-14 20:14:18Z dejan $
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

/**
 * TODO: In general we can have a generic Comparable<T> filter...
 * @author dejan
 */
public class StringEqualsFilter extends BaseRowFilter {
	String stringToCompare;

	public StringEqualsFilter() {
		stringToCompare = "<all>";
	}

	public StringEqualsFilter(String argStr) {
		stringToCompare = argStr;
	}

	public void setString(String argStr) {
		stringToCompare = argStr;
	}

	public boolean check(Object argValue) {
		if (stringToCompare.equals("<all>")) // bypass the filtering in this case
			return true;
		else
			return (((String)argValue).equals(stringToCompare));
	} // check()

	@Override
	public void reset() {
		setString("<all>");
	}

	public String toString() {
		return stringToCompare;
	}
} // StringEqualsFilter calss
