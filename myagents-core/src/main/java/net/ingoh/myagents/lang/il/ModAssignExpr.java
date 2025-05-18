package net.ingoh.myagents.lang.il;

public record ModAssignExpr(
        Expr left,
        Expr right
) implements ILNode, AssignmentExpr {
    public ModAssignExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}