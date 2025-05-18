package net.ingoh.myagents.lang.il;

public record LessThanOrEqualExpr(
        Expr left,
        Expr right
) implements ILNode, RelationalExpr {
    public LessThanOrEqualExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}