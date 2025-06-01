package net.ingoh.myagents.lang.symbols;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.utils.ClassHelper;
import net.ingoh.myagents.utils.MethodFinder;
import net.ingoh.myagents.utils.MethodHelper;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Stream;

public class MethodSymbolJava implements MethodSymbol {
    private final Method src;
    private final Object obj;

    public MethodSymbolJava(Method src) {
        this.src = src;
        this.obj = null;
    }

    public MethodSymbolJava(Method src, Object obj) {
        this.src = src;
        this.obj = obj;
    }

    @Override
    public String getName() {
        return src.getName();
    }

    @Override
    public Object invoke(Interpreter interpreter, Object objOvr, Object... args) {
        try {
            if (obj != null) {
                objOvr = obj;
            }
            while (objOvr instanceof VariableSymbol) {
                objOvr = ((VariableSymbol) objOvr).getValue(interpreter, interpreter.getExecutionSource().getSource());
            }
            Class<?> cls;
            if (objOvr instanceof Class<?>) {
                cls = (Class<?>) objOvr;
            } else {
                cls = objOvr.getClass();
            }
            var instance = (objOvr != null ? objOvr : interpreter.getExecutionSource().getSource());
            var method = MethodFinder.findCompatibleMethod(
                    cls,
                    getName(),
                    args != null ? Stream.of(args)
                            .map(Object::getClass)
                            .map(ClassHelper::unproxy)
                            .toArray(Class[]::new) : new Class[0]);
            if (method == null) {
                throw new NoSuchMethodException("No compatible method found: " + getName());
            }
            args = MethodHelper.castArgs(method, args);
            return method.invoke(instance, args);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke method: " + getName(), e);
        }
    }
}
