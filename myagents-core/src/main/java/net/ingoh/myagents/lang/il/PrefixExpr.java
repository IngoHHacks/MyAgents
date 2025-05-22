package net.ingoh.myagents.lang.il;

public sealed interface PrefixExpr extends Expr, ILNode
        permits PrefixIncrementExpr, PrefixDecrementExpr, UnaryPlusExpr, UnaryMinusExpr, NotExpr, BitwiseNotExpr {}
