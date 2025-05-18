package net.ingoh.myagents.lang.il;

public record ShiftRightExpr(
        Expr left,
        Expr right
) implements ILNode, BinaryExpr {
    public ShiftRightExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}