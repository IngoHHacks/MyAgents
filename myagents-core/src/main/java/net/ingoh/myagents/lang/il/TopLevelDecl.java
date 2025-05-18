package net.ingoh.myagents.lang.il;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

public sealed interface TopLevelDecl extends ILNode permits PackageDecl, ImportDecl, OverrideBodyDecl, ClassDecl {}