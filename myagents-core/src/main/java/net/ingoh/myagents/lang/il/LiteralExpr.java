package net.ingoh.myagents.lang.il;

public sealed interface LiteralExpr extends PrimaryExpr, ILNode permits
        BooleanLiteralExpr,
        CharLiteralExpr,
        StringLiteralExpr,
        IntLiteralExpr,
        FloatLiteralExpr,
        NullLiteralExpr,
        TextBlockLiteralExpr
{}