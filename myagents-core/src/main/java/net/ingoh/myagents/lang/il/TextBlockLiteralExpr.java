package net.ingoh.myagents.lang.il;

public record TextBlockLiteralExpr(
        String value
) implements ILNode, LiteralExpr {
    public TextBlockLiteralExpr {
        if (value == null) {
            throw new IllegalArgumentException("Text block value cannot be null");
        }
    }
}