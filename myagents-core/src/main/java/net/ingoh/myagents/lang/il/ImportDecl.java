package net.ingoh.myagents.lang.il;

public record ImportDecl(boolean isStatic, NamespaceIdentifier namespace) implements TopLevelDecl {
    public ImportDecl {
        if (namespace == null) {
            throw new IllegalArgumentException("NamespaceIdentifier cannot be null");
        }
    }

    @Override
    public String toString() {
        return (isStatic ? "import static " : "import ") + namespace + ";";
    }
}