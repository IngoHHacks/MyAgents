package net.ingoh.myagents.lang.il;

import java.util.List;

public record OverrideBodyDecl(List<ClassBodyDecl> bodyDecls) implements ILNode, TopLevelDecl {
    public OverrideBodyDecl {
        if (bodyDecls == null) {
            throw new IllegalArgumentException("Override body declarations cannot be null or empty");
        }
    }
}
