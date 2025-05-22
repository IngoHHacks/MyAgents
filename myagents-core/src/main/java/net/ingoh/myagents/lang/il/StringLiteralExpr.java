package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record StringLiteralExpr(
        String value
) implements ILNode, LiteralExpr {
    public StringLiteralExpr {
        if (value == null) {
            throw new IllegalArgumentException("String value cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return value;
    }
}