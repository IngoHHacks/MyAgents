package net.ingoh.myagents.lang.il;

public record LessThanExpr(
        Expr left,
        Expr right
) implements ILNode, RelationalExpr {
    public LessThanExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}