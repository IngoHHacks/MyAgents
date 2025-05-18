package net.ingoh.myagents.lang.il;

public record ClassRefExpr(
        TypeIdentifier id
) implements ILNode, PrimaryExpr {
    public ClassRefExpr {
        if (id == null) {
            throw new IllegalArgumentException("Identifier cannot be null");
        }
    }
}
