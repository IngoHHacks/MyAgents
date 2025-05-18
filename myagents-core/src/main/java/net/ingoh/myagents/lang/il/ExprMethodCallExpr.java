package net.ingoh.myagents.lang.il;

public record ExprMethodCallExpr(Expr target, MethodIdentifier methodId, Expr... args) implements ILNode, Expr {
    public ExprMethodCallExpr {
        if (target == null) {
            throw new IllegalArgumentException("Target cannot be null");
        }
        if (methodId == null) {
            throw new IllegalArgumentException("Method name cannot be null");
        }
        if (args == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
    }
}