package net.ingoh.myagents.lang.il;

public record AddExpr(
        Expr left,
        Expr right
) implements ILNode, BinaryExpr {
    public AddExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}