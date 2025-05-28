package net.ingoh.myagents.utils;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;

public class MethodHelper {
    public static Object[] castArgs(Executable m, Object[] args) {
        if (args == null || args.length == 0) {
            return new Object[0];
        }
        var params = m.getParameterTypes();
        if (params.length != args.length) {
            throw new IllegalArgumentException("Method " + m.getName() + " expects " + params.length + " arguments, but got " + args.length);
        }
        Object[] castedArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null || params[i].isInstance(args[i])) {
                castedArgs[i] = args[i];
            } else {
                castedArgs[i] = ClassHelper.cast(args[i], params[i]);
            }
        }
        return castedArgs;
    }

    public static Class<?> boxPrimitive(Class<?> type) {
        if (type == int.class) return Integer.class;
        if (type == long.class) return Long.class;
        if (type == double.class) return Double.class;
        if (type == float.class) return Float.class;
        if (type == boolean.class) return Boolean.class;
        if (type == char.class) return Character.class;
        if (type == byte.class) return Byte.class;
        if (type == short.class) return Short.class;
        return type;
    }

    public static Class<?> unboxPrimitive(Class<?> type) {
        if (type == Integer.class) return int.class;
        if (type == Long.class) return long.class;
        if (type == Double.class) return double.class;
        if (type == Float.class) return float.class;
        if (type == Boolean.class) return boolean.class;
        if (type == Character.class) return char.class;
        if (type == Byte.class) return byte.class;
        if (type == Short.class) return short.class;
        return type;
    }
}
