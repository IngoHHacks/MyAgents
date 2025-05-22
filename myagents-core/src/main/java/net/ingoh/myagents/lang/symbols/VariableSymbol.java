package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.execution.Interpreter;

public interface VariableSymbol extends Symbol {
    Object getValue(Interpreter interpreter);
    <T> T getValue(Interpreter interpreter, Class<T> type);
    void setValue(Interpreter interpreter, Object value);
    void changeValueBy(Interpreter interpreter, Number value);
}
