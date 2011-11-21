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
 * This is the main class for the "apc-client" application.
 * Execute apc-client like this: java -jar apc-client.jar -Dareen.developer=dejan
 * You can ommit the parameter if you do not want to debug the application.
 *
 * Author(s) in chronological order:
 *   Dejan Lekic , http://dejan.lekic.org
 * Contributor(s):
 *   -
 */

package com.areen.jlib.pattern;

/**
 * This interface formulates what methods must be implemented by any CRUD operations implementation.
 *
 * @author dejan
 */
public interface CrudOperations<T extends ValueObject> {
    // CONSTANTS ----------------------------------------
    int CREATE = 1;
    int CREATE_VO = 2;
    int CREATE_ARRAY = 3;
    int READ_ALL = 4;
    int UPDATE_VO = 5;
    int UPDATE_ARRAY = 6;
    int DELETE_VO = 7;
    int DELETE_ARRAY = 8;

    // EXTRA METHODS ------------------------------------
    public void    setOldValue(Object argOldValue);
    public void    setArgs(String argInput); // Use this method to specify parameters to the CRUD object.
    public void    set(String argName, Object argValue);
    public Object  get(String argName);
    public boolean check(int argOperation, T[] argVos); // Checks whether operation can be executed.
    public String  getMessage(); // In the case check() fails, use this method to get the reason why.
    public void    setOldValues(T[] argVos);

    // CREATE -------------------------------------------
    public T       create();
    public T       create(T argVo);
    public T[]     create(T[] argVos); // Used for duplicating rows

    // READ ---------------------------------------------
    public T[]     readAll();

    // UPDATE -------------------------------------------
    public T       update(T argOld);
    public T       update(T argOld, int argFieldNumber); /** byte would be better here, but Java is stupid,
                                                             we would have to cast arg to byte every time */
    // DELETE -------------------------------------------
    public boolean delete(T argVo);
    public boolean delete(T[] argVos); /// Used to delete a group of rows
} // CrudOperations<T> interface

// $Id$
