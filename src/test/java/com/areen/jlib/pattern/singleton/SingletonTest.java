/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.areen.jlib.pattern.singleton;

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
public class SingletonTest {

    public SingletonTest() {

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

    /**
     * Test of init method, of class Singleton.
     */
    @Test
    public void testInit() {
        System.out.println("testInit()");
        String[] params = null;
        InheritedSingletonTest.init(params);
        assertTrue(InheritedSingletonTest.getInstance().getVal() == 5);
    }

    /**
     * Test of getInstance method, of class Singleton.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance()");
        String[] params = null;
        InheritedSingletonTest.init(params);
        assertTrue(InheritedSingletonTest.getInstance().getVal() == 5);
    }
}
