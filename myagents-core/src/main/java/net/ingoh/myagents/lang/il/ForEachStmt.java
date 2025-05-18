package net.ingoh.myagents.lang.il;

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
}
