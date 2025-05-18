package net.ingoh.myagents.lang.il;

public record ModExpr(
        Expr left,
        Expr right
) implements ILNode, BinaryExpr {
    public ModExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}