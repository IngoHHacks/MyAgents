package net.ingoh.myagents.lang.symbols;

public class ClassSymbolJava implements ClassSymbol {
    private final Class<?> src;

    public ClassSymbolJava(Class<?> src) {
        this.src = src;
    }

    public Class<?> getSrc() {
        return src;
    }

    @Override
    public String getName() {
        return src.getSimpleName();
    }

    @Override
    public String getNameSpace() {
        return src.getPackageName();
    }
}
