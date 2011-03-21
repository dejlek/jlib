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

	/**
	 * Test of init method, of class Singleton.
	 */ @Test
	public void testInit() {
		System.out.println("init+getInstance");
		String[] params = null;
		InheritedSingletonTest.init(params);
		if (InheritedSingletonTest.getInstance().getVal() != 5)
			fail("The test case is a prototype.");
	}

}