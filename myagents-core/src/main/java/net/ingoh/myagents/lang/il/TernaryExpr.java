package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record TernaryExpr(
        Expr condition,
        Expr trueExpr,
        Expr falseExpr
) implements ILNode, Expr {
    public TernaryExpr {
        if (condition == null || trueExpr == null || falseExpr == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        var conditionValue = (Boolean) condition.accept(interpreter);
        if (conditionValue) {
            return trueExpr.accept(interpreter);
        } else {
            return falseExpr.accept(interpreter);
        }
    }
}