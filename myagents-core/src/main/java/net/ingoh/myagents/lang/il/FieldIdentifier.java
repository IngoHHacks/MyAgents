package net.ingoh.myagents.lang.il;

public record FieldIdentifier(String id) implements ILNode, VariableIdentifier, MemberIdentifier {
    public FieldIdentifier {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Variable id cannot be null or empty");
        }
    }
}