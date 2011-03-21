package jlib.pattern.singleton;

/**
 * Singleton pattern -  the most simple implementation.
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
	}
} // Singleton class
