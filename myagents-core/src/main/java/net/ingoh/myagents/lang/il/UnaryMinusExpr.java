package net.ingoh.myagents.lang.il;

public record UnaryMinusExpr(
        Expr expr
) implements ILNode, PrefixExpr {
    public UnaryMinusExpr {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }
}