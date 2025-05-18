package net.ingoh.myagents.lang.il;

public record InequalityExpr(
        Expr left,
        Expr right
) implements ILNode, BinaryExpr {
    public InequalityExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}