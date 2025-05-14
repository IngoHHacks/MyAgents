package net.ingoh.myagents.lang.il;

public record PackageDecl(NamespaceIdentifier namespace) implements TopLevelDecl {
    public PackageDecl {
        if (namespace == null || namespace.name().isEmpty()) {
            throw new IllegalArgumentException("Namespace cannot be null or empty");
        }
    }

    @Override
    public String toString() {
        return "package " + namespace.name() + ";";
    }
}
