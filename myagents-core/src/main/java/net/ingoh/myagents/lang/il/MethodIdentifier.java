package net.ingoh.myagents.lang.il;

public record MethodIdentifier(String id) implements ILNode, MemberIdentifier {
    public MethodIdentifier {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Method id cannot be null or empty");
        }
    }
}