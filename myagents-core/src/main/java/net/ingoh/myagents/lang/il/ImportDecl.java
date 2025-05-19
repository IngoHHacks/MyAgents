package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record ImportDecl(boolean isStatic, NamespaceIdentifier namespace) implements ILNode, TopLevelDecl {
    public ImportDecl {
        if (namespace == null) {
            throw new IllegalArgumentException("NamespaceIdentifier cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        interpreter.importFrom(this);
        return null;
    }
}