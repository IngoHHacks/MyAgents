package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.symbols.VariableSymbol;

public record LogicAndExpr(
        Expr left,
        Expr right
) implements ILNode, BinaryExpr {
    public LogicAndExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        var leftValue = left.accept(interpreter);
        var rightValue = right.accept(interpreter);
        if (leftValue instanceof VariableSymbol) {
            leftValue = ((VariableSymbol) leftValue).getValue(interpreter, interpreter.getExecutionSource().getSource(), Boolean.class);
        }
        if (rightValue instanceof VariableSymbol) {
            rightValue = ((VariableSymbol) rightValue).getValue(interpreter, interpreter.getExecutionSource().getSource(), Boolean.class);
        }
        if (leftValue instanceof Boolean && rightValue instanceof Boolean) {
            return (Boolean) leftValue && (Boolean) rightValue;
        } else {
            throw new IllegalArgumentException("Operands must be of type Boolean");
        }
    }
}