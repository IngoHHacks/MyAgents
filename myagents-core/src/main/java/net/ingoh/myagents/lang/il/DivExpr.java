package net.ingoh.myagents.lang.il;

public record DivExpr(
        Expr left,
        Expr right
) implements ILNode, BinaryExpr {
    public DivExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}