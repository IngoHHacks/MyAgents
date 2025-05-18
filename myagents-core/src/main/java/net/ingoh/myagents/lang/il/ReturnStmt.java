package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.il.Expr;
import net.ingoh.myagents.lang.il.Stmt;

public record ReturnStmt (
        Expr returnValue
) implements ILNode, Stmt {
    public ReturnStmt {
        if (returnValue == null) {
            throw new IllegalArgumentException("Return value cannot be null");
        }
    }
}
