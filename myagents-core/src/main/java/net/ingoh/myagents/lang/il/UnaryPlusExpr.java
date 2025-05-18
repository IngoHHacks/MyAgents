package net.ingoh.myagents.lang.il;

public record UnaryPlusExpr(
        Expr expr
) implements ILNode, PrefixExpr {
    public UnaryPlusExpr {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }
}