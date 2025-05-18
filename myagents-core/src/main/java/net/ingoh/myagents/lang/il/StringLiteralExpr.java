package net.ingoh.myagents.lang.il;

public record StringLiteralExpr(
        String value
) implements ILNode, LiteralExpr {
    public StringLiteralExpr {
        if (value == null) {
            throw new IllegalArgumentException("String value cannot be null");
        }
    }
}