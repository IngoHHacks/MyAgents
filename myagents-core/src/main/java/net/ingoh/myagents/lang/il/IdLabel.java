package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.il.Stmt;

public record IdLabel(
        LabelIdentifier id
) implements ILNode, Stmt {
    public IdLabel {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
    }
}