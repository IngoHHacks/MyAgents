package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.execution.ProgramFile;
import net.ingoh.myagents.lang.il.ClassDecl;

public class ClassSymbolFile implements ClassSymbol {
    private final Interpreter interpreter;
    private final ProgramFile programFile;

    public ClassSymbolFile(Interpreter interpreter, ProgramFile programFile) {
        if (interpreter == null) {
            throw new IllegalArgumentException("Interpreter cannot be null");
        }
        if (programFile == null) {
            throw new IllegalArgumentException("ProgramFile cannot be null");
        }
        this.interpreter = interpreter;
        this.programFile = programFile;
    }

    @Override
    public String getName() {
        return programFile.name;
    }

    @Override
    public String getNameSpace () {
        return programFile.namespace.id();
    }

    public ProgramFile getFile() {
        return programFile;
    }
}