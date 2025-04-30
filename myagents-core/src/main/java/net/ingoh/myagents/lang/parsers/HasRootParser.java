package net.ingoh.myagents.lang.parsers;

import org.antlr.v4.runtime.ParserRuleContext;

public interface HasRootParser {

    <C extends ParserRuleContext> C root();
}
