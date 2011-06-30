/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jlib.pattern.singleton;

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
      System.out.println("init");
      String[] params = null;
      Singleton.init(params);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of getInstance method, of class Singleton.
    */
   @Test
   public void testGetInstance() {
      System.out.println("getInstance");
      Singleton expResult = null;
      Singleton result = Singleton.getInstance();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

}