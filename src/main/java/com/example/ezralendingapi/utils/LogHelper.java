package com.example.ezralendingapi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHelper {
    private static Logger log = LoggerFactory.getLogger(LogHelper.class.getName());

    public static void debug(String msg) {
        log.debug(getLogInfo() + "||" + msg);
    }

    public static void info(String msg) {
        log.info(getLogInfo() + "||" + msg);
    }

    public static void warn(String msg) {
        log.warn(getLogInfo() + "||" + msg);
    }

    public static void error(String msg) {
        log.error(getLogInfo() + "||" + msg);
    }

    public static void debugTrace(Exception e) {
        log.error(getLogInfo() + "||" + org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));

    }

    private static String getLogInfo() {
        try {
            StackTraceElement thisCaller = Thread.currentThread().getStackTrace()[3];
            return thisCaller.getClassName() + "." + thisCaller.getMethodName();
        } catch (Exception e) {
            log.warn("Error finding log, defaulting!");
        }
        return LogHelper.class.getName();
    }
}
