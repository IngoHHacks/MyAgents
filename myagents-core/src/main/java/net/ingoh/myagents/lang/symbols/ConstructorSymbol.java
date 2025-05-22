package net.ingoh.myagents.lang.symbols;

import java.util.List;

public interface ConstructorSymbol extends CallableSymbol {
    List<String> getParameterNames();
    int getParameterCount();
}
