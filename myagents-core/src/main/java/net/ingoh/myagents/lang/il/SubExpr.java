package net.ingoh.myagents.lang.il;

public record SubExpr(
        Expr left,
        Expr right
) implements ILNode, BinaryExpr {
    public SubExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}