package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;

public record MemberRefExpr(
        MemberContainerIdentifier ref,
        MemberIdentifier member
) implements ILNode, PrimaryExpr {
    public MemberRefExpr {
        if (ref == null || member == null) {
            throw new IllegalArgumentException("Identifiers cannot be null");
        }
    }

    @Override
    public Object accept(Interpreter interpreter) {
        var refValue = ref.accept(interpreter);
        var memberValue = member.accept(interpreter);
        return interpreter.memberRefExpr(refValue, memberValue);
    }
}