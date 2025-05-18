package net.ingoh.myagents.lang.il;

public record ShiftRightUnsignedAssignExpr(
        Expr left,
        Expr right
) implements ILNode, AssignmentExpr {
    public ShiftRightUnsignedAssignExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }

}