package net.ingoh.myagents.lang.il;

import java.util.List;

public record Block(List<BlockStmt> statements) implements ClassBodyDecl {
    public Block {
        if (statements == null) {
            throw new IllegalArgumentException("Block statements cannot be null or empty");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (BlockStmt stmt : statements) {
            sb.append(stmt).append("\n");
        }
        return sb.toString();
    }
}
