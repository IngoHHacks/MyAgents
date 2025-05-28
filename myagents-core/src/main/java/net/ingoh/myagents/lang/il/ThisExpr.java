package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record ThisExpr() implements ILNode, PrimaryExpr, IdentifierOrSpecial {
    @Override
    public Object accept(Interpreter interpreter) {
        return interpreter.getExecutionSource().getSource();
    }

    @Override
    public String id() {
        return "this";
    }
}
