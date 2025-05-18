package net.ingoh.myagents.lang.il;

public record MemberRefExpr(
        MemberContainerIdentifier ref,
        MemberIdentifier member
) implements ILNode, PrimaryExpr {
    public MemberRefExpr {
        if (ref == null || member == null) {
            throw new IllegalArgumentException("Identifiers cannot be null");
        }
    }
}