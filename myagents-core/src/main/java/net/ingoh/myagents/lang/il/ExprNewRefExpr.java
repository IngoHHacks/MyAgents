package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record ExprNewRefExpr(Expr expr) implements ILNode, Expr {
    public ExprNewRefExpr {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return expr.accept(interpreter); // TODO: Implement 'new'
    }
}