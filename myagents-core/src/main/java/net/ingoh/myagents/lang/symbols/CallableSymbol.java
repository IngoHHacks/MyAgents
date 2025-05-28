package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.execution.Interpreter;

public interface CallableSymbol extends Symbol {
    Object invoke(Interpreter interpreter, Object obj, Object... args);
}
