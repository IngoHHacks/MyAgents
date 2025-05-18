package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.il.BlockStmt;
import net.ingoh.myagents.lang.il.Stmt;

public record TryStmt(
        BlockStmt tryBlock,
        BlockStmt catchBlock,
        BlockStmt finallyBlock
) implements ILNode, Stmt {

    public TryStmt {
        if (tryBlock == null) {
            throw new IllegalArgumentException("Try block cannot be null");
        }
        if (catchBlock == null && finallyBlock == null) {
            throw new IllegalArgumentException("At least one of catch or finally block must be provided");
        }
    }
}
