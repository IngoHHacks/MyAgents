package net.ingoh.myagents.lang.il;

public sealed interface VariableIdentifier extends Identifier, ILNode
        permits
        LocalVariableIdentifier,
        ParameterIdentifier,
        FieldIdentifier,
        AnyVariableIdentifier
{}
