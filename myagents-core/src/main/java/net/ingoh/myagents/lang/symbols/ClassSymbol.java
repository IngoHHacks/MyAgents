package net.ingoh.myagents.lang.symbols;

public interface ClassSymbol extends Symbol {
    String getNameSpace();
    default String getFullName() {
        String ns = getNameSpace();
        return ns.isEmpty() ? getName() : ns + "." + getName();
    }
}
