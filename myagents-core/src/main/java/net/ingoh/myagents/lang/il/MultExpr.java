package net.ingoh.myagents.lang.il;

public record MultExpr(
        Expr left,
        Expr right
) implements ILNode, BinaryExpr {
    public MultExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Both left and right expressions must be non-null");
        }
    }
}