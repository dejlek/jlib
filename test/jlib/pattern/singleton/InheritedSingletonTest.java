
package jlib.pattern.singleton;

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
