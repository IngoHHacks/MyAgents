package net.ingoh.myagents.lang.il;

import java.util.List;

public record ConstructorDecl(List<ParameterIdentifier> params, Block body) implements ILNode, MemberDecl {
    public ConstructorDecl {
        if (params == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
    }
}
