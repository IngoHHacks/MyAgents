package net.ingoh.myagents.lang.il;

public sealed interface Identifier extends ILNode permits VariableIdentifier, TypeIdentifier, MethodIdentifier, NamespaceIdentifier {}