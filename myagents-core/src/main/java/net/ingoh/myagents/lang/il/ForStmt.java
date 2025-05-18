package net.ingoh.myagents.lang.il;

import java.util.List;

public record ForStmt(
        ForInit init,
        Expr condition,
        ExprList update,
        Stmt body
) implements ILNode, Stmt {
    public ForStmt {
        if (init == null) {
            throw new IllegalArgumentException("Initialization cannot be null");
        }
        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        if (update == null) {
            throw new IllegalArgumentException("Update cannot be null");
        }
        if (body == null) {
            throw new IllegalArgumentException("Body cannot be null");
        }
    }
}
