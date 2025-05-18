package net.ingoh.myagents.lang.il;

public record BitAndExpr(
        Expr left,
        Expr right
) implements ILNode, BinaryExpr {
    public BitAndExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}