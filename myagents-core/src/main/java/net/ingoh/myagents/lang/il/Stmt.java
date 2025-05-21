package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public sealed interface Stmt extends BlockStmt, ILNode
        permits
        Block,
        IfStmt,
        ForStmt,
        ForEachStmt,
        WhileStmt,
        DoStmt,
        TryStmt,
        SwitchStmt,
        SwitchExpr,
        ReturnStmt,
        ThrowStmt,
        BreakStmt,
        ContinueStmt,
        ExprStmt,
        IdLabel
{
    Object accept(Interpreter interpreter);
}