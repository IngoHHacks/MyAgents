package net.ingoh.myagents.lang.il;

public record TypeIdentifier(String id) implements ILNode, MemberIdentifier {
    public TypeIdentifier {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Type id cannot be null or empty");
        }
    }
}