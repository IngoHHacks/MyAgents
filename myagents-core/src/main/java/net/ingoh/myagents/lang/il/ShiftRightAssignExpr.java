package net.ingoh.myagents.lang.il;

public record ShiftRightAssignExpr(
        Expr left,
        Expr right
) implements ILNode, AssignmentExpr {
    public ShiftRightAssignExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }

}