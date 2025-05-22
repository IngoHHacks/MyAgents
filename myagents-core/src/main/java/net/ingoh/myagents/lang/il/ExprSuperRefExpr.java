package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record ExprSuperRefExpr(Expr expr, ExprList args) implements ILNode, Expr {
    public ExprSuperRefExpr {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
        if (args == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        return expr.accept(interpreter); // TODO: Implement 'super'
    }
}