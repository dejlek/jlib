/**
 * $Id$
 *
 * Copyright (c) 2009-2013 Areen Design Services Ltd
 * 23 Eyot Gardens; London; W6 9TR
 * http://www.areen.com
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of Areen Design Services Ltd ("Confidential 
 * Information").  You shall not disclose such Confidential Information and shall use it only in accordance 
 * with the terms of the license agreement you entered into with Areen Design Services Ltd.
 * 
 * This file is best viewed with 110 columns.
12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
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
    int CREATE = 1;       // for creating a new record without any parameter given
    int CREATE_VO = 2;    // --//-- with a VO as parameter
    int CREATE_ARRAY = 3; // --//-- new records with an array of VO objects as parameter
    int READ_ALL = 4;     // READ all records
    int UPDATE_VO = 5;    // A VO given as parameter to the UPDATE
    int UPDATE_ARRAY = 6; // An array of VOs given as parameter to the UPDATE
    int DELETE_VO = 7;    // A VO given as parameter to the DELETE
    int DELETE_ARRAY = 8; // An array of VOs given as parameter to the DELETE
    int READ = 9;         // READ a single record

    // EXTRA METHODS ------------------------------------
    void    setOldValue(Object argOldValue);
    void    setArgs(String argInput); // Use this method to specify parameters to the CRUD object.
    void    set(String argName, Object argValue);
    Object  get(String argName);
    boolean check(int argOperation, T[] argVos); // Checks whether operation can be executed.
    
    /**
     * In the case check() fails, use this method to get the reason why.
     * @return String value with the message to the user. If return value is NULL, and operation is UPDATE,
     *         that means that the update operation failed!
     */
    String  getMessage();
    
    void    setOldValues(T[] argVos);
    boolean isLocked(int argRowIndex);

    // CREATE -------------------------------------------
    T       create();
    T       create(T argVo);
    T[]     create(T[] argVos); // Used for duplicating rows

    // READ ---------------------------------------------
    T[]     readAll();
    T[]     read(T[] argVos);

    // UPDATE -------------------------------------------
    T       update(T argOld);
    T       update(T argOld, int argFieldNumber); /** byte would be better here, but Java is stupid,
                                                             we would have to cast arg to byte every time */
    /**
     * This method is executed in all multiple-updates.
     * @param argVos An array of MODIFIED ValueObjects
     * @return An array of ValueObjects potentially modified by the database layer.
     */
    T[]     update(T[] argVos);
    
    // DELETE -------------------------------------------
    boolean delete(T argVo);
    boolean delete(T[] argVos); /// Used to delete a group of rows
} // CrudOperations<T> interface

// $Id$
