package net.ingoh.myagents.core;

import net.ingoh.myagents.lang.internal.AgentBaseVisitor;
import net.ingoh.myagents.lang.lexers.AgentLexerImpl;
import net.ingoh.myagents.lang.lexers.EnvironmentLexerImpl;
import net.ingoh.myagents.lang.parsers.AgentParserImpl;
import net.ingoh.myagents.lang.parsers.EnvironmentParserImpl;
import net.ingoh.myagents.lang.visitors.AgentVisitorImpl;
import net.ingoh.myagents.lang.visitors.EnvironmentVisitorImpl;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public class DataType {
    public static final int TYPE_UNKNOWN = -1;
    public static final int TYPE_ENVIRONMENT = 0;
    public static final int TYPE_AGENT = 1;

    public static Class<? extends Lexer> getLexerForType(int type) {
        return switch (type) {
            case TYPE_ENVIRONMENT -> EnvironmentLexerImpl.class;
            case TYPE_AGENT -> AgentLexerImpl.class;
            default -> null;
        };
    }

    public static Class<? extends Parser> getParserForType(int type) {
        return switch (type) {
            case TYPE_ENVIRONMENT -> EnvironmentParserImpl.class;
            case TYPE_AGENT -> AgentParserImpl.class;
            default -> null;
        };
    }

    public static Class<? extends AbstractParseTreeVisitor<Object>> getVisitorForType(int type) {
        return switch (type) {
            case TYPE_ENVIRONMENT -> EnvironmentVisitorImpl.class;
            case TYPE_AGENT -> AgentVisitorImpl.class;
            default -> null;
        };
    }

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
