package net.ingoh.myagents.lang.il;

public record ShiftLeftAssignExpr(
        Expr left,
        Expr right
) implements ILNode, AssignmentExpr {
    public ShiftLeftAssignExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}