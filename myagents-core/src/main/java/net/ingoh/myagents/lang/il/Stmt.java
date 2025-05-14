package net.ingoh.myagents.lang.il;

public sealed interface Stmt extends BlockStmt
    permits
        BlockLabel,
        IfStmt,
        ForStmt,
        ForEachStmt,
        WhileStmt,
        DoStmt,
        TryStmt,
        SwitchStmt,
        SwitchExprStatement,
        ReturnStmt,
        ThrowStmt,
        BreakStmt,
        ContinueStmt,
        ExprStmt,
        IdLabel
{}