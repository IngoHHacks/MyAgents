package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record ParenthesizedExpr(
        Expr expr
) implements ILNode, PrimaryExpr {
    public ParenthesizedExpr {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return expr.accept(interpreter);
    }
}