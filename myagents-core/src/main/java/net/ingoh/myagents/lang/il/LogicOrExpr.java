package net.ingoh.myagents.lang.il;

public record LogicOrExpr(
        Expr left,
        Expr right
) implements ILNode, BinaryExpr {
    public LogicOrExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}