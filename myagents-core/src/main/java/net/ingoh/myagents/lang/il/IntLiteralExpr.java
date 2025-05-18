package net.ingoh.myagents.lang.il;

public record IntLiteralExpr(int value) implements ILNode, LiteralExpr {
    public IntLiteralExpr {
        // No validation needed for primitive literal values
    }
}
