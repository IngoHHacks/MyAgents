package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.il.Stmt;

public record BreakStmt(
        LabelIdentifier label
) implements ILNode, Stmt {
    public BreakStmt {
        if (label == null) {
            throw new IllegalArgumentException("Label cannot be null");
        }
    }
}