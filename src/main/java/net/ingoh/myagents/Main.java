package net.ingoh.myagents;

import net.ingoh.myagents.lang.generators.EnvironmentGenerator;
import net.ingoh.myagents.lang.lexers.EnvironmentLexer;
import net.ingoh.myagents.lang.parsers.EnvironmentParser;
import org.antlr.v4.runtime.*;

public class Main {
    public static void main(String[] args) {
        String input = """
name MyEnv;
agents MyAgent1 MyAgent2;
tick (time) {
    print "Hello, World!";
    return true;
}
        """;
        CharStream charStream = CharStreams.fromString(input);
        EnvironmentLexer lexer = new EnvironmentLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        EnvironmentParser parser = new EnvironmentParser(tokens);
        EnvironmentParser.ProgramContext tree = parser.program();
        if (parser.getNumberOfSyntaxErrors() > 0) {
            System.err.println("Syntax errors detected.");
            return;
        }
        EnvironmentGenerator generator = new EnvironmentGenerator();
        var output = generator.visitProgram(tree);
        if (output == null) {
            System.err.println("Failed to generate environment class.");
            return;
        }
        System.out.println("Generated Environment Class:");
        System.out.println(output);
    }
}
