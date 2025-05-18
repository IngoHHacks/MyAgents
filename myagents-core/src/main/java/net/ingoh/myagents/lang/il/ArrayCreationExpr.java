package net.ingoh.myagents.lang.il;

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
}