package net.ingoh.myagents.lang.il;

public record DivAssignExpr(
        Expr left,
        Expr right
) implements ILNode, AssignmentExpr {
    public DivAssignExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}