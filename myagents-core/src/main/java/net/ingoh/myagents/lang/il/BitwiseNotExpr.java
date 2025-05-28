package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.symbols.VariableSymbol;

public record BitwiseNotExpr(
        Expr expr
) implements ILNode, PrefixExpr {
    public BitwiseNotExpr {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        var value = (VariableSymbol) expr.accept(interpreter);
        value.setValue(interpreter, interpreter.getExecutionSource().getSource(), ~value.getValue(interpreter, interpreter.getExecutionSource().getSource(), Number.class).longValue());
        return value;
    }
}