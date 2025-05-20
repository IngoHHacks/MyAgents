package net.ingoh.myagents.lang.execution;

import net.ingoh.myagents.lang.il.FieldDecl;
import net.ingoh.myagents.lang.symbols.*;

import java.io.PrintStream;
import java.util.Dictionary;
import java.util.Hashtable;

public class SymbolTable {

    public Dictionary<String, SymbolType> symbols = new Hashtable<>();
    public Dictionary<String, Symbol> declarations = new Hashtable<>();

    public Dictionary<String, ClassSymbol> classes = new Hashtable<>();
    public Dictionary<String, MethodSymbol> methods = new Hashtable<>();
    public Dictionary<String, VariableSymbol> fields = new Hashtable<>();
    public Dictionary<String, ConstructorSymbol> constructors = new Hashtable<>();

    public SymbolTable() {}

    public void addSymbol(SymbolType symbolType, String simpleName, Symbol decl) {
        symbols.put(simpleName, symbolType);
        declarations.put(simpleName, decl);
        switch (symbolType) {
            case CLASS:
            case LOCAL_CLASS:
                classes.put(simpleName, (ClassSymbol) decl);
                break;
            case METHOD:
                methods.put(simpleName, (MethodSymbol) decl);
                break;
            case FIELD:
            case PARAMETER:
            case LOCAL_VARIABLE:
                fields.put(simpleName, (VariableSymbol) decl);
                break;
            case CONSTRUCTOR:
                constructors.put(simpleName, (ConstructorSymbol) decl);
                break;
        }
    }

    public boolean hasSymbol(SymbolType symbolType, String simpleName) {
        if (symbolType == null || simpleName == null) {
            throw new IllegalArgumentException("Symbol type and id cannot be null");
        }
        return symbols.get(simpleName) == symbolType;
    }

    public SymbolType getSymbolType(SymbolType symbolType, String simpleName) {
        return symbols.get(simpleName);
    }

    public Symbol getSymbol(SymbolType symbolType, String simpleName) {
        return declarations.get(simpleName);
    }

    public MethodSymbol resolveMethod(String methodName) {
        if (methodName == null) {
            throw new IllegalArgumentException("Method id cannot be null");
        }
        if (!hasSymbol(SymbolType.METHOD, methodName)) {
            return resolveSpecialMethod(methodName);
        }
        return (MethodSymbol) declarations.get(methodName);
    }

    private MethodSymbol resolveSpecialMethod(String methodName) {
        try {
            if (methodName.equals("print")) {
                return new MethodSymbolJava(PrintStream.class.getMethod("println", Object.class), System.out);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Error resolving special method: " + methodName, e);
        }
        throw new IllegalArgumentException("Method " + methodName + " is not defined");
    }

    public CallableSymbol resolveConstructor(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Size cannot be negative");
        }
        var entries = constructors.elements();
        while (entries.hasMoreElements()) {
            var entry = entries.nextElement();
            if (entry.getParameterCount() == size) {
                return entry;
            }
        }
        throw new IllegalArgumentException("Constructor with " + size + " parameters not found in symbol table");
    }
}
