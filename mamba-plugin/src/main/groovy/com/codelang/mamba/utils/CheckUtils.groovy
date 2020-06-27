package com.codelang.mamba.utils

import com.codelang.mamba.config.Config

import java.util.regex.Pattern

class CheckUtils {

    public static ArrayList<String> exclude = []
    public static ArrayList<String> include = []
    public static boolean trackEnable = false


    /**
     * 检查是否是 class 类，并且是否满足 pkg
     * @param className
     * @return
     */
    public static boolean checkClass(String className) {
        return checkClassName(className) && checkInclude(className)
    }

    /**
     * 1、检查 class 名称
     * @param className
     * @return
     */
    public static boolean checkClassName(String className) {
        return className.endsWith(".class") &&
                !className.endsWith(Config.GENERATE_TO_CLASS_FILE_NAME) && // 过滤 api 类
                !checkExclude(className) && // 过滤 exclude
                !className.endsWith("R.class") &&  // 过滤 R 文件
                !Pattern.compile("R\\\$\\w+\\.class").matcher(className).find() &&
                !className.endsWith("BuildConfig.class") &&
                !className.startsWith("org/intellij") &&
                !className.startsWith("org/jetbrains") &&
                !className.startsWith("androidx") && // 过滤 androidx 包
                !className.startsWith("android") &&
                !className.startsWith("support") && // 过滤 support 包
                !className.startsWith("kotlinx") && // 过滤 kotlin 包
                !className.startsWith("kotlin")

    }

    /**
     * @param classPath
     * @return
     */
    private static boolean checkInclude(String classPath) {
        boolean flag = false
        if (include.isEmpty()) {
            return flag
        }
        include.forEach { it ->
            String path = it.replace(".", '/')
            if (classPath.contains(path)) {
                flag = true
            }
        }
        return flag
    }

    /**
     * @param classPath
     * @return
     */
    private static boolean checkExclude(String classPath) {
        boolean flag = false
        if (exclude.isEmpty()) {
            return flag
        }
        exclude.forEach { it ->
            String path = it.replace(".", '/')
            if (classPath.contains(path)) {
                flag = true
            }
        }
        return flag
    }

}