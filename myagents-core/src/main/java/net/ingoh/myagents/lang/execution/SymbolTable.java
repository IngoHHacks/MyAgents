package net.ingoh.myagents.lang.execution;

import java.io.PrintStream;
import java.util.Hashtable;

import net.ingoh.myagents.lang.symbols.*;

public class SymbolTable {

    // TODO: Allow variable shadowing and enforce scope rules
    public Hashtable<String, SymbolType> symbols = new Hashtable<>();
    public Hashtable<String, Symbol> declarations = new Hashtable<>();

    public Hashtable<String, ClassSymbol> classes = new Hashtable<>();
    public Hashtable<String, MethodSymbol> methods = new Hashtable<>();
    public Hashtable<String, VariableSymbol> fields = new Hashtable<>();
    public Hashtable<String, ConstructorSymbol> constructors = new Hashtable<>();

    public SymbolTable(Interpreter interpreter) {}

    public SymbolTable(Interpreter interpreter, boolean global) {
        if (global) {
            addSymbol(interpreter, SymbolType.NONLOCAL_CLASS, "System", new ClassSymbolJava(System.class));
            addSymbol(interpreter, SymbolType.NONLOCAL_CLASS, "Object", new ClassSymbolJava(Object.class));
            addSymbol(interpreter, SymbolType.NONLOCAL_CLASS, "String", new ClassSymbolJava(String.class));
            addSymbol(interpreter, SymbolType.NONLOCAL_CLASS, "Number", new ClassSymbolJava(Number.class));
            addSymbol(interpreter, SymbolType.NONLOCAL_CLASS, "Math", new ClassSymbolJava(Math.class));
            addSymbol(interpreter, SymbolType.NONLOCAL_CLASS, "Color", new ClassSymbolJava(java.awt.Color.class));
        }
    }

    public void addSymbol(Interpreter interpreter, SymbolType symbolType, String simpleName) {
        switch (symbolType) {
            case FIELD:
            case PARAMETER:
            case LOCAL_VARIABLE:
            case VARIABLE:
                addSymbol(interpreter, symbolType, simpleName, new VariableSymbolImpl(interpreter, simpleName, null, null));
                break;
            default:
                throw new IllegalArgumentException("Cannot add symbol of type " + symbolType + " without a declaration");
        }
    }

    public void addSymbol(Interpreter interpreter, SymbolType symbolType, String simpleName, Symbol decl) {
        if (symbols.containsKey(simpleName)) {
            if (getSymbolType(interpreter, simpleName) != symbolType) {
                System.out.println("Warning: Symbol " + simpleName + " already exists with type " +
                        getSymbolType(interpreter, simpleName) + ". Overwriting with type " + symbolType);
            }
            else if (symbolType == SymbolType.FIELD || symbolType == SymbolType.PARAMETER ||
                symbolType == SymbolType.LOCAL_VARIABLE || symbolType == SymbolType.VARIABLE) {
                var varSymbol = (VariableSymbol) declarations.get(simpleName);
                if (varSymbol != null) {
                    varSymbol.setValue(interpreter, interpreter.getExecutionSource().getSource(), decl);
                }
                return;
            }
        }
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
            if (this != interpreter.getGlobals()) {
                return interpreter.getGlobals().getSymbol(interpreter, symbolType, simpleName);
            }
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
            switch (methodName) {
                case "print":
                    return new MethodSymbolJava(PrintStream.class.getMethod("println", Object.class), System.out);
                case "min":
                    return new MethodSymbolJava(Math.class.getMethod("min", double.class, double.class), Math.class);
                case "max":
                    return new MethodSymbolJava(Math.class.getMethod("max", double.class, double.class), Math.class);
                case "round":
                    return new MethodSymbolJava(Math.class.getMethod("round", double.class), Math.class);
                case "abs":
                    return new MethodSymbolJava(Math.class.getMethod("abs", double.class), Math.class);
                case "rnd":
                    return new MethodSymbolJava(Math.class.getMethod("random"), Math.class);
                case "interpreter":
                    return new LambdaMethodSymbol("interpreter", (__) -> interpreter);
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
        return constructors.values().stream()
                .filter(c -> c.getParameterCount() == size || c.takesInterpreter() && c.getParameterCount() == size + 1)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Constructor with " + size + " parameters not found in symbol table"));
    }
}
