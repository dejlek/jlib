/**
 * $Id: CodePair.java 76 2011-08-11 12:10:31Z dejan $
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
 *   Dejan Lekic, http://dejan.lekic.org
 * Contributor(s):
 *   -
 */

package com.areen.jlib.gui.form.ifp;

import com.areen.jlib.tuple.Pair;

/**
 * A small subclass of our Pair<K,V> class from the JLib which is going to be used for JComboBox items.
 */
public class CodePair extends Pair<String, String> {

    public CodePair(final String left, final String right) {
        super(left, right);
    }

    @Override
    public String toString() {
        return first + " - " + second;
    }
} // CodePair class

// $Id: CodePair.java 76 2011-08-11 12:10:31Z dejan $
