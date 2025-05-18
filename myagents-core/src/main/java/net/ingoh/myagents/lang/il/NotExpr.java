package net.ingoh.myagents.lang.il;

public record NotExpr(
        Expr expr
) implements ILNode, PrefixExpr {
    public NotExpr {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }
}