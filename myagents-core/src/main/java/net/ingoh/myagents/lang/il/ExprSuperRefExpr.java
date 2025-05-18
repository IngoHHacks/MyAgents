package net.ingoh.myagents.lang.il;

public record ExprSuperRefExpr(Expr expr, ExprList args) implements ILNode, Expr {
    public ExprSuperRefExpr {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
        if (args == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
    }
}