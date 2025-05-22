package net.ingoh.myagents.lang.il;

public sealed interface PostfixExpr extends Expr, ILNode permits PostfixIncrementExpr, PostfixDecrementExpr {}
