package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record PackageDecl(NamespaceIdentifier namespace) implements ILNode, TopLevelDecl {
    public PackageDecl {
        if (namespace == null || namespace.name().isEmpty()) {
            throw new IllegalArgumentException("Namespace cannot be null or empty");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        interpreter.setPackageDecl(namespace);
        return null;
    }
}
