package net.ingoh.myagents.lang.il;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.utils.ClassHelper;

public record SuperExpr() implements ILNode, PrimaryExpr, IdentifierOrSpecial {
    @Override
    public Object accept(Interpreter interpreter) {
        return ClassHelper.unproxy(ClassHelper.unproxy(interpreter.getExecutionSource().getSource().getClass()).getSuperclass()).cast(interpreter.getExecutionSource().getSource());
    }

    @Override
    public String id() {
        return "super";
    }
}
