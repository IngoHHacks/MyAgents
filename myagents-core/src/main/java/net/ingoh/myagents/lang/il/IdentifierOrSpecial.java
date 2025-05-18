package net.ingoh.myagents.lang.il;

public sealed interface IdentifierOrSpecial extends ILNode
        permits Identifier, ThisExpr, SuperExpr {}
