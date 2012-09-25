/**
 * $Id: AreenTableModel.java 825 2012-08-13 16:37:23Z dejan $
 *
 * Copyright (c) 2009-2012 Areen Design Services Ltd
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

package com.areen.jlib.test;

import com.areen.jlib.model.SimpleObject;
import com.areen.jlib.pattern.ValueObject;
import java.io.Serializable;

/**
 *
 * @author dejan
 */
public final class User implements SimpleObject<User.VO> {

    static final byte NUMBER_OF_FIELDS = 4;
    String[] titles = new String[]{"User ID", "Department", "Username", "Password"};

    @Override
    public User create() {
        return new User();
    }

    @Override
    public User[] create(int arg) {
        return new User[arg];
    }

    public static class VO implements ValueObject, Serializable {
        long userPk = 0; /// reserved for future use
    String userId = "UNK01";     /// "userPk" column
    String dept;             /// reference VO an adequate row in the dir_val_depts table
    String userName = "unknown"; /// "vms_username" column
    String password = "unknown"; /// "extension" column
        /**
         * user_id                          char            5  yes    null     1
         * dept_code                        char            3  yes    null
         * vms_username                     char           30  yes    null
         * print_queue                      char           30  yes    null
         * batch_queue                      char           30  yes    null
         * extension                        char           10  yes    null
         * authority_level                  char           15  yes    null
         * keyboard_type                    char            3  yes    null
         */
    } // User.VO class

    User.VO value;
    /*************************************************************************
     * Constructors
     *************************************************************************/
    /**
     * Default constructor
     */
    public User() {
        value = new User.VO();
    } // User (default) constructor

    public User(final User.VO argValue) {
        value = argValue;
    } // User constructor

    public User(final String  argUserId, final String argdepartment, final String arguserName,
                final String argpassword) {
        value = new User.VO();
        value.userId = argUserId;
        value.dept = argdepartment;
        value.userName = arguserName;
        value.password = argpassword;
    } // User constructor

    /**
     * Copy constructor.
     * Used by clone().
     * @param argOriginal 
     */
    public User(User argOriginal) {
        value = new User.VO();
        User.VO originalValue = (User.VO) argOriginal.getValue();
        value.userId = originalValue.userId;
        value.dept = originalValue.dept;
        value.userName = originalValue.userName;
        value.password = originalValue.password;
    }

    @Override
    public Object get(final int argIndex) {
        Object ret = null;
        switch (argIndex) {
            case 0: ret = value.userId; break;
            case 1: ret = value.dept; break;
            case 2: ret = value.userName; break;
            case 3: ret = value.password; break;
            default: break; // No action
        } // switch
        return ret;
    }

    @Override
    public Class<?> getFieldClass(final int argIndex) {
        Class<?> ret = Object.class;
        switch (argIndex) {
            case 0: ret = String.class; break;
            case 1: ret = String.class; break;
            case 2: ret = String.class; break;
            case 3: ret = String.class; break;
            default: break; // No action
        } // switch
        return ret;
    }

    @Override
    public byte getNumberOfFields() {
        return NUMBER_OF_FIELDS;
    }

    @Override
    public String[] getTitles() {
        return titles;
    }

    @Override
    public User.VO getValue() {
        return value;
    }

    @Override
    public User.VO[] newArray(final int argNumberOfElements) {
        return new User.VO[argNumberOfElements];
    }

    @Override
    public User.VO newValue() {
         return new User.VO();
    }

    @Override
    public Object set(final int argIndex, final Object argValue) {
        Object result = null;
        switch (argIndex) {
            case 0: value.userId = (String) argValue; break;
            case 1: value.dept = (String) argValue; break;
            case 2: value.userName = (String) argValue; break;
            case 3: value.password = (String) argValue; break;
            default: break; // No action
        } // switch
        if (argIndex >= 0 && argIndex < NUMBER_OF_FIELDS) {
            result = argValue;
        }
        return result;
    }

    @Override
    public void setTitles(final String[] argTitles) {
        titles = argTitles;
    }

    @Override
    public void setValue(final User.VO argVo) {
        value = (User.VO) argVo;
    }

    @Override
    public String toString() {
        return "(Project "
        + value.userId + " "
        + value.dept + " "
        + "`" + value.userName + "` "
        + "`" + value.password + "` "
                + ")";
    } // toString() method

    @Override
    public SimpleObject<User.VO> clone() {
        User ret = new User(this);
        return ret;
    }
    
} // User class

// $Id$
