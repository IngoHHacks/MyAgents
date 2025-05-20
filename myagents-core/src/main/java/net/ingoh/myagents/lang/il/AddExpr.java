package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.symbols.VariableSymbol;

public record AddExpr(
        Expr left,
        Expr right
) implements ILNode, BinaryExpr {
    public AddExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        var leftValue = left.accept(interpreter);
        var rightValue = right.accept(interpreter);
        if (leftValue instanceof VariableSymbol) {
            leftValue = ((VariableSymbol) leftValue).getValue(interpreter, Number.class);
        }
        if (rightValue instanceof VariableSymbol) {
            rightValue = ((VariableSymbol) rightValue).getValue(interpreter, Number.class);
        }
        if (leftValue instanceof Number && rightValue instanceof Number) {
            return ((Number) leftValue).doubleValue() + ((Number) rightValue).doubleValue();
        } else {
            return leftValue.toString() + rightValue.toString();
        }
    }
}