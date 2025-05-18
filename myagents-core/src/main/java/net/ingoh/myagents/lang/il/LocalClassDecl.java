package net.ingoh.myagents.lang.il;

import java.util.List;

public record LocalClassDecl(ClassDecl classDecl) implements ILNode, BlockStmt {
    public LocalClassDecl {
        if (classDecl == null) {
            throw new IllegalArgumentException("Class declaration cannot be null");
        }
    }
}
