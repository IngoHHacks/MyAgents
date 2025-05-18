package net.ingoh.myagents.lang.il;

public record TernaryExpr(
        Expr condition,
        Expr trueExpr,
        Expr falseExpr
) implements ILNode, Expr {
    public TernaryExpr {
        if (condition == null || trueExpr == null || falseExpr == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}