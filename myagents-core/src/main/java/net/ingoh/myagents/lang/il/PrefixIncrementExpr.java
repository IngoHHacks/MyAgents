package net.ingoh.myagents.lang.il;

public record PrefixIncrementExpr(
        Expr expr
) implements ILNode, PrefixExpr {
    public PrefixIncrementExpr {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }

    @Override
    public String toString() {
        return "++" + expr;
    }
}