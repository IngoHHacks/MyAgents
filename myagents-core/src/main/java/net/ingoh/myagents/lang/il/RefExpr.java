package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record RefExpr(Identifier id) implements ILNode, PrimaryExpr {
    public RefExpr {
        if (id == null) {
            throw new IllegalArgumentException("Identifier cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return interpreter.getSymbol((String) id.accept(interpreter));
    }
}