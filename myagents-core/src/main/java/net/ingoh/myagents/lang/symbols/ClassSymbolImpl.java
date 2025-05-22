package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.il.ClassDecl;

public class ClassSymbolImpl implements ClassSymbol {
    private final ClassDecl classDecl;

    public ClassSymbolImpl(ClassDecl classDecl) {
        this.classDecl = classDecl;
    }

    @Override
    public String getName() {
        return classDecl.id().id();
    }

    @Override
    public String getNamepsace() {
        return classDecl.id().getNamespace().id();
    }
}
