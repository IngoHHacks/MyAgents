package net.ingoh.myagents.lang.il;

import java.util.List;

public record OverrideBodyDecl(List<ClassBodyDecl> bodyDecls) implements TopLevelDecl {
    public OverrideBodyDecl {
        if (bodyDecls == null) {
            throw new IllegalArgumentException("Override body declarations cannot be null or empty");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ClassBodyDecl decl : bodyDecls) {
            sb.append(decl).append("\n");
        }
        return sb.toString();
    }
}
