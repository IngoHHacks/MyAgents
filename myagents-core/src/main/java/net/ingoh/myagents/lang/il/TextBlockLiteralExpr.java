package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record TextBlockLiteralExpr(
        String value
) implements ILNode, LiteralExpr {
    public TextBlockLiteralExpr {
        if (value == null) {
            throw new IllegalArgumentException("Text block value cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return value;
    }
}