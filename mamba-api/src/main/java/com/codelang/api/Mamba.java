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
        try {
            iMambaLoader.methodEnter(clazz, methodName, args);
        } catch (Exception e) {
            throw new IllegalArgumentException("请先初始化 Mamba.init");
        }
    }

    public static void i(Class clazz, String methodName) {
        try {
            iMambaLoader.methodEnter(clazz, methodName, null);
        } catch (Exception e) {
            throw new IllegalArgumentException("请先初始化 Mamba.init");
        }
    }

    public static void o(Class clazz, String methodName) {
        try {
            iMambaLoader.methodExit(clazz, methodName);
        } catch (Exception e) {
            throw new IllegalArgumentException("请先初始化 Mamba.init");
        }
    }
}
