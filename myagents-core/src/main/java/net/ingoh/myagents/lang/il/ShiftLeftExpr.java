package net.ingoh.myagents.lang.il;

public record ShiftLeftExpr(
        Expr left,
        Expr right
) implements ILNode, BinaryExpr {
    public ShiftLeftExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}