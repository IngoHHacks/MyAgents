package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public sealed interface Expr extends ILNode permits PrimaryExpr, ArrayAccessExpr, MemberAccessExpr, ExprMethodCallExpr, ExprThisRefExpr, ExprNewRefExpr, ExprSuperRefExpr, GlobalMethodCallExpr, MethodCallExpr, SwitchExpr, PostfixExpr, PrefixExpr, ObjectCreationExpr, ArrayCreationExpr, BinaryExpr,
    TernaryExpr, AssignmentExpr {

    Object accept(Interpreter interpreter);
}