package net.ingoh.myagents.lang.il;

public sealed interface RelationalExpr extends BinaryExpr, ILNode
        permits LessThanExpr, GreaterThanExpr, LessThanOrEqualExpr, GreaterThanOrEqualExpr {}