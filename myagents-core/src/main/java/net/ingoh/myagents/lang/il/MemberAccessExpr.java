package net.ingoh.myagents.lang.il;

public record MemberAccessExpr(Expr target, MemberIdentifier member) implements ILNode, Expr {
    public MemberAccessExpr {
        if (target == null) {
            throw new IllegalArgumentException("Target cannot be null");
        }
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }
    }
}