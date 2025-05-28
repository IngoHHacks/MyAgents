package net.ingoh.myagents.core.basetypes;

import net.ingoh.myagents.lang.execution.Interpreter;

public abstract class MyAgentsClassBase {
    protected final Interpreter interpreter;

    public MyAgentsClassBase(Interpreter interpreter) {
        this.interpreter = interpreter;
    }
}
