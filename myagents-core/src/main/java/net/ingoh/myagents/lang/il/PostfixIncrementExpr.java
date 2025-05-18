package net.ingoh.myagents.lang.il;

public record PostfixIncrementExpr(
        Expr expr
) implements ILNode, PostfixExpr {
    public PostfixIncrementExpr {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }
}