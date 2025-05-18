package net.ingoh.myagents.lang.il;

public record BitAndAssignExpr(
        Expr left,
        Expr right
) implements ILNode, AssignmentExpr {
    public BitAndAssignExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}