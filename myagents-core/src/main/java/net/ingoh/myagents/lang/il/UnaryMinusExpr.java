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
        var value = (VariableSymbol) expr.accept(interpreter);
        if (value.getValue(interpreter, interpreter.getExecutionSource().getSource()) instanceof Long || value.getValue(interpreter, interpreter.getExecutionSource().getSource()) instanceof Integer) {
            value.setValue(interpreter, interpreter.getExecutionSource().getSource(), -value.getValue(interpreter, interpreter.getExecutionSource().getSource(), long.class));
        } else {
            value.setValue(interpreter, interpreter.getExecutionSource().getSource(), -value.getValue(interpreter, interpreter.getExecutionSource().getSource(), double.class));
        }
        return value;
    }
}