package net.ingoh.myagents.lsp.server.tokens;

import java.util.LinkedList;
import java.util.List;

public class SemanticToken {
    public int line;
    public int startChar;
    public int length;
    public int tokenType;
    public int tokenModifiers;

    public SemanticToken(int line, int startChar, int length, int tokenType, int tokenModifiers) {
        this.line = line;
        this.startChar = startChar;
        this.length = length;
        this.tokenType = tokenType;
        this.tokenModifiers = tokenModifiers;
    }

    public enum Types {
        NONE(-1, "none"),
        NAMESPACE(0, "namespace"),
        TYPE(1, "type"),
        CLASS(2, "class"),
        ENUM(3, "enum"),
        INTERFACE(4, "interface"),
        STRUCT(5, "struct"),
        TYPE_PARAMETER(6, "typeParameter"),
        PARAMETER(7, "parameter"),
        VARIABLE(8, "variable"),
        PROPERTY(9, "property"),
        ENUM_MEMBER(10, "enumMember"),
        EVENT(11, "event"),
        FUNCTION(12, "function"),
        METHOD(13, "method"),
        MACRO(14, "macro"),
        KEYWORD(15, "keyword"),
        MODIFIER(16, "modifier"),
        COMMENT(17, "comment"),
        STRING(18, "string"),
        NUMBER(19, "number"),
        REGEXP(20, "regexp"),
        OPERATOR(21, "operator");

        private final int id;
        private final String name;

        Types(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public TokenType type() {
            return new TokenType(id, 0);
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public static List<String> allNames() {
            List<String> names = new LinkedList<>();
            for (Types type : Types.values()) {
                if (type == NONE) {
                    continue; // Skip NONE type
                }
                names.add(type.getName());
            }
            return names;
        }
    }

    public enum Modifiers {
        NONE(0, "none"),
        DECLARATION(1, "declaration"),
        DEFINITION(1 << 1, "definition"),
        READONLY(1 << 2, "readonly"),
        STATIC(1 << 3, "static"),
        DEPRECATED(1 << 4, "deprecated"),
        ABSTRACT(1 << 5, "abstract"),
        ASYNC(1 << 6, "async"),
        MODIFICATION(1 << 7, "modification"),
        DOCUMENTATION(1 << 8, "documentation"),
        DEFAULT_LIBRARY(1 << 9, "defaultLibrary");

        private final int id;
        private final String name;

        Modifiers(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public static List<String> allNames() {
            List<String> names = new LinkedList<>();
            for (Modifiers modifier : Modifiers.values()) {
                if (modifier == NONE) {
                    continue; // Skip NONE modifier
                }
                names.add(modifier.getName());
            }
            return names;
        }
    }

    public static List<String> types() {
        return Types.allNames();
    }

    public static List<String> modifiers() {
        return Modifiers.allNames();
    }
}