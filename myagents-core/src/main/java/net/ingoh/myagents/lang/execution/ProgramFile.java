package net.ingoh.myagents.lang.execution;

import net.ingoh.myagents.lang.il.*;
import net.ingoh.myagents.lang.symbols.*;

import java.util.LinkedList;
import java.util.List;

public class ProgramFile {
    public NamespaceIdentifier namespace = new NamespaceIdentifier("");
    public List<ProgramFile> imports = new LinkedList<>();
    public SymbolTable symbolTable = new SymbolTable();

    private List<Object> instances = new LinkedList<>();

    public static ProgramFile fromClass(Interpreter interpreter, Class<?> cls) {
        ProgramFile programFile = new ProgramFile();
        programFile.namespace = new NamespaceIdentifier(cls.getPackageName());
        programFile.symbolTable = new SymbolTable();
        // We don't need imports because classes aren't run by the interpreter directly
        for (var innerClass : cls.getDeclaredClasses()) {
            programFile.symbolTable.addSymbol(interpreter, SymbolType.NONLOCAL_CLASS, innerClass.getSimpleName(), new ClassSymbolJava(innerClass));
        }
        for (var method : cls.getDeclaredMethods()) {
            programFile.symbolTable.addSymbol(interpreter, SymbolType.METHOD, method.getName(), new MethodSymbolJava(method));
        }
        for (var field : cls.getDeclaredFields()) {
            programFile.symbolTable.addSymbol(interpreter, SymbolType.FIELD, field.getName(), new VariableSymbolJava(field));
        }
        for (var constructor : cls.getDeclaredConstructors()) {
            programFile.symbolTable.addSymbol(interpreter, SymbolType.CONSTRUCTOR, constructor.getName(), new ConstructorSymbolJava(constructor));
        }
        return programFile;
    }

    public Object newInstance(Interpreter interpreter, ConstructorDecl constructor, Object... args) {
        var obj = new Object();
        instances.add(obj);
        var tempExecutionSource = interpreter.getExecutionSource();
        interpreter.setExecutionSource(new ExecutionSource(obj));
        for (BlockStmt stmt :  constructor.body().statements()) {
            if (stmt instanceof ReturnStmt) {
                interpreter.setExecutionSource(tempExecutionSource);
                return obj;
            }
            stmt.accept(interpreter);
        }
        interpreter.setExecutionSource(tempExecutionSource);
        return obj;
    }
}
