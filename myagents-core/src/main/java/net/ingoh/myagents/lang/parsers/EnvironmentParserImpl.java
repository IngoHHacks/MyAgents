package net.ingoh.myagents.lang.parsers;

import net.ingoh.myagents.lang.internal.EnvironmentParser;
import org.antlr.v4.runtime.TokenStream;

public class EnvironmentParserImpl extends EnvironmentParser implements HasRootParser {
    public EnvironmentParserImpl(TokenStream input) {
        super(input);
    }

    @SuppressWarnings("unchecked")
    public EnvironmentParser.ProgramContext root() {
        return this.program();
    }
}