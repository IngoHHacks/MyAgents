package net.ingoh.myagents.lang.execution;

public enum SymbolType {
    NONLOCAL_CLASS,
    METHOD,
    FIELD,
    PARAMETER,
    LOCAL_VARIABLE,
    LOCAL_CLASS,
    CONSTRUCTOR,
    CALLABLE, // Method, Constructor
    CLASS, // Nonlocal class, Local class
    VARIABLE, // Field, Parameter, Local Variable
    ANY
}
