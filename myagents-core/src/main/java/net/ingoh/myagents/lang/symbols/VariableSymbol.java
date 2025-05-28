package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.execution.Interpreter;

public interface VariableSymbol extends Symbol {
    Object getValue(Interpreter interpreter, Object object);
    <T> T getValue(Interpreter interpreter, Object object, Class<T> type);
    void setValue(Interpreter interpreter, Object object, Object value);
    void changeValueBy(Interpreter interpreter, Object object, Number value);
}
