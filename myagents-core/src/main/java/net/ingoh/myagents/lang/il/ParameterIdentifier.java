package net.ingoh.myagents.lang.il;

public record ParameterIdentifier(String id) implements ILNode, VariableIdentifier {
    public ParameterIdentifier {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Parameter id cannot be null or empty");
        }
    }
}