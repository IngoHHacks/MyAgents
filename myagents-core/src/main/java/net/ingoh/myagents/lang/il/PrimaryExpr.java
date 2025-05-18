package net.ingoh.myagents.lang.il;

public sealed interface PrimaryExpr extends Expr, ILNode
        permits
        ParenthesizedExpr,
        ThisExpr,
        SuperExpr,
        LiteralExpr,
        RefExpr,
        ClassRefExpr,
        MemberRefExpr
{}