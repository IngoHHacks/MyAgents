package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public sealed interface Identifier extends IdentifierOrSpecial, ILNode permits VariableIdentifier, MemberIdentifier, NamespaceIdentifier, LabelIdentifier, MemberContainerIdentifier {
    Object accept(Interpreter interpreter);
    String id();
}