package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.il.ConstructorDecl;

import java.util.List;

public class ConstructorSymbolImpl implements ConstructorSymbol {
    private final ConstructorDecl constructorDecl;

    public ConstructorSymbolImpl(ConstructorDecl constructorDecl) {
        this.constructorDecl = constructorDecl;
    }

    @Override
    public String getName() {
        return String.join(",", getParameterNames());
    }

    @Override
    public List<String> getParameterNames() {
        return constructorDecl.params().stream().map(s -> s.id()).toList();
    }
}
