package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.il.Stmt;

public record ContinueStmt(
        LabelIdentifier label
) implements ILNode, Stmt {
    public ContinueStmt {
        if (label == null) {
            throw new IllegalArgumentException("Label cannot be null");
        }
    }
}
