package net.ingoh.myagents.lang.il;

public record MinusAssignExpr(
        Expr left,
        Expr right
) implements ILNode, AssignmentExpr {
    public MinusAssignExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}