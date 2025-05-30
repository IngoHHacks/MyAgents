package net.ingoh.myagents.core.basetypes;

import net.ingoh.myagents.lang.execution.Interpreter;

public abstract class MyAgentsClassBase {
    protected final Interpreter interpreter;
    public String __type;

    public MyAgentsClassBase(Interpreter interpreter) {
        this.interpreter = interpreter;
    }
}
