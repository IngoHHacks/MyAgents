package net.ingoh.myagents.lang.il;

public record GreaterThanOrEqualExpr(
        Expr left,
        Expr right
) implements ILNode, RelationalExpr {
    public GreaterThanOrEqualExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}