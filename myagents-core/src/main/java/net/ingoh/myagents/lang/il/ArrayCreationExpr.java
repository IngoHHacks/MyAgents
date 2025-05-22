package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.symbols.VariableSymbol;

import java.util.List;

public record ArrayCreationExpr(
        VariableIdentifier id,
        List<Expr> dimensions
) implements ILNode, Expr {
    public ArrayCreationExpr {
        if (id == null) {
            throw new IllegalArgumentException("Identifier cannot be null");
        }
        if (dimensions == null) {
            throw new IllegalArgumentException("Dimensions cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        throw new UnsupportedOperationException("Array creation is not supported yet");
    }
}