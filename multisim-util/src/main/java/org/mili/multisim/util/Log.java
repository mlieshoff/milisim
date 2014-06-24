package org.mili.multisim.util;

/**
 * @author
 */
public class Log {

    private static final boolean LOG = true;

    public static void trace(Object object, String method, String message, Object... args) {
        log("TRACE", object, method, message, args);
    }

    public static void debug(Object object, String method, String message, Object... args) {
        log("DEBUG", object, method, message, args);
    }

    public static void info(Object object, String method, String message, Object... args) {
        log("INFO", object, method, message, args);
    }

    public static void error(Object object, String method, String message, Object... args) {
        log("ERROR", object, method, message, args);
    }

    public static void warn(Object object, String method, String message, Object... args) {
        log("WARN", object, method, message, args);
    }


    private static void log(String level, Object object, String method, String message, Object... args) {
        if (LOG) {
            System.out.println(String.format("[%s] %s- [%s]: %s", level, object, method, String.format(message, args)));
        }
    }

}
