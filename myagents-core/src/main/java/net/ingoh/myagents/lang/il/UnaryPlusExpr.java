package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.symbols.VariableSymbol;

public record UnaryPlusExpr(
        Expr expr
) implements ILNode, PrefixExpr {
    public UnaryPlusExpr {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }


    @Override
    public Object accept(Interpreter interpreter) {
        var value = (VariableSymbol) expr.accept(interpreter);
        value.setValue(interpreter, interpreter.getExecutionSource().getSource(), value.getValue(interpreter, long.class));
        return value;
    }
}