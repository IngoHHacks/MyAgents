package net.ingoh.myagents.lang.il;

public record PostfixDecrementExpr(
        Expr expr
) implements ILNode, PostfixExpr {
    public PostfixDecrementExpr {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }
}