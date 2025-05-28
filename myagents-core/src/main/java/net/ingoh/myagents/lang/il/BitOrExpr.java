package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.symbols.VariableSymbol;

public record BitOrExpr(
        Expr left,
        Expr right
) implements ILNode, BinaryExpr {
    public BitOrExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        var leftValue = left.accept(interpreter);
        var rightValue = right.accept(interpreter);
        if (leftValue instanceof VariableSymbol) {
            leftValue = ((VariableSymbol) leftValue).getValue(interpreter, interpreter.getExecutionSource().getSource(), Number.class);
        }
        if (rightValue instanceof VariableSymbol) {
            rightValue = ((VariableSymbol) rightValue).getValue(interpreter, interpreter.getExecutionSource().getSource(), Number.class);
        }
        if (leftValue instanceof Number && rightValue instanceof Number) {
            return ((Number) leftValue).longValue() | ((Number) rightValue).longValue();
        } else {
            throw new IllegalArgumentException("Operands must be numbers");
        }
    }
}