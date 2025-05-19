package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.il.FieldDecl;

public class FieldSymbolImpl implements FieldSymbol {
    private final FieldDecl fieldDecl;

    public FieldSymbolImpl(FieldDecl fieldDecl) {
        this.fieldDecl = fieldDecl;
    }

    @Override
    public String getName() {
        return fieldDecl.id().id();
    }
}
