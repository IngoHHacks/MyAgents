package net.ingoh.myagents.lang.il;

public sealed interface MemberIdentifier extends Identifier, ILNode
        permits
        MethodIdentifier,
        FieldIdentifier,
        TypeIdentifier,
        AnyMemberIdentifier
{
    // Marker interface for identifiers that refer to members (fields, methods, properties, etc.)
}
