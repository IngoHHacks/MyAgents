package net.ingoh.myagents.lang.il;

import java.util.List;

public record Block(List<BlockStmt> statements) implements ILNode, ClassBodyDecl, Stmt {
    public Block {
        if (statements == null) {
            throw new IllegalArgumentException("Block statements cannot be null or empty");
        }
    }
}
