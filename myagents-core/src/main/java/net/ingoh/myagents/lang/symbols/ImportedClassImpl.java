package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.execution.SymbolType;

public class ImportedClassImpl implements ClassSymbol {

    private final Interpreter interpreter;
    private final String name;
    private final String namespace;

    public ImportedClassImpl(Interpreter interpreter, String name, String namespace) {
        if (interpreter == null) {
            throw new IllegalArgumentException("Interpreter cannot be null");
        }
        if (name == null || namespace == null) {
            throw new IllegalArgumentException("Name and namespace cannot be null");
        }
        this.interpreter = interpreter;
        this.name = name;
        this.namespace = namespace;
    }

    public ImportedClassImpl(Interpreter interpreter, String name) {
        if (interpreter == null) {
            throw new IllegalArgumentException("Interpreter cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        this.interpreter = interpreter;
        var idx = name.lastIndexOf('.');
        if (idx < 0) {
            this.name = name;
            this.namespace = "";
        } else {
            this.name = name.substring(idx + 1);
            this.namespace = name.substring(0, idx);
        }
    }

    @Override
    public String getNameSpace() {
        return namespace;
    }

    @Override
    public String getName() {
        return name;
    }
}
