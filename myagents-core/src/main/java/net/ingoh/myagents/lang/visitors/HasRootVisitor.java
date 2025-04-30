package net.ingoh.myagents.lang.visitors;

import org.antlr.v4.runtime.ParserRuleContext;

public interface HasRootVisitor<T, C extends ParserRuleContext> {
    T root(C ctx);
}
