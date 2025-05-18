package net.ingoh.myagents.lang.il;

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
{}