package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record BooleanLiteralExpr(
        boolean value
) implements ILNode, LiteralExpr {
    public BooleanLiteralExpr {
        // No validation needed for primitive literal values
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return value;
    }
}