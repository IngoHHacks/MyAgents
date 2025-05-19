package net.ingoh.myagents.lang.il;

public record NamespaceIdentifier(String name) implements ILNode, Identifier {

    public NamespaceIdentifier {
        if (name == null) {
            throw new IllegalArgumentException("Namespace id cannot be null");
        }
    }
}
