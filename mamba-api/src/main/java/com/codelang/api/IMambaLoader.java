package com.codelang.api;

public interface IMambaLoader {
    /**
     * 进入方法
     *
     * @param clazz
     * @param methodName
     */
    void methodEnter(Class clazz, String methodName, Object[] args);

    /**
     * 方法结束
     *
     * @param clazz
     * @param methodName
     */
    void methodExit(Class clazz, String methodName);
}
