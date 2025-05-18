package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.il.BlockStmt;
import net.ingoh.myagents.lang.il.Expr;
import net.ingoh.myagents.lang.il.Stmt;

public record DoStmt(
        Expr condition,
        BlockStmt body
) implements ILNode, Stmt {

    public DoStmt {
        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        if (body == null) {
            throw new IllegalArgumentException("Body cannot be null");
        }
    }
}
