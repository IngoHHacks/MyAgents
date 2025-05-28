package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.symbols.VariableSymbol;

public record PrefixIncrementExpr(
        Expr expr
) implements ILNode, PrefixExpr {
    public PrefixIncrementExpr {
        if (expr == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
    }


    @Override
    public Object accept(Interpreter interpreter) {
        var value = (VariableSymbol) expr.accept(interpreter);
        value.changeValueBy(interpreter, interpreter.getExecutionSource().getSource(), 1);
        return value;
    }
}