package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.symbols.VariableSymbol;

public record BitAndAssignExpr(
        Expr left,
        Expr right
) implements ILNode, AssignmentExpr {
    public BitAndAssignExpr {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        var variableSymbol = (VariableSymbol) left.accept(interpreter);
        var valueSymbol = right.accept(interpreter);
        if (valueSymbol instanceof VariableSymbol) {
            valueSymbol = ((VariableSymbol) valueSymbol).getValue(interpreter, interpreter.getExecutionSource().getSource(), Number.class).longValue();
        }
        variableSymbol.setValue(interpreter, interpreter.getExecutionSource().getSource(), variableSymbol.getValue(interpreter, interpreter.getExecutionSource().getSource(), Number.class).longValue() & (long) valueSymbol);
        return variableSymbol;
    }
}