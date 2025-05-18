package net.ingoh.myagents.lang.il;

public record AssignExpr(
        Expr variable,
        Expr value
) implements ILNode, AssignmentExpr {
    public AssignExpr {
        if (variable == null || value == null) {
            throw new IllegalArgumentException("Variable and value cannot be null");
        }
    }
}