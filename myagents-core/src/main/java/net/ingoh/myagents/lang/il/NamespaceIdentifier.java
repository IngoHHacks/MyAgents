package net.ingoh.myagents.lang.il;

public record NamespaceIdentifier(String name) implements ILNode, Identifier {

    public NamespaceIdentifier {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Namespace name cannot be null or empty");
        }
    }
}
