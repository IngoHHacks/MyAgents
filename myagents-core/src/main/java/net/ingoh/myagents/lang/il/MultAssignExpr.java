package net.ingoh.myagents.lang.il;

public record MultAssignExpr(
        Expr left,
        Expr right
) implements ILNode, AssignmentExpr {
    public MultAssignExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }

}