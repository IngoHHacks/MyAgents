package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record NullLiteralExpr() implements ILNode, LiteralExpr {
    public NullLiteralExpr {
        // No validation needed for null literal
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return null;
    }
}