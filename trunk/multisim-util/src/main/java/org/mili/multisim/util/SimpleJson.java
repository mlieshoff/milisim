package org.mili.multisim.util;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.List;

/**
 * @author
 */
public class SimpleJson {

    public static String appendString(String key, Object value) {
        return String.format("    \"%s\":\"%s\"", key, value);
    }

    public static String appendNumber(String key, int value) {
        return String.format("    \"%s\":%s", key, value);
    }

    public static <T> String appendValues(String key, List<T> values) {
        StringBuilder s = new StringBuilder();
        for (int i = 0, n = values.size(); i < n; i ++) {
            s.append(values.get(i));
            if (i < n - 1) {
                s.append(",");
            }
        }
        return String.format("    \"%s\":[%s]", key, s);
    }

    public static String build(Part<?>... parts) {
        return build(true, parts);
    }

    public static String build(boolean enclose, Part<?>... parts) {
        StringBuilder s = new StringBuilder();
        if (enclose) {
            s.append("{");
        }
        for (int i = 0; i < parts.length; i ++) {
            Part part = parts[i];
            String name = "\"" + part.name + "\"";

            s.append(name);
            s.append(":");
            if (part.subs != null && part.subs.length > 0) {
                if (part.type == Array.class) {
                    s.append("[");
                    s.append(build(part.subs));
                    s.append("]");
                } else {
                    s.append(build(part.subs));
                }
            } else {
                String value;

                if (part.transformer != null) {
                    value = part.transformer.transform(part.value);
                } else {
                    value = part.value != null ? part.value.toString() : "";

                    if (part.value == null && part.type == Object.class) {
                        value = "null";
                    }

                }

                if (part.type == String.class || part.type == Date.class || part.type == Enum.class) {
                    value = "\"" + value + "\"";
                }
                s.append(value);
            }
            if (i < parts.length - 1) {
                s.append(",");
            }
        }
        if (enclose) {
            s.append("}");
        }
        return s.toString();
    }

    public static Part bool(String name, boolean value) {
        return new Part<>(name, Boolean.class, value, null);
    }

    public static Part integer(String name, int value) {
        return new Part<>(name, Integer.class, value, null);
    }

    public static Part string(String name, String value) {
        return new Part<>(name, String.class, value, null);
    }

    public static Part nil(String name) {
        return new Part<>(name, Object.class, null, null);
    }

    public static <T> Part part(String name, Part<?>... subs) {
        return new Part<>(name, null, null, null, subs);
    }

    public static <T> Part part(String name, Class<T> type, T value, Part<?>... subs) {
        return new Part<>(name, type, value, null, subs);
    }

    public static <T> Part part(String name, Class<T> type, T value, Transformer<T> transformer, Part<?>... subs) {
        return new Part<>(name, type, value, transformer, subs);
    }

    public static <T> Part array(String name, Part<?>... array) {
        return new Part<>(name, Array.class, null, null, array);
    }

    public static class Part<T> {

        private String name;
        private Class<T> type;
        private Object value;
        private Transformer<T> transformer;
        private Part<?>[] subs;

        private <X extends T> Part(String name, Class<T> type, T value, Transformer<T> transformer, Part<?>... subs) {
            this.name = name;
            this.type = type;
            this.value = value;
            this.transformer = transformer;
            this.subs = subs;
        }

    }

    public static interface Transformer<T> {
        String transform(T value);
    }

}
