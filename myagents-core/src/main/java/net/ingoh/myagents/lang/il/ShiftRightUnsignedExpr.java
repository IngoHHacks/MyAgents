package net.ingoh.myagents.lang.il;

public record ShiftRightUnsignedExpr(
        Expr left,
        Expr right
) implements ILNode, BinaryExpr {
    public ShiftRightUnsignedExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}