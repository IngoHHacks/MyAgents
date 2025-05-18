package net.ingoh.myagents.lang.il;

public record BitOrAssignExpr(
        Expr left,
        Expr right
) implements ILNode, AssignmentExpr {
    public BitOrAssignExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}