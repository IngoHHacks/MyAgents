package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record BitXorExpr(
        Expr left,
        Expr right
) implements ILNode, BinaryExpr {
    public BitXorExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        var leftValue = (Number) left.accept(interpreter);
        var rightValue = (Number) right.accept(interpreter);
        return leftValue.intValue() ^ rightValue.intValue();
    }
}