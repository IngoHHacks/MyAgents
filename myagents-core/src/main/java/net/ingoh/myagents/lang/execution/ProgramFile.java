package net.ingoh.myagents.lang.execution;

import net.ingoh.myagents.lang.il.MethodDecl;
import net.ingoh.myagents.lang.il.NamespaceIdentifier;
import net.ingoh.myagents.lang.symbols.ClassSymbolJava;
import net.ingoh.myagents.lang.symbols.ConstructorSymbolJava;
import net.ingoh.myagents.lang.symbols.FieldSymbolJava;
import net.ingoh.myagents.lang.symbols.MethodSymbolJava;

import java.util.LinkedList;
import java.util.List;

public class ProgramFile {
    public NamespaceIdentifier namespace = new NamespaceIdentifier("");
    public List<ProgramFile> imports = new LinkedList<>();
    public SymbolTable symbolTable = new SymbolTable();

    public static ProgramFile fromClass(Class<?> cls) {
        ProgramFile programFile = new ProgramFile();
        programFile.namespace = new NamespaceIdentifier(cls.getPackageName());
        programFile.symbolTable = new SymbolTable();
        // We don't need imports because classes aren't run by the interpreter directly
        for (var innerClass : cls.getDeclaredClasses()) {
            programFile.symbolTable.addSymbol(SymbolType.CLASS, innerClass.getSimpleName(), new ClassSymbolJava(innerClass));
        }
        for (var method : cls.getDeclaredMethods()) {
            programFile.symbolTable.addSymbol(SymbolType.METHOD, method.getName(), new MethodSymbolJava(method));
        }
        for (var field : cls.getDeclaredFields()) {
            programFile.symbolTable.addSymbol(SymbolType.FIELD, field.getName(), new FieldSymbolJava(field));
        }
        for (var constructor : cls.getDeclaredConstructors()) {
            programFile.symbolTable.addSymbol(SymbolType.CONSTRUCTOR, constructor.getName(), new ConstructorSymbolJava(constructor));
        }
        return programFile;
    }
}
