package net.ingoh.myagents.utils;

import net.ingoh.myagents.core.DSLArray;

import java.lang.reflect.Array;

public class ClassHelper {
    public static Class<?> unproxy(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        if (isProxy(cls)) {
            return cls.getSuperclass();
        }
        return cls;
    }

    public static boolean isProxy(Class<?> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        return cls.getSimpleName().contains("ByteBuddy");
    }

    public static Object cast(Object arg, Class<?> param) {
        if (param.isArray()) {
            var arr = makeArray(param.getComponentType(), Array.getLength(arg));
            for (int i = 0; i < Array.getLength(arg); i++) {
                Array.set(arr, i, cast(Array.get(arg, i), param.getComponentType()));
            }
            return arr;
        }
        param = MethodHelper.unboxPrimitive(param);
        if (param == String.class) {
            return arg.toString();
        }
        if (param.isPrimitive()) {
            if (arg == null) {
                throw new IllegalArgumentException("Cannot cast null to primitive type: " + param.getName());
            }
            if (param == int.class || param == long.class || param == double.class || param == float.class ||
                param == short.class || param == byte.class) {
                if (!(arg instanceof Number)) {
                    throw new IllegalArgumentException("Cannot cast " + arg.getClass().getName() + " to primitive type: " + param.getName());
                }
                if (param == int.class) {
                    return ((Number) arg).intValue();
                } else if (param == long.class) {
                    return ((Number) arg).longValue();
                } else if (param == double.class) {
                    return ((Number) arg).doubleValue();
                } else if (param == float.class) {
                    return ((Number) arg).floatValue();
                } else if (param == short.class) {
                    return ((Number) arg).shortValue();
                } else {
                    return ((Number) arg).byteValue();
                }
            }
            if (param == boolean.class) {
                if (!(arg instanceof Boolean)) {
                    throw new IllegalArgumentException("Cannot cast " + arg.getClass().getName() + " to primitive type: " + param.getName());
                }
                return arg;
            }
            if (param == char.class) {
                if (!(arg instanceof Character) && !(arg instanceof String)) {
                    throw new IllegalArgumentException("Cannot cast " + arg.getClass().getName() + " to primitive type: " + param.getName());
                }
                if (arg instanceof String) {
                    if (((String) arg).length() != 1) {
                        throw new IllegalArgumentException("Cannot cast string of length " + ((String) arg).length() + " to char");
                    }
                    return ((String) arg).charAt(0);
                }
                return arg;
            }
        }
        return param.cast(arg);
    }

    private static Object makeArray(Class<?> componentType, int length) {
        componentType = MethodHelper.unboxPrimitive(componentType);
        return Array.newInstance(componentType, length);
    }

    public static Object unarray(Object obj) {
        if (obj instanceof DSLArray da) {
            return da.toArray();
        }
        return obj;
    }
}
