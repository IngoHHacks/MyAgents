package net.ingoh.myagents.lang.il;

public sealed interface ClassBodyDecl extends ILNode permits Block, MethodDecl, FieldDecl, ConstructorDecl, ClassDecl {}