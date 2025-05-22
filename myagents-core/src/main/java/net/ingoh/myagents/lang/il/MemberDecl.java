package net.ingoh.myagents.lang.il;

public sealed interface MemberDecl extends ClassBodyDecl, ILNode permits MethodDecl, FieldDecl, ConstructorDecl, ClassDecl {}