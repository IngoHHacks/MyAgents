package net.ingoh.myagents.lang.il;

public record ExprThisRefExpr(Expr expr) implements ILNode, Expr {
    public ExprThisRefExpr {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }
}