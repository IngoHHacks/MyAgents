package net.ingoh.myagents.lang.symbols;

import java.lang.reflect.Field;

public class FieldSymbolJava implements FieldSymbol {
    private final Field src;

    public FieldSymbolJava(Field src) {
        this.src = src;
    }

    @Override
    public String getName() {
        return src.getName();
    }
}
