package net.ingoh.myagents.lang.parsers;

import net.ingoh.myagents.lang.internal.AgentParser;
import net.ingoh.myagents.lang.internal.EnvironmentParser;
import org.antlr.v4.runtime.TokenStream;

public class AgentParserImpl extends AgentParser implements HasRootParser {
    public AgentParserImpl(TokenStream input) {
        super(input);
    }

    @SuppressWarnings("unchecked")
    public AgentParser.ProgramContext root() {
        return this.program();
    }
}
