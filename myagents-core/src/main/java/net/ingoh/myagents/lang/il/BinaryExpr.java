package net.ingoh.myagents.lang.il;

public sealed interface BinaryExpr extends Expr,
        ILNode permits MultExpr, DivExpr, ModExpr, AddExpr, SubExpr, ShiftLeftExpr, ShiftRightExpr, ShiftRightUnsignedExpr, RelationalExpr, EqualityExpr, InequalityExpr, BitAndExpr, BitXorExpr, BitOrExpr, LogicAndExpr, LogicOrExpr {}