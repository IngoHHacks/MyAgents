package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.execution.SymbolType;
import net.ingoh.myagents.lang.symbols.VariableSymbol;

public record ForEachStmt(
        VariableIdentifier variable,
        Expr collection,
        Stmt body
) implements ILNode, Stmt {
    public ForEachStmt {
        if (variable == null) {
            throw new IllegalArgumentException("Variable cannot be null");
        }
        if (collection == null) {
            throw new IllegalArgumentException("Collection cannot be null");
        }
        if (body == null) {
            throw new IllegalArgumentException("Body cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        var collectionValue = collection.accept(interpreter);
        if (collectionValue instanceof Iterable<?> iterable) {
            for (var item : iterable) {
                var symbol = (VariableSymbol) interpreter.getSymbol(SymbolType.VARIABLE, variable.id());
                symbol.setValue(interpreter, item);
                body.accept(interpreter);
            }
        } else {
            throw new IllegalArgumentException("Collection must be iterable");
        }
        return null;
    }
}
