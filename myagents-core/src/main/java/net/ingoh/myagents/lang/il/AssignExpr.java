package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.symbols.VariableSymbol;

public record AssignExpr(
        Expr variable,
        Expr value
) implements ILNode, AssignmentExpr {
    public AssignExpr {
        if (variable == null || value == null) {
            throw new IllegalArgumentException("Variable and value cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        var variableSymbol = (VariableSymbol) variable.accept(interpreter);
        var valueSymbol = value.accept(interpreter);
        if (valueSymbol instanceof VariableSymbol) {
            valueSymbol = ((VariableSymbol) valueSymbol).getValue(interpreter, interpreter.getExecutionSource().getSource());
        }
        variableSymbol.setValue(interpreter, interpreter.getExecutionSource().getSource(), valueSymbol);
        return variableSymbol;
    }
}