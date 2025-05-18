package net.ingoh.myagents.lang.il;

import java.util.List;

public record MethodDecl(MethodIdentifier id, List<ParameterIdentifier> params, Block body) implements ILNode, MemberDecl {
    public MethodDecl {
        if (id == null) {
            throw new IllegalArgumentException("Method id cannot be null");
        }
        if (params == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
    }
}
