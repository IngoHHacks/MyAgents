package net.ingoh.myagents.lang.symbols;

public class ClassSymbolJava implements ClassSymbol {
    private final Class<?> src;

    public ClassSymbolJava(Class<?> src) {
        this.src = src;
    }

    @Override
    public String getName() {
        return src.getSimpleName();
    }

    @Override
    public String getNamepsace() {
        return src.getPackageName();
    }
}
