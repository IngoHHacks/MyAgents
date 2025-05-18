package net.ingoh.myagents.lang.il;

public record IfStmt(
        Expr condition,
        Stmt thenStmt,
        Stmt elseStmt
) implements ILNode, Stmt {
    public IfStmt {
        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        if (thenStmt == null) {
            throw new IllegalArgumentException("Then statement cannot be null");
        }
    }
}
