package net.ingoh.myagents.lang.symbols;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.ingoh.myagents.core.DynamicClass;
import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.execution.SymbolTable;
import net.ingoh.myagents.utils.ClassHelper;
import net.ingoh.myagents.utils.MethodFinder;
import net.ingoh.myagents.utils.MethodHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConstructorSymbolJava implements ConstructorSymbol {
    private final String name;
    private final Constructor<?> constructor;
    private final SymbolTable symbolTable;

    public ConstructorSymbolJava(String name, Constructor<?> constructor, SymbolTable symbolTable) {
        this.name = name;
        this.constructor = constructor;
        this.symbolTable = symbolTable;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getParameterNames() {
        return Stream.of(constructor.getParameters()).map(Parameter::getName).collect(Collectors.toList());
    }

    @Override
    public int getParameterCount() {
        return constructor.getParameterCount();
    }

    @Override
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    @Override
    public Object invoke(Interpreter interpreter, Object _obj, Object... args) {
        try {
            List<MethodSymbol> methods = symbolTable.methods.values().stream()
                    .filter(MethodSymbolImpl.class::isInstance)
                    .toList();
            var argParams = Arrays.stream(args).map(Object::getClass).map(ClassHelper::unproxy).toArray(Class[]::new);
            var clsList = Stream.concat(Stream.of(Interpreter.class), Arrays.stream(argParams)).toArray(Class[]::new);
            var jConstr = MethodFinder.findCompatibleConstructor(
                    ClassHelper.unproxy(constructor.getDeclaringClass()),
                    clsList
            );
            var argList = flatten(interpreter, args);
            args = MethodHelper.castArgs(jConstr, argList);
            clsList = Arrays.stream(jConstr.getParameterTypes())
                    .map(ClassHelper::unproxy)
                    .toArray(Class[]::new);
            var instance = new ByteBuddy()
                    .subclass(constructor.getDeclaringClass())
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(new DynamicClass(interpreter, methods)))
                    .make()
                    .load(constructor.getDeclaringClass().getClassLoader())
                    .getLoaded()
                    .getConstructor(clsList)
                    .newInstance(argList);

            interpreter.addInstance(instance, name);

            var tempExecutionSource = interpreter.getExecutionSource();
            interpreter.setExecutionSource(instance);
            for (var field : symbolTable.fields.keySet()) {
                var existing = (Arrays.stream(ClassHelper.unproxy(instance.getClass()).getFields()).filter(field1 -> field1.getName().equals(field)).findFirst());
                if (existing.isPresent()) {
                    try {
                        existing.get().set(instance, symbolTable.fields.get(field).getValue(interpreter, interpreter.getExecutionSource().getSource()));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to set field value: " + field, e);
                    }
                }
            }
            interpreter.setExecutionSource(tempExecutionSource);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke constructor: " + getName(), e);
        }
    }

    private Object[] flatten(Interpreter interpreter, Object... args) {
        Object[] flattenedArgs = new Object[args.length + 1];
        flattenedArgs[0] = interpreter;
        for (int i = 0; i < args.length; i++) {
            flattenedArgs[i + 1] = args[i];
        }
        return flattenedArgs;
    }
}
