package net.ingoh.myagents.lang.il;

public record AnyVariableIdentifier(String id) implements ILNode, VariableIdentifier {
    public AnyVariableIdentifier {
        if (id == null) {
            throw new IllegalArgumentException("Variable id cannot be null");
        }
    }
}