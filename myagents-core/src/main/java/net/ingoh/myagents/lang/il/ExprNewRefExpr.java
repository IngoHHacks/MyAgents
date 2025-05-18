package net.ingoh.myagents.lang.il;

public record ExprNewRefExpr(Expr expr) implements ILNode, Expr {
    public ExprNewRefExpr {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }
}