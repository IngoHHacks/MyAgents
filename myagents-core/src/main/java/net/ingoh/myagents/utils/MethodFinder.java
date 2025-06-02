package net.ingoh.myagents.utils;

import net.ingoh.myagents.core.DSLArray;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.stream.Stream;

public class MethodFinder {
    public static Method findCompatibleMethod(Class<?> cls, String methodName, Object... args) {
        return findCompatibleMethod(cls, methodName,
                args != null ? Stream.of(args)
                        .map(Object::getClass)
                        .map(ClassHelper::unproxy)
                        .toArray(Class[]::new) : new Class[0]);
    }

    public static Method findCompatibleMethod(Class<?> cls, String methodName, Class<?>... classList) {
        Method[] methods = cls.getMethods();
        for (Method method : methods) {
            if (!method.getName().equals(methodName)) continue;

            Class<?>[] paramTypes = method.getParameterTypes();
            if (paramTypes.length != classList.length) continue;

            boolean compatible = true;
            for (int i = 0; i < paramTypes.length; i++) {
                Class<?> argType = classList[i] == null ? null : classList[i];
                Class<?> paramType = paramTypes[i];

                if (paramType.isPrimitive()) {
                    paramType = MethodHelper.boxPrimitive(paramType);
                }
                if (argType != null && !paramType.isAssignableFrom(argType) && !isCastable(paramType, argType)) {
                    compatible = false;
                    break;
                }
            }
            if (compatible) return method;
        }
        return null;
    }

    public static Constructor<?> findCompatibleConstructor(Class<?> cls, Object... args) {
        return findCompatibleConstructor(cls,
                args != null ? Stream.of(args)
                        .map(Object::getClass)
                        .map(ClassHelper::unproxy)
                        .toArray(Class[]::new) : new Class[0]);
    }

    public static Constructor<?> findCompatibleConstructor(Class<?> cls, Class... clsList) {
        Constructor<?>[] constructors = cls.getConstructors();
        for (Constructor<?> constructor : constructors) {
            Class<?>[] paramTypes = constructor.getParameterTypes();
            if (paramTypes.length != clsList.length) continue;

            boolean compatible = true;
            for (int i = 0; i < paramTypes.length; i++) {
                Class<?> paramType = paramTypes[i];
                Class<?> argType = clsList[i];

                if (paramType.isPrimitive()) {
                    paramType = MethodHelper.boxPrimitive(paramType);
                }
                if (!paramType.isAssignableFrom(argType) && !isCastable(paramType, argType)) {
                    compatible = false;
                    break;
                }
            }
            if (compatible) return constructor;
        }
        return null;
    }

    private static boolean isCastable(Class<?> target, Class<?> source) {
        target = MethodHelper.boxPrimitive(target);
        source = MethodHelper.boxPrimitive(source);
        if (source.isArray()) {
            return target.isArray();
        }
        if (target.isAssignableFrom(source)) return true;
        if (target == Integer.class ||
            target == Long.class ||
            target == Double.class ||
            target == Float.class ||
            target == Byte.class ||
            target == Short.class) {
            return source == Integer.class ||
                   source == Long.class ||
                   source == Double.class ||
                   source == Float.class ||
                   source == Byte.class ||
                   source == Short.class;
        }
        if (target == Character.class) {
            return source == Character.class || source == String.class;
        }
        if (target == Boolean.class) {
            return source == Boolean.class;
        }
        return false;
    }
}