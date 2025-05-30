package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.symbols.VariableSymbol;

public record LogicOrExpr(
        Expr left,
        Expr right
) implements ILNode, BinaryExpr {
    public LogicOrExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        var leftValue = left.accept(interpreter);
        if (leftValue instanceof VariableSymbol) {
            leftValue = ((VariableSymbol) leftValue).getValue(interpreter, interpreter.getExecutionSource().getSource(), Boolean.class);
        }
        if (leftValue instanceof Boolean) {
            // If left is true, short-circuit and return true
            if ((Boolean) leftValue) {
                return true;
            }
        } else {
            throw new IllegalArgumentException("Left operand must be of type Boolean");
        }
        var rightValue = right.accept(interpreter);
        if (rightValue instanceof VariableSymbol) {
            rightValue = ((VariableSymbol) rightValue).getValue(interpreter, interpreter.getExecutionSource().getSource(), Boolean.class);
        }
        if (rightValue instanceof Boolean) {
            return (Boolean) leftValue || (Boolean) rightValue;
        } else {
            throw new IllegalArgumentException("Operands must be of type Boolean");
        }
    }
}