package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record ClassRefExpr(
        TypeIdentifier id
) implements ILNode, PrimaryExpr {
    public ClassRefExpr {
        if (id == null) {
            throw new IllegalArgumentException("Identifier cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return id.accept(interpreter);
    }
}
