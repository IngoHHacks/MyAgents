package net.ingoh.myagents.lang.execution;

import net.ingoh.myagents.lang.symbols.MethodSymbol;
import net.ingoh.myagents.lang.symbols.Symbol;

import java.util.Dictionary;
import java.util.Hashtable;

public class SymbolTable {

    public Dictionary<String, SymbolType> symbols = new Hashtable<>();
    public Dictionary<String, Symbol> declarations = new Hashtable<>();

    public SymbolTable() {}

    public void addSymbol(SymbolType symbolType, String simpleName, Symbol decl) {
        symbols.put(simpleName, symbolType);
        declarations.put(simpleName, decl);
    }

    public boolean hasSymbol(SymbolType symbolType, String simpleName) {
        if (symbolType == null || simpleName == null) {
            throw new IllegalArgumentException("Symbol type and name cannot be null");
        }
        return symbols.get(simpleName) == symbolType;
    }

    public SymbolType getSymbol(SymbolType symbolType, String simpleName) {
        return symbols.get(simpleName);
    }

    public MethodSymbol resolveMethod(String methodName) {
        if (methodName == null) {
            throw new IllegalArgumentException("Method name cannot be null");
        }
        if (!hasSymbol(SymbolType.METHOD, methodName)) {
            throw new IllegalArgumentException("Method " + methodName + " not found in symbol table");
        }
        return (MethodSymbol) declarations.get(methodName);
    }
}
