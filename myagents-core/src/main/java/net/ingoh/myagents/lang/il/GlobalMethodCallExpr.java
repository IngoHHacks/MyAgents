package net.ingoh.myagents.lang.il;

public record GlobalMethodCallExpr(IdentifierOrSpecial methodName, ExprList args) implements ILNode, Expr {
    public GlobalMethodCallExpr {
        if (methodName == null) {
            throw new IllegalArgumentException("Method cannot be null");
        }
        if (args == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
    }
}