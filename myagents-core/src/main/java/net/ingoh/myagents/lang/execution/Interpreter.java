package net.ingoh.myagents.lang.execution;

import net.ingoh.myagents.lang.il.*;
import net.ingoh.myagents.lang.symbols.*;

import java.util.Dictionary;
import java.util.Hashtable;

public class Interpreter {

    private Dictionary<String, ProgramFile> programFiles = new Hashtable<>();
    private ExecutionSource executionSource;
    private ProgramFile currentProgramFile;

    public Interpreter() {

    }

    public void run(ProgramDecl program) {
        runFile(program);
    }

    private void runFile(ProgramDecl program) {
        currentProgramFile = parseFile(program);
        if (currentProgramFile.symbolTable.hasSymbol(SymbolType.METHOD, "init")) {
            var m = currentProgramFile.symbolTable.resolveMethod("init");
            var code = invoke(ExecutionSource.STATIC, m);
            System.out.println("Program exited with code: " + code);
        }
    }

    private ProgramFile parseFile(ProgramDecl program) {
        currentProgramFile = new ProgramFile();
        for (var decl : program.topLevelDecls()) {
            decl.accept(this);
        }
        programFiles.put(currentProgramFile.namespace.name(), currentProgramFile);
        return currentProgramFile;
    }

    private ProgramFile resolveFile(NamespaceIdentifier namespace) {
        var file = programFiles.get(namespace.name());
        if (file == null) {
            return getBuiltInFile(namespace);
        }
        return file;
    }

    private ProgramFile getBuiltInFile(NamespaceIdentifier namespace) {
        try {
            var cls = Class.forName(namespace.name());
            return ProgramFile.fromClass(cls);
        } catch (Exception e) {
            throw new RuntimeException("Error resolving file: " + namespace.name(), e);
        }
    }

    private Object invoke(ExecutionSource src, MethodSymbol method, Object... args) {
        var prevExecutionSource = executionSource;
        executionSource = src;
        if (method == null) {
            throw new IllegalArgumentException("Method cannot be null");
        }
        var result = method.invoke(this, args);
        executionSource = prevExecutionSource;
        return result;
    }

    ////////////////////////////////////////////

    public void setPackageDecl(NamespaceIdentifier namespace) {
        currentProgramFile.namespace = namespace;
    }

    public void importFrom(ImportDecl importDecl) {
        var prevFile = currentProgramFile;
        if (importDecl.isStatic()) {
            throw new RuntimeException("Static import not supported yet");
        } else {
            currentProgramFile = programFiles.get(importDecl.namespace().name());
            var file = resolveFile(importDecl.namespace());
            programFiles.put(file.namespace.name(), file);
            currentProgramFile = prevFile;
        }
    }

    public void declsFromOverrideBody(OverrideBodyDecl overrideBodyDecl) {
         var decls = overrideBodyDecl.bodyDecls();
         for (var decl : decls) {
             if (!(decl instanceof Block)) {
                 decl.accept(this);
             }
         }
    }

    public void classDecl(ClassDecl classDecl) {
        currentProgramFile.symbolTable.addSymbol(SymbolType.CLASS, classDecl.id().id(), new ClassSymbolImpl(classDecl));
    }

    public void methodDecl(MethodDecl methodDecl) {
        currentProgramFile.symbolTable.addSymbol(SymbolType.METHOD, methodDecl.id().id(), new MethodSymbolImpl(methodDecl));
    }

    public void fieldDecl(FieldDecl fieldDecl) {
        currentProgramFile.symbolTable.addSymbol(SymbolType.FIELD, fieldDecl.id().id(), new FieldSymbolImpl(fieldDecl));
    }

    public void constructorDecl(ConstructorDecl constructorDecl) {
        currentProgramFile.symbolTable.addSymbol(SymbolType.CONSTRUCTOR, String.join(",", constructorDecl.params().stream()
                .map(param -> param.id())
                .toList()), new ConstructorSymbolImpl(constructorDecl));
    }

    public Object invokeMethod(MethodDecl methodDecl, Object[] args) {
        for (var stmt : methodDecl.body().statements()) {
            if (stmt instanceof ReturnStmt) {
                return stmt.accept(this);
            }
            stmt.accept(this);
        }
        return null;
    }

    ////////////////////////////////////////////

    public ExecutionSource getExecutionSource() {
        return executionSource;
    }
}
