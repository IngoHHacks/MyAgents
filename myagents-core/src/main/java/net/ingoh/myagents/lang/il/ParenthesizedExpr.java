package net.ingoh.myagents.lang.il;

public record ParenthesizedExpr(
        Expr expr
) implements ILNode, PrimaryExpr {
    public ParenthesizedExpr {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }
}