package net.ingoh.myagents.lang.il;

public record PrefixDecrementExpr(
        Expr expr
) implements ILNode, PrefixExpr {
    public PrefixDecrementExpr {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }
}