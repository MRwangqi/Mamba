package com.codelang.api;

/**
 * @author codelang
 */
public class Mamba {
    private static IMambaLoader iMambaLoader;

    public static void init(IMambaLoader iMambaLoader) {
        Mamba.iMambaLoader = iMambaLoader;
    }

    public static void i(Class clazz, String methodName, Object... args) {
        iMambaLoader.methodEnter(clazz, methodName, args);
    }

    public static void i(Class clazz, String methodName) {
        iMambaLoader.methodEnter(clazz, methodName, null);
    }

    public static void o(Class clazz, String methodName) {
        iMambaLoader.methodExit(clazz, methodName);
    }
}
