package net.ingoh.myagents.lang.il;

public sealed interface AssignmentExpr extends Expr, ILNode
        permits AssignExpr, PlusAssignExpr, MinusAssignExpr, MultAssignExpr, DivAssignExpr, ModAssignExpr,
        BitAndAssignExpr, BitOrAssignExpr, BitXorAssignExpr, ShiftLeftAssignExpr, ShiftRightAssignExpr, ShiftRightUnsignedAssignExpr {}