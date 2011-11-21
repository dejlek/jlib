/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.ds;

import com.areen.jlib.exceptions.SmallArrayException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dejan
 */
public class SmallArrayIntTest {
    SmallArrayInt testArray;

    public SmallArrayIntTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testConstructor() {
        System.out.println("TEST: testConstructor()");
        try {
            // TODO review the generated test code and remove the default call to fail.
            testArray = new SmallArrayInt(3);
        } catch (SmallArrayException ex) {
            Logger.getLogger(SmallArrayIntTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertTrue("Incorrect number of elements. Got " + testArray.size() + ", expected 10.", (testArray.size() == 10));
    } // testConstructor() method

} // SmallArrayIntTest class

// $Id$
