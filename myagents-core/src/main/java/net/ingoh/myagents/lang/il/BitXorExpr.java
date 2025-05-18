package net.ingoh.myagents.lang.il;

public record BitXorExpr(
        Expr left,
        Expr right
) implements ILNode, BinaryExpr {
    public BitXorExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}