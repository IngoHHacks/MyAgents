package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record MemberAccessExpr(Expr target, MemberIdentifier member) implements ILNode, Expr {
    public MemberAccessExpr {
        if (target == null) {
            throw new IllegalArgumentException("Target cannot be null");
        }
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        var targetValue = target.accept(interpreter);
        var memberValue = member.accept(interpreter);
        return interpreter.memberAccessExpr(targetValue, memberValue);
    }
}