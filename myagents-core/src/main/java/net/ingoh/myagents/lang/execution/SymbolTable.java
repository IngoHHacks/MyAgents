package net.ingoh.myagents.lang.execution;

import net.ingoh.myagents.lang.symbols.*;

import java.io.PrintStream;
import java.util.Dictionary;
import java.util.Hashtable;

public class SymbolTable {

    // TODO: Allow variable shadowing and enforce scope rules
    public Dictionary<String, SymbolType> symbols = new Hashtable<>();
    public Dictionary<String, Symbol> declarations = new Hashtable<>();

    public Dictionary<String, ClassSymbol> classes = new Hashtable<>();
    public Dictionary<String, MethodSymbol> methods = new Hashtable<>();
    public Dictionary<String, VariableSymbol> fields = new Hashtable<>();
    public Dictionary<String, ConstructorSymbol> constructors = new Hashtable<>();

    public SymbolTable() {}

    public void addSymbol(Interpreter interpreter, SymbolType symbolType, String simpleName) {
        switch (symbolType) {
            case FIELD:
            case PARAMETER:
            case LOCAL_VARIABLE:
            case VARIABLE:
                addSymbol(interpreter, symbolType, simpleName, new VariableSymbolImpl(interpreter, simpleName, null));
                break;
            default:
                throw new IllegalArgumentException("Cannot add symbol of type " + symbolType + " without a declaration");
        }
    }

    public void addSymbol(Interpreter interpreter, SymbolType symbolType, String simpleName, Symbol decl) {
        symbols.put(simpleName, symbolType);
        declarations.put(simpleName, decl);
        switch (symbolType) {
            case NONLOCAL_CLASS:
            case LOCAL_CLASS:
            case CLASS:
                classes.put(simpleName, (ClassSymbol) decl);
                break;
            case CALLABLE:
            case METHOD:
                methods.put(simpleName, (MethodSymbol) decl);
                break;
            case FIELD:
            case PARAMETER:
            case LOCAL_VARIABLE:
            case VARIABLE:
                fields.put(simpleName, (VariableSymbol) decl);
                break;
            case CONSTRUCTOR:
                constructors.put(simpleName, (ConstructorSymbol) decl);
                break;
        }
    }

    public boolean hasSymbol(Interpreter interpreter, SymbolType symbolType, String simpleName) {
        if (symbolType == null || simpleName == null) {
            throw new IllegalArgumentException("Symbol type and id cannot be null");
        }
        return symbols.get(simpleName) == symbolType;
    }

    public SymbolType getSymbolType(Interpreter interpreter, String simpleName) {
        return symbols.get(simpleName);
    }

    public Symbol getSymbol(Interpreter interpreter, SymbolType symbolType, String simpleName) {
        var s = declarations.get(simpleName);
        if (symbolType != SymbolType.ANY) {
            if (s == null || !hasSymbol(interpreter, symbolType, simpleName)) {
                addSymbol(interpreter, symbolType, simpleName);
                return declarations.get(simpleName);
            }
            return s;
        }
        if (s == null) {
            throw new IllegalArgumentException("Symbol " + simpleName + " not found in symbol table");
        }
        return s;
    }

    public MethodSymbol resolveMethod(Interpreter interpreter, String methodName) {
        if (methodName == null) {
            throw new IllegalArgumentException("Method id cannot be null");
        }
        if (!hasSymbol(interpreter, SymbolType.METHOD, methodName)) {
            return resolveSpecialMethod(interpreter, methodName);
        }
        return (MethodSymbol) declarations.get(methodName);
    }

    private MethodSymbol resolveSpecialMethod(Interpreter interpreter, String methodName) {
        try {
            if (methodName.equals("print")) {
                return new MethodSymbolJava(PrintStream.class.getMethod("println", Object.class), System.out);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Error resolving special method: " + methodName, e);
        }
        throw new IllegalArgumentException("Method " + methodName + " is not defined");
    }

    public CallableSymbol resolveConstructor(Interpreter interpreter, int size) {
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
