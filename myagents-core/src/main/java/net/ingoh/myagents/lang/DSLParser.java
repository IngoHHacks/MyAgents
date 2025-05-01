package net.ingoh.myagents.lang;

import net.ingoh.myagents.core.DataType;
import net.ingoh.myagents.lang.il.ILNode;
import net.ingoh.myagents.lang.parsers.*;
import net.ingoh.myagents.lang.visitors.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public class DSLParser {
    public static ILNode parse(String content, Class<?> targetClass) {
        assert content != null : "Content cannot be null";
        assert targetClass != null : "Target class cannot be null";
        int type = DataType.getTypeForClass(targetClass);
        if (type < 0) {
            throw new RuntimeException("No parser found for target class: " + targetClass.getName());
        }
        return parse(content,
                DataType.getLexerForType(type),
                DataType.getParserForType(type),
                DataType.getVisitorForType(type)
        );
    }

    @SuppressWarnings("unchecked")
    public static <L extends Lexer, P extends Parser, V extends AbstractParseTreeVisitor<Object>, C extends ParserRuleContext> ILNode parse(String content, Class<L> lexerClass, Class<P> parserClass, Class<V> visitorClass) {
        assert content != null : "Content cannot be null";
        assert lexerClass != null : "Lexer class cannot be null";
        assert parserClass != null : "Parser class cannot be null";
        assert visitorClass != null : "Visitor class cannot be null";
        try {
            CharStream input = CharStreams.fromString(content);
            L lexer = lexerClass.getConstructor(CharStream.class).newInstance(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            HasRootParser parser = (HasRootParser) parserClass.getConstructor(TokenStream.class).newInstance(tokens);
            HasRootVisitor<Object, C> visitor = (HasRootVisitor<Object, C>) visitorClass.getConstructor().newInstance();
            return (ILNode) visitor.root(parser.root());
        } catch (Exception e) {
            throw new RuntimeException("Error parsing content: " + e.getMessage(), e);
        }
    }
}
