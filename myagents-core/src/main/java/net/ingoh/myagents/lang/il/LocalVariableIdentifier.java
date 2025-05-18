package net.ingoh.myagents.lang.il;

public record LocalVariableIdentifier(String id) implements ILNode, VariableIdentifier {
    public LocalVariableIdentifier {
        if (id == null) {
            throw new IllegalArgumentException("Variable id cannot be null");
        }
    }
}