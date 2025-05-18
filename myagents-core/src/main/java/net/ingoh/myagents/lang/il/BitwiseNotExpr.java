package net.ingoh.myagents.lang.il;

public record BitwiseNotExpr(
        Expr expr
) implements ILNode, PrefixExpr {
    public BitwiseNotExpr {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }
}