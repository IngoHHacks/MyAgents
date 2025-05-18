package net.ingoh.myagents.lang.il;

public record GreaterThanExpr(
        Expr left,
        Expr right
) implements ILNode, RelationalExpr {
    public GreaterThanExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}