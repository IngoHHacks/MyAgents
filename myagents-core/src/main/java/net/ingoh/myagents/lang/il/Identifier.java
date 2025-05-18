package net.ingoh.myagents.lang.il;

public sealed interface Identifier extends IdentifierOrSpecial, ILNode permits VariableIdentifier, MemberIdentifier, NamespaceIdentifier, LabelIdentifier, MemberContainerIdentifier {}