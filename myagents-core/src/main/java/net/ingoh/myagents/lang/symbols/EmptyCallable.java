package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.execution.Interpreter;

public class EmptyCallable implements CallableSymbol {
    @Override
    public Object invoke(Interpreter interpreter, Object... args) {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }
}
