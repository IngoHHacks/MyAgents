package net.ingoh.myagents.lang.il;

public record BitOrExpr(
        Expr left,
        Expr right
) implements ILNode, BinaryExpr {
    public BitOrExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}