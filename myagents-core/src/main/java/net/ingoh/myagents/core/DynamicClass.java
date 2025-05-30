package net.ingoh.myagents.core;

import net.bytebuddy.implementation.bind.annotation.*;
import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.execution.ReturnVal;
import net.ingoh.myagents.lang.symbols.CallableSymbol;
import net.ingoh.myagents.lang.symbols.MethodSymbol;
import net.ingoh.myagents.lang.symbols.MethodSymbolImpl;
import net.ingoh.myagents.lang.symbols.MethodSymbolJava;
import net.ingoh.myagents.utils.ClassHelper;
import net.ingoh.myagents.utils.MethodFinder;
import net.ingoh.myagents.utils.MethodHelper;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Callable;

public class DynamicClass {
    protected final Interpreter interpreter;
    private final Hashtable<String, MethodSymbolImpl> dslMethods;

    public DynamicClass(Interpreter interpreter, List<MethodSymbol> dslMethods) {
        if (interpreter == null) {
            throw new IllegalArgumentException("Interpreter cannot be null");
        }
        this.interpreter = interpreter;
        if (dslMethods == null) {
            throw new IllegalArgumentException("DSL methods cannot be null");
        }
        this.dslMethods = new Hashtable<>();
        for (MethodSymbol method : dslMethods) {
            if (method instanceof MethodSymbolImpl methodImpl) {
                this.dslMethods.put(methodImpl.getName(), methodImpl);
            }
        }
    }

    public CallableSymbol get(Object instance, String methodName, Object[] args) {
        if (!dslMethods.containsKey(methodName)) {
            Method method = MethodFinder.findCompatibleMethod(ClassHelper.unproxy(instance.getClass()), methodName,
                    args != null ? List.of(args).stream().map(Object::getClass).map(ClassHelper::unproxy).toArray(Class[]::new) : new Class[0]);
            args = MethodHelper.castArgs(method, args);
            return new MethodSymbolJava(method, this);
        }
        MethodSymbolImpl dslMethod = dslMethods.get(methodName);
        return dslMethod;
    }

    @RuntimeType
    public Object intercept(@SuperCall Callable<?> superCall, @Origin Method method, @This Object instance, @AllArguments Object[] args) throws Exception {
        var call = get(instance, method.getName(), args);
        Object r;
        if (call instanceof MethodSymbolJava jCall) {
            try {
                r = superCall.call();
            } catch (Exception e) {
                throw new RuntimeException("Failed to invoke method: " + jCall.getName(), e);
            }
        } else {
            r = call.invoke(interpreter, instance, args);
        }
        if (r instanceof ReturnVal rv) {
            return rv.value;
        }
        return r;
    }
}