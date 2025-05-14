package net.ingoh.myagents.lang.il;

import java.util.List;

public record ProgramDecl(List<TopLevelDecl> topLevelDecls) implements ILNode {
    public ProgramDecl {
        if (topLevelDecls == null) {
            throw new IllegalArgumentException("Top level declarations cannot be null");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (TopLevelDecl decl : topLevelDecls) {
            sb.append(decl).append("\n");
        }
        return sb.toString();
    }
}
