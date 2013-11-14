/**
 * Project: JLIB
 * Version: $Id$ 
 * License: (see below block, or the accompanied COPYING.txt file)
 **************************************************************************************** LICENSE BEGIN *****
 * Copyright (c) 2009-2013, JLIB AUTHORS (see the AUTHORS.txt file for the list of the individuals)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided 
 * that the following conditions are met:
 * 
 *  1) Redistributions of source code must retain the above copyright notice, this list of conditions and the 
 *     following disclaimer.
 *  2) Redistributions in binary form must reproduce the above copyright notice, this list of conditions and 
 *     the following disclaimer in the documentation and/or other materials provided with the distribution.
 *  3) Neither the names of the organisations involved in the JLIB project, nor the names of their 
 *     contributors to the JLIB project may be used to endorse or promote products derived from this software 
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED 
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A 
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR 
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 ****************************************************************************************** LICENSE END *****
 * 
 * This file is best viewed with 110 columns.
12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 * 
 * In chronological order
 * Author(s):
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
    public static enum Field implements ValueObject.Field {
        USER_ID,
        DEPARTMENT,
        USER_NAME,
        PASSWORD
    }

    static final int NUMBER_OF_FIELDS = 4;
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

    public User(final String  argUserId, final String argdepartment, final String arguserName,
                final String argpassword) {
        value = new User.VO();
        value.userId = argUserId;
        value.dept = argdepartment;
        value.userName = arguserName;
        value.password = argpassword;
    } // User constructor

    /**
     * Copy constructor
     * @param argValue 
     */
    public User(final User.VO argValue) {
        this(argValue.userId, argValue.dept, argValue.userName, argValue.password);
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
    public int getNumberOfFields() {
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
    
    //@Override
    public Object get(final User.Field argField) {
        Object ret = null;
        switch (argField) {
            case USER_ID: ret = value.userId; break;
            case DEPARTMENT: ret = value.dept; break;
            case USER_NAME: ret = value.userName; break;
            case PASSWORD: ret = value.password; break;
            default: break; // No action
        } // switch
        return ret;
    } // get() method
    
    //@Override
    public Object set(final User.Field argField, final Object argValue) {
        Object result = null;
        switch (argField) {
            case USER_ID: value.userId = (String) argValue; break;
            case DEPARTMENT: value.dept = (String) argValue; break;
            case USER_NAME: value.userName = (String) argValue; break;
            case PASSWORD: value.password = (String) argValue; break;
            default: break; // No action
        } // switch
        result = argValue;
        
        return result;
    }
    
    // ====================================================================================================
    // ==== Main function =================================================================================
    // ====================================================================================================
    
	public static void main(String[] args) {
		User user = new User();
        user.set(Field.USER_ID, "LED01");
        user.set(Field.DEPARTMENT, "ITC");
        user.set(Field.USER_NAME, "dejan");
        user.set(Field.PASSWORD, "5up3r53cr3t");
        System.out.println(user);
        if (user instanceof Cloneable) {
            System.out.println("Cloneable!");
        }
        
	} // main() function
    
} // User class

// $Id$
