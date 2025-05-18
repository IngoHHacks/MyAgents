package net.ingoh.myagents.lang.il;

public record MemberContainerIdentifier(String id) implements ILNode, Identifier {
    public MemberContainerIdentifier {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
    }
}
