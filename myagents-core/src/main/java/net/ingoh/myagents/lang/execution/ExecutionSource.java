package net.ingoh.myagents.lang.execution;

import net.ingoh.myagents.lang.symbols.ClassSymbol;

public class ExecutionSource {
    public static ExecutionSource STATIC = new ExecutionSource(null);

    private final Object source;

    public ExecutionSource(Object source) {
        if (source instanceof ClassSymbol classSymbol) {
            this.source = null;
            return;
        }
        this.source = source;
    }

    public Object getSource() {
        return source;
    }
}
