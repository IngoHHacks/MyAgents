package net.ingoh.myagents.lang.il;

public record FloatLiteralExpr(
        float value
) implements ILNode, LiteralExpr {
    public FloatLiteralExpr {
        // No validation needed for primitive literal values
    }
}
