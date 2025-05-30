package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.symbols.VariableSymbol;

public record UnaryMinusExpr(
        Expr expr
) implements ILNode, PrefixExpr {
    public UnaryMinusExpr {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        var value = expr.accept(interpreter);
        if (value instanceof VariableSymbol variable) {
            if (variable.getValue(interpreter, interpreter.getExecutionSource().getSource()) instanceof Long || variable.getValue(interpreter, interpreter.getExecutionSource().getSource()) instanceof Integer) {
                variable.setValue(interpreter, interpreter.getExecutionSource().getSource(), -variable.getValue(interpreter, interpreter.getExecutionSource().getSource(), long.class));
            } else {
                variable.setValue(interpreter, interpreter.getExecutionSource().getSource(), -variable.getValue(interpreter, interpreter.getExecutionSource().getSource(), double.class));
            }
            return variable;
        }
        if (value instanceof Long || value instanceof Integer) {
            return -((Number) value).longValue();
        } else if (value instanceof Double) {
            return -((Double) value);
        } else if (value instanceof Float) {
            return -((Float) value);
        } else {
            throw new IllegalArgumentException("Unsupported type for unary minus: " + value.getClass().getName());
        }
    }
}