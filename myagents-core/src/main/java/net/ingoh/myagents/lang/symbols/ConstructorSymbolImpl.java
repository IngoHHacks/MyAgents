package net.ingoh.myagents.lang.symbols;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.ingoh.myagents.core.DynamicClass;
import net.ingoh.myagents.lang.execution.ExecutionSource;
import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.execution.SymbolTable;
import net.ingoh.myagents.lang.il.BlockStmt;
import net.ingoh.myagents.lang.il.ConstructorDecl;
import net.ingoh.myagents.lang.il.ReturnStmt;
import net.ingoh.myagents.utils.ClassHelper;
import net.ingoh.myagents.utils.MethodFinder;
import net.ingoh.myagents.utils.MethodHelper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ConstructorSymbolImpl implements ConstructorSymbol {
    private final ConstructorDecl constructorDecl;
    private final int size;
    private final SymbolTable symbolTable;
    private final boolean takesInterpreter;

    public ConstructorSymbolImpl(ConstructorDecl constructorDecl, SymbolTable symbolTable) {
        this.constructorDecl = constructorDecl;
        this.size = constructorDecl.params().size();
        this.symbolTable = symbolTable;
        this.takesInterpreter = constructorDecl.params().stream()
                .anyMatch(param -> param.id().equals("interpreter"));
    }

    @Override
    public String getName() {
        return constructorDecl.type().id();
    }

    @Override
    public int getParameterCount() {
        return size;
    }

    @Override
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    @Override
    public boolean takesInterpreter() {
        return takesInterpreter;
    }

    @Override
    public Object invoke(Interpreter interpreter, Object _obj, Object... args) {
        List<MethodSymbol> methods = symbolTable.methods.values().stream()
                .filter(MethodSymbolImpl.class::isInstance)
                .toList();
        var cls = interpreter.coreClassOf(constructorDecl.baseType());
        var instance = newInstance(interpreter, cls, methods, constructorDecl, args);
        var tempExecutionSource = interpreter.getExecutionSource();
        interpreter.setExecutionSource(instance);
        for (var field : symbolTable.fields.keySet()) {
            var existing = (Arrays.stream(instance.getClass().getFields()).filter(field1 -> field1.getName().equals(field)).findFirst());
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
    }

    public Object newInstance(Interpreter interpreter, Class<?> type, List<MethodSymbol> methods, ConstructorDecl constructor, Object... args) {
        var argParams = Arrays.stream(args).map(Object::getClass).map(ClassHelper::unproxy).toArray(Class[]::new);
        Object obj = null;
        try {
            var clsList = Stream.concat(Stream.of(Interpreter.class), Arrays.stream(argParams)).toArray(Class[]::new);
            var jConstr = MethodFinder.findCompatibleConstructor(
                    ClassHelper.unproxy(interpreter.coreClassOf(constructorDecl.baseType())),
                    clsList
            );
            args = MethodHelper.castArgs(jConstr, args);
            clsList = Arrays.stream(jConstr.getParameterTypes())
                    .map(ClassHelper::unproxy)
                    .toArray(Class[]::new);
            var argList = flatten(interpreter, args);
            obj = new ByteBuddy()
                    .subclass(type)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(new DynamicClass(interpreter, methods)))
                    .make()
                    .load(type.getClassLoader())
                    .getLoaded()
                    .getConstructor(clsList)
                    .newInstance(argList);

            interpreter.addInstance(obj, constructorDecl.type().id());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance of " + type.getName(), e);
        }
        var tempExecutionSource = interpreter.getExecutionSource();
        interpreter.setExecutionSource(new ExecutionSource(obj));
        for (BlockStmt stmt :  constructor.body().statements()) {
            if (stmt instanceof ReturnStmt) {
                interpreter.setExecutionSource(tempExecutionSource);
                return obj;
            }
            stmt.accept(interpreter);
        }
        interpreter.setExecutionSource(tempExecutionSource);
        return obj;
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
