package net.ingoh.myagents.core;

import net.ingoh.myagents.core.basetypes.Agent;
import net.ingoh.myagents.core.basetypes.Environment;

public class DataType {
    public static final int TYPE_UNKNOWN = -1;
    public static final int TYPE_ENVIRONMENT = 0;
    public static final int TYPE_AGENT = 1;

    public static String getTypeName(int type) {
        return switch (type) {
            case TYPE_ENVIRONMENT -> "Environment";
            case TYPE_AGENT -> "Agent";
            default -> "Unknown";
        };
    }

    public static Class<?> getClassForType(int type) {
        return switch (type) {
            case TYPE_ENVIRONMENT -> Environment.class;
            case TYPE_AGENT -> Agent.class;
            default -> null;
        };
    }

    public static int getTypeForClass(Class<?> cls) {
        if (cls == Environment.class) {
            return TYPE_ENVIRONMENT;
        } else if (cls == Agent.class) {
            return TYPE_AGENT;
        }
        return TYPE_UNKNOWN;
    }
}
