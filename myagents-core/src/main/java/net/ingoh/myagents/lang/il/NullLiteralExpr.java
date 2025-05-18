package net.ingoh.myagents.lang.il;

public record NullLiteralExpr() implements ILNode, LiteralExpr {
    public NullLiteralExpr {
        // No validation needed for null literal
    }
}