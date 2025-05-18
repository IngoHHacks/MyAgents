package net.ingoh.myagents.lang.il;

public record LabelIdentifier(String id) implements ILNode, Identifier {
    public LabelIdentifier {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Label identifier cannot be null or empty");
        }
    }
}
