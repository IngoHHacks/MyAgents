package net.ingoh.myagents.lang.il;

public record EqualityExpr(
        Expr left,
        Expr right
) implements ILNode, BinaryExpr {
    public EqualityExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}