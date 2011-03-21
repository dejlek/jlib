/**
 * $Id: NumberFilter.java 796 2010-03-14 20:14:18Z dejan $
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
public class NumberFilter extends BaseRowFilter {
	String stringToCompare;
	double valueToCompare = 0.0;
	char operation = '*'; // * returns every row, > greater than, < less than, = equal to

	public NumberFilter() {
		stringToCompare = "<all>";
	}

	public NumberFilter(String argStr) {
		setString(argStr);
	} // NumberFilter() constructor

	/**
	 * @note Method assumes user-input is perfect!
	 * @todo Make user-input more flexible. Like: spaces between operators and numbers, etc.
	 * @param argStr
	 */
	public void setString(String argStr) {
		stringToCompare = argStr;
		String trimmed = argStr.trim();
		if (argStr.isEmpty() || (trimmed.charAt(0) == '*') || argStr.equals("<all>")) {
			stringToCompare = "<all>";
			valueToCompare = 0.0;
		} else {
			operation = argStr.charAt(0);
			valueToCompare = Double.parseDouble(argStr.substring(1));
			stringToCompare = operation + " " + valueToCompare;
		} // else
	} // setString() method

	public boolean check(Object argValue) {
		if (stringToCompare.equals("<all>")) // bypass the filtering in this case
			return true;
		switch (operation) {
			case '<': return (Double.parseDouble(argValue.toString()) < valueToCompare);
			case '=': return (Double.parseDouble(argValue.toString()) == valueToCompare);
			case '>': return (Double.parseDouble(argValue.toString()) > valueToCompare);
			default: return true;
		} // switch
	} // check()

	@Override
	public void reset() {
		setString("*");
	}

	public String toString() {
		return "" + operation + " " + valueToCompare;
	} // toString() method
} // NumberFilter calss
