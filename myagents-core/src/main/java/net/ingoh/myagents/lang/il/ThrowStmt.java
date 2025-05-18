package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.il.Expr;
import net.ingoh.myagents.lang.il.Stmt;

public record ThrowStmt(
        Expr exception
) implements ILNode, Stmt {
    public ThrowStmt {
        if (exception == null) {
            throw new IllegalArgumentException("Exception cannot be null");
        }
    }
}
