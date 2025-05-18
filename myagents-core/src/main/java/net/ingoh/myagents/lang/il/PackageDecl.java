package net.ingoh.myagents.lang.il;

public record PackageDecl(NamespaceIdentifier namespace) implements ILNode, TopLevelDecl {
    public PackageDecl {
        if (namespace == null || namespace.name().isEmpty()) {
            throw new IllegalArgumentException("Namespace cannot be null or empty");
        }
    }
}
