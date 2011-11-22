package com.areen.jlib.pattern.singleton;

/**
 *
 * @author dejan
 */
public class InheritedSingletonTest extends Singleton {

    private static final int DEFAULT_VALUE = 5;
    private final int value;

    public static void init(final String[] params) {
        instance = new InheritedSingletonTest(params, DEFAULT_VALUE);
    }

    public static void init(final String[] params, final int value) {
        instance = new InheritedSingletonTest(params, value);
    }

    public static InheritedSingletonTest getInstance() {
        return (InheritedSingletonTest) instance;
    }

    public int getVal() {
        return value;
    }

    protected InheritedSingletonTest(final String[] params, final int argValue) {
        super(params);
        this.value = argValue;
    }
}

// $Id$
