package net.ingoh.myagents.lang.il;

public record BitXorAssignExpr(
        Expr left,
        Expr right
) implements ILNode, AssignmentExpr {
    public BitXorAssignExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }
}