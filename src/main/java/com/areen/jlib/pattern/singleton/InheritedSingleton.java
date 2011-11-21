
package com.areen.jlib.pattern.singleton;

/**
 * This is just an example how to inherit Singleton, and use the inherited class.

 * @author dejan
 */
public class InheritedSingleton extends Singleton {

    private final static int DEFAULT_VALUE = 5;
    private final int value;

    public static void init(final String[] params) {
        instance = new InheritedSingleton(params, DEFAULT_VALUE);
    }

    public static void init(final String[] params, final int value) {
        instance = new InheritedSingleton(params, value);
    }

    public static InheritedSingleton getInstance() {
        return (InheritedSingleton) instance;
    }

    public int getVal() {
        return value;
    }

    protected InheritedSingleton(final String[] params, final int argValue) {
        super(params);
        this.value = argValue;
    }
}
