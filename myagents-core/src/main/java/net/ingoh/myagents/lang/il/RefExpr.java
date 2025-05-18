package net.ingoh.myagents.lang.il;

public record RefExpr(Identifier id) implements ILNode, PrimaryExpr {
    public RefExpr {
        if (id == null) {
            throw new IllegalArgumentException("Identifier cannot be null");
        }
    }
}