
package com.areen.jlib.pattern.singleton;

/**
 * Singleton pattern - the simplest implementation.
 * 
 * EXAMPLE of usage:
 ******************* 
public class InheritedSingletonTest extends Singleton {

	private final static int DEFAULT_VALUE = 5;
	private final int value;

	public static void init(final String[] params) {
		instance = new InheritedSingletonTest(params, DEFAULT_VALUE);
	}

	public static void init(final String[] params, final int value) {
		instance = new InheritedSingletonTest(params, value);
	}

	public static InheritedSingletonTest getInstance() {
		return (InheritedSingletonTest)instance;
	}

	public int getVal() {
		return value;
	}

	protected InheritedSingletonTest(final String[] params, final int value) {
		super(params);
		this.value = value;
	}
} 
 *******************
 * As shown above, Singleton should be inherited by the application's singleton,
 * and application-specific functionality added as additional methods.
 * 
 * Code from the "Hardcode Java", p62.
 */
public class Singleton {
	public static Singleton instance = null;
	private final String[] params;

	protected Singleton(final String[] params) {
		this.params = params;
	} // Singleton constructor

	public static void init(final String[] params) {
		instance = new Singleton(params);
	} // init method

	public static Singleton getInstance() {
		return Singleton.instance;
	} // getInstance() method
   
} // Singleton class
