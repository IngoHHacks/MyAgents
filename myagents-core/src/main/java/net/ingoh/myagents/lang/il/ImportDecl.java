package net.ingoh.myagents.lang.il;

public record ImportDecl(boolean isStatic, NamespaceIdentifier namespace) implements ILNode, TopLevelDecl {
    public ImportDecl {
        if (namespace == null) {
            throw new IllegalArgumentException("NamespaceIdentifier cannot be null");
        }
    }
}