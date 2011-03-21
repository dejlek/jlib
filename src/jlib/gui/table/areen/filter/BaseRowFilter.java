/**
 * $Id: Main.java 634 2010-02-17 09:41:17Z dejan $
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
 *
 * @author dejan
 */
public abstract class BaseRowFilter {
	private Object compareToThis;
	private int columnIndex = 0;

	public boolean check(Object argValue) {
		return (argValue.equals(compareToThis));
	} // check()

	public void setColumnIndex(int argIndex) {
		columnIndex = argIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	/**
	 * This method must be provided by all subclasses so user can reset filters
	 * to their default values (so they show all rows).
	 */
	public abstract void reset();

	@Override
	public String toString() {
		return "(BaseRowFilter idx " +  columnIndex + " val \"" + compareToThis.toString() +"\")";
	}
} // IRowFilter interface

// $Id$
