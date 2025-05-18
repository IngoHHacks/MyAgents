package net.ingoh.myagents.lang.il;

public record MethodCallExpr(Expr target, IdentifierOrSpecial methodName, ExprList args) implements ILNode, Expr {
    public MethodCallExpr {
        if (target == null) {
            throw new IllegalArgumentException("Target cannot be null");
        }
        if (methodName == null) {
            throw new IllegalArgumentException("Method cannot be null");
        }
    }
}