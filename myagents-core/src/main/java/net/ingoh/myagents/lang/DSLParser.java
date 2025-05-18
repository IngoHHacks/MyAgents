package net.ingoh.myagents.lang;

import net.ingoh.myagents.lang.il.ProgramDecl;
import net.ingoh.myagents.lang.internal.MyAgentsLexer;
import net.ingoh.myagents.lang.internal.MyAgentsParser;
import net.ingoh.myagents.lang.visitors.*;
import org.antlr.v4.runtime.*;

public class DSLParser {
    public static ProgramDecl parse(String content) {
        assert content != null : "Content cannot be null";
        try {
            CharStream input = CharStreams.fromString(content);
            MyAgentsLexer lexer = new MyAgentsLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MyAgentsParser parser = new MyAgentsParser(tokens);
            MyAgentsVisitorImpl visitor = new MyAgentsVisitorImpl();
            return visitor.visitProgram(parser.program());
        } catch (Exception e) {
            throw new RuntimeException("Error parsing content: " + e.getMessage(), e);
        }
    }
}