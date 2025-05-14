package net.ingoh.myagents.lang.il;

public sealed interface TopLevelDecl extends ILNode permits PackageDecl, ImportDecl, OverrideBodyDecl, ClassDecl {}