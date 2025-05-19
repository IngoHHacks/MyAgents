package net.ingoh.myagents.lang.symbols;

import java.util.List;

public interface ConstructorSymbol extends Symbol {
    List<String> getParameterNames();
}
