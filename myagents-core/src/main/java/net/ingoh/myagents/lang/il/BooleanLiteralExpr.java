package net.ingoh.myagents.lang.il;

public record BooleanLiteralExpr(
        boolean value
) implements ILNode, LiteralExpr {
    public BooleanLiteralExpr {
        // No validation needed for primitive literal values
    }
}