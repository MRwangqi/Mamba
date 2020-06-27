package com.codelang.mamba.utils

import org.gradle.api.Project

class Logger {
    static org.gradle.api.logging.Logger logger

    static void make(Project project) {
        logger = project.getLogger()
    }

    static void i(String info) {
        if (null != info && null != logger) {
            logger.info("Logger:: >>> " + info)
        }
    }

    static void e(String error) {
        if (null != error && null != logger) {
            logger.error("Logger:: >>> " + error)
        }
    }

    static void w(String warning) {
        if (null != warning && null != logger) {
            logger.warn("Logger:: >>> " + warning)
        }
    }
}
