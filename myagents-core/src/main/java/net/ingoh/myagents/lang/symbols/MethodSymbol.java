package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.execution.Interpreter;

import java.util.List;

public interface MethodSymbol extends Symbol {
    List<String> getParameterNames();
    Object invoke(Interpreter interpreter, Object... args);
}
