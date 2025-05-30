package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.il.ClassDecl;

public class ClassSymbolImpl implements ClassSymbol {
    private final Interpreter interpreter;
    private final ClassDecl classDecl;

    public ClassSymbolImpl(Interpreter interpreter, ClassDecl classDecl) {
        if (interpreter == null) {
            throw new IllegalArgumentException("Interpreter cannot be null");
        }
        if (classDecl == null) {
            throw new IllegalArgumentException("Class declaration cannot be null");
        }
        this.interpreter = interpreter;
        this.classDecl = classDecl;
    }

    @Override
    public String getName() {
        return classDecl.id().id();
    }

    @Override
    public String getNameSpace() {
        return classDecl.id().getNamespace().id();
    }

    public ClassDecl getClassDecl() {
        return classDecl;
    }
}
