package net.ingoh.myagents.lang.il;

public record PlusAssignExpr(
        Expr left,
        Expr right
) implements ILNode, AssignmentExpr {
    public PlusAssignExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}