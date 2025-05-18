package net.ingoh.myagents.lang.il;

public record AnyMemberIdentifier(String id) implements ILNode, MemberIdentifier {
    public AnyMemberIdentifier {
        if (id == null) {
            throw new IllegalArgumentException("Member id cannot be null");
        }
    }
}