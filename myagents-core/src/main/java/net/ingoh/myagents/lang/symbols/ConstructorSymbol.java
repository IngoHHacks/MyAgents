package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.execution.SymbolTable;

import java.util.List;

public interface ConstructorSymbol extends CallableSymbol {
    int getParameterCount();
    SymbolTable getSymbolTable();
    boolean takesInterpreter();
}
