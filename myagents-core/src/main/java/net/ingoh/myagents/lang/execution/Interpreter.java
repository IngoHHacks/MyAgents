package net.ingoh.myagents.lang.execution;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import net.ingoh.myagents.core.DSLArray;
import net.ingoh.myagents.core.basetypes.Environment;
import net.ingoh.myagents.lang.DSLParser;
import net.ingoh.myagents.lang.il.*;
import net.ingoh.myagents.lang.symbols.*;
import net.ingoh.myagents.utils.ClassHelper;

public class Interpreter {

    private Hashtable<String, ProgramFile> programFiles = new Hashtable<>();
    private ExecutionSource executionSource = new ExecutionSource(null);
    private ProgramFile currentProgramFile;
    private Hashtable<Object, String> instances = new Hashtable<>();
    private SymbolTable globals = new SymbolTable(this, true);
    private List<ProgramFile> envs = new LinkedList<>();

    public Interpreter(List<ProgramDecl> sources) {
        for (var source : sources) {
            var file = parseFile(source);
            var name = "";
            for (var decl : source.topLevelDecls()) {
                if (decl instanceof PackageDecl packageDecl) {
                    name = packageDecl.namespace().id();
                } else if (decl instanceof OverrideBodyDecl overrideBodyDecl) {
                    file.baseType = getType(overrideBodyDecl.type());
                    name = overrideBodyDecl.name();
                    if (file.name == null || file.name.isEmpty()) {
                        file.name = name;
                    }
                    addBaseMethods(name, file, file.baseType);
                    build(file, name, file.baseType);
                    if (file.baseType == Environment.class) {
                        envs.add(file);
                    }
                    if (overrideBodyDecl.agents() != null) {
                        file.symbolTable.addSymbol(this, SymbolType.FIELD, "agentTypes", new VariableSymbolImpl(this, "agentTypes", null, overrideBodyDecl.agents()));
                        for (var agent : overrideBodyDecl.agents()) {
                            globals.addSymbol(this, SymbolType.NONLOCAL_CLASS, agent, importFrom(agent));
                            file.symbolTable.addSymbol(this, SymbolType.NONLOCAL_CLASS, agent, importFrom(agent));
                        }
                    }
                    if (overrideBodyDecl.importDecls() != null) {
                        for (var importDecl : overrideBodyDecl.importDecls()) {
                            importFrom(importDecl);
                        }
                    }
                } else {
                    decl.accept(this);
                }
            }
            programFiles.put(name, file);
        }
    }

    private Class<?> getType(String type) {
        if (type == null) {
            throw new IllegalArgumentException("TypeIdentifier cannot be null");
        }
        try {
            return Class.forName("net.ingoh.myagents.core.basetypes." + type);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Type not found: " + type, e);
        }
    }

    private void addBaseMethods(String name, ProgramFile file, Class<?> type) {
        if (type == null) {
            return;
        }
        for (var method : type.getDeclaredMethods()) {
            if (!file.symbolTable.methods.containsKey(method.getName())) {
                file.symbolTable.addSymbol(this, SymbolType.METHOD, method.getName(), new MethodSymbolJava(method));
            }
        }
        for (var field : type.getDeclaredFields()) {
            if (!file.symbolTable.fields.containsKey(field.getName())) {
                file.symbolTable.addSymbol(this, SymbolType.FIELD, field.getName(), new VariableSymbolJava(field));
            }
        }
        for (var constructor : type.getDeclaredConstructors()) {
            if (!file.symbolTable.constructors.containsKey(constructor.getName())) {
                file.symbolTable.addSymbol(this, SymbolType.CONSTRUCTOR, constructor.getName(), new ConstructorSymbolJava(name, constructor, file.symbolTable));
            }
        }
        for (var innerClass : type.getDeclaredClasses()) {
            if (!file.symbolTable.classes.containsKey(innerClass.getSimpleName())) {
                file.symbolTable.addSymbol(this, SymbolType.NONLOCAL_CLASS, innerClass.getSimpleName(), new ClassSymbolJava(innerClass));
            }
        }
        if (type.getSuperclass() != null && !type.getSuperclass().getName().equals("java.lang.Object")) {
            addBaseMethods(name, file, type.getSuperclass());
        }
        for (var iface : type.getInterfaces()) {
            addBaseMethods(name, file, iface);
        }
    }

    private void build(ProgramFile file, String name, Class<?> type) {
        switch (type.getName()) {
            case "net.ingoh.myagents.core.basetypes.Environment" -> {
                file.symbolTable.addSymbol(this, SymbolType.METHOD, "main", new MethodSymbolImpl(
                        ((MethodDecl)((OverrideBodyDecl) DSLParser.parse("main() {instance = new " + name + "(\"" +  name + "\"); return instance.run();}").topLevelDecls().get(0)).bodyDecls().get(0)))
                );
            }
        }
    }

    public void run(ProgramFile program) {
        runFile(program);
    }

    private void runFile(ProgramFile program) {
        currentProgramFile = program;
        if (currentProgramFile.symbolTable.hasSymbol(this, SymbolType.METHOD, "main")) {
            var m = currentProgramFile.symbolTable.resolveMethod(this, "main");
            var code = invoke(ExecutionSource.STATIC, m);
            if (code instanceof ReturnVal returnVal) {
                code = returnVal.value;
            }
            System.out.println("Program started with code: " + code);
        }
    }

    private ProgramFile parseFile(ProgramDecl program) {
        currentProgramFile = new ProgramFile(this, "");
        for (var decl : program.topLevelDecls()) {
            decl.accept(this);
        }
        programFiles.put(currentProgramFile.namespace.id(), currentProgramFile);
        return currentProgramFile;
    }

    public ProgramFile resolveFile(String str) {
        var file = programFiles.get(str);
        if (file == null) {
            return getBuiltInFile(str);
        }
        return file;
    }

    private ProgramFile getBuiltInFile(String str) {
        try {
            var cls = Class.forName(str);
            return ProgramFile.fromClass(this, cls);
        } catch (Exception e) {
            throw new RuntimeException("Error resolving file: " + str, e);
        }
    }

    public Object runBlock(List<BlockStmt> statements) {
        for (var stmt : statements) {
            if (stmt instanceof ReturnStmt) {
                return new ReturnVal(stmt.accept(this));
            }
            var r = stmt.accept(this);
            if (r != null && r instanceof ReturnVal returnVal) {
                return returnVal;
            }
        }
        return null;
    }

    private Object invoke(ExecutionSource src, MethodSymbol method, Object... args) {
        var prevExecutionSource = executionSource;
        executionSource = src;
        if (method == null) {
            throw new IllegalArgumentException("Method cannot be null");
        }
        var result = method.invoke(this, executionSource.getSource(), args);
        executionSource = prevExecutionSource;
        return result;
    }

    public CallableSymbol resolveMethod(Object src, IdentifierOrSpecial methodId, ExprList args) {
        var tempFile = currentProgramFile;
        var tempExecutionSource = executionSource;
        String str = null;
        if (src instanceof ClassSymbol classSymbol) {
            str = classSymbol.getNameSpace();
            if (classSymbol instanceof ClassSymbolFile classSymbolFile) {
                src = classSymbolFile.getFile();
            } else if (classSymbol instanceof ClassSymbolImpl classSymbolImpl) {
                src = classSymbolImpl.getClassDecl();
            } else if (classSymbol instanceof ClassSymbolJava classSymbolJava) {
                src = classSymbolJava.getSrc();
            } else {
                throw new RuntimeException("Unknown class symbol type: " + classSymbol.getClass().getName());
            }
        } else {
            while (src instanceof VariableSymbol variableSymbol) {
                var obj = variableSymbol.getValue(this, executionSource.getSource());
                if (obj == null) {
                    throw new RuntimeException("Object is null: " + variableSymbol.getName());
                }
                src = obj;
            }
        }
        if (src != null && !(src instanceof ProgramFile)) {
            str = ClassHelper.unproxy(src.getClass()).getName();
        } else if (src != null) {
            str = src.getClass().getName();
        }
        executionSource = new ExecutionSource(src);
        if (src instanceof ProgramFile programFile) {
            currentProgramFile = programFile;
        } else if (!(src instanceof Class<?>)) {
            currentProgramFile = str != null ? resolveFile(str) : currentProgramFile;
        }
        if (currentProgramFile == null) {
            throw new RuntimeException("File not found: " + src.getClass().getPackageName());
        }
        var method = resolveGlobalMethod(methodId, args);
        currentProgramFile = tempFile;
        executionSource = tempExecutionSource;
        return method;
    }

    public CallableSymbol resolveGlobalMethod(IdentifierOrSpecial methodName, ExprList args) {
        if (methodName instanceof ThisExpr) {
            return currentProgramFile.symbolTable.resolveConstructor(this, args.exprs().size());
        } else if (methodName instanceof SuperExpr) {
            throw new RuntimeException("Super method calls not supported yet");
        } else if (methodName instanceof Identifier methodId) {
            return currentProgramFile.symbolTable.resolveMethod(this, methodId.id());
        }
        throw new IllegalArgumentException("Invalid method name: " + methodName);
    }


    public CallableSymbol resolveConstructor(TypeIdentifier type, ExprList args) {
        return resolveConstructor(type, args.exprs().size());
    }

    public CallableSymbol resolveConstructor(TypeIdentifier type, int size) {
        var tempFile = currentProgramFile;
        currentProgramFile = resolveFile(type.id());
        if (currentProgramFile == null) {
            throw new RuntimeException("File not found: " + type.getNamespace().id());
        }
        var constructor = currentProgramFile.symbolTable.resolveConstructor(this, size);
        if (constructor == null) {
            throw new RuntimeException("Constructor not found: " + type.getNamespace().id());
        }
        currentProgramFile = tempFile;
        return constructor;
    }

    ////////////////////////////////////////////

    public void setPackageDecl(NamespaceIdentifier namespace) {
        currentProgramFile.namespace = namespace;
    }

    public ClassSymbolFile importFrom(String namespace) {
        return importFrom(new ImportDecl(false, new NamespaceIdentifier(namespace)));
    }

    public ClassSymbolFile importFrom(ImportDecl importDecl) {
        if (importDecl.isStatic()) {
            throw new RuntimeException("Static import not supported yet");
        } else {
            var file = resolveFile(importDecl.namespace().id());
            programFiles.put(file.namespace.id(), file);
            var importedClass = new ClassSymbolFile(this, file);
            currentProgramFile.symbolTable.addSymbol(this, SymbolType.NONLOCAL_CLASS, file.name, importedClass);
            return importedClass;
        }
    }

    public void declsFromOverrideBody(OverrideBodyDecl overrideBodyDecl) {
        var decls = overrideBodyDecl.bodyDecls();
        for (var decl : decls) {
            if (decl != null && !(decl instanceof Block)) {
                decl.accept(this);
            }
        }
        if (currentProgramFile.symbolTable.constructors.size() == 0) {
            var constructor = new ConstructorSymbolImpl(new ConstructorDecl(new TypeIdentifier(overrideBodyDecl.name()), new TypeIdentifier(overrideBodyDecl.type()), List.of(), null), currentProgramFile.symbolTable);
            currentProgramFile.symbolTable.addSymbol(this, SymbolType.CONSTRUCTOR, overrideBodyDecl.name(), constructor);
        }
    }

    public void classDecl(ClassDecl classDecl) {
        currentProgramFile.symbolTable.addSymbol(this, SymbolType.NONLOCAL_CLASS, classDecl.id().id(), new ClassSymbolImpl(this, classDecl));
    }

    public void methodDecl(MethodDecl methodDecl) {
        currentProgramFile.symbolTable.addSymbol(this, SymbolType.METHOD, methodDecl.id().id(), new MethodSymbolImpl(methodDecl));
    }

    public void fieldDecl(FieldDecl fieldDecl) {
        currentProgramFile.symbolTable.addSymbol(this, SymbolType.FIELD, fieldDecl.id().id(), new VariableSymbolImpl(this, fieldDecl.id().id(), executionSource.getSource(), visitExpr(fieldDecl.expr())));
    }

    public void constructorDecl(ConstructorDecl constructorDecl) {
        currentProgramFile.symbolTable.addSymbol(this, SymbolType.CONSTRUCTOR, String.join(",", constructorDecl.params().stream()
                .map(ParameterIdentifier::id)
                .toList()), new ConstructorSymbolImpl(constructorDecl, currentProgramFile.symbolTable));
    }

    public void localVariableDecl(LocalVariableDecl localVariableDecl) {
        currentProgramFile.symbolTable.addSymbol(this, SymbolType.LOCAL_VARIABLE, localVariableDecl.id().id(), new VariableSymbolImpl(this, localVariableDecl.id().id(), executionSource.getSource(), visitExpr(localVariableDecl.expr())));
    }

    public void localClassDecl(LocalClassDecl localClassDecl) {
        currentProgramFile.symbolTable.addSymbol(this, SymbolType.LOCAL_CLASS, localClassDecl.classDecl().id().id(), new ClassSymbolImpl(this, localClassDecl.classDecl()));
    }

    public Symbol getSymbol(SymbolType type, String id) {
        return currentProgramFile.symbolTable.getSymbol(this, type, id);
    }

    public Symbol getSymbol(String id) {
        return currentProgramFile.symbolTable.getSymbol(this, SymbolType.ANY, id);
    }

    public Object invokeMethod(MethodDecl methodDecl, Object obj, Object[] args) {
        if (args.length != methodDecl.params().size()) {
            throw new IllegalArgumentException("Invalid number of arguments. Expected: " + methodDecl.params().size() + ", got: " + args.length);
        }
        var tempProgramFile = currentProgramFile;
        if (obj != null) {
            var myFile = whatsMyFile(obj);
            if (myFile != null) {
                currentProgramFile = myFile;
            }
        }
        List<ParameterIdentifier> params = methodDecl.params();
        for (int i = 0; i < params.size(); i++) {
            var param = params.get(i);
            currentProgramFile.symbolTable.addSymbol(this, SymbolType.PARAMETER, param.id(), new VariableSymbolImpl(this, param.id(), obj, args[i]));
        }
        var tempExecutionSource = executionSource;
        executionSource = new ExecutionSource(obj);
        var result = methodDecl.body().accept(this);
        executionSource = tempExecutionSource;
        currentProgramFile = tempProgramFile;
        return result;
    }

    public Object visitExpr(Expr expr) {
        if (expr == null) {
            return null;
        }
        return expr.accept(this);
    }

    public Object memberRefExpr(Object refValue, Object memberValue) {
        return new AnyMemberIdentifier(memberValue.toString());
    }

    public Object memberAccessExpr(Object targetValue, Object memberValue) {
        if (targetValue == null) {
            throw new RuntimeException("Target value is null for member access: " + memberValue);
        }
        if (memberValue == null) {
            throw new RuntimeException("Member value is null for member access");
        }
        while (targetValue instanceof VariableSymbol variableSymbol) {
            targetValue = variableSymbol.getValue(this, executionSource.getSource());
        }
        if (targetValue instanceof ClassSymbol classSymbol) {
            return staticMemberAccessExpr(classSymbol, memberValue);
        }
        var file = whatsMyFile(targetValue);
        if (file != null && file.symbolTable.hasSymbol(this, SymbolType.FIELD, memberValue.toString())) {
            return ((VariableSymbol) file.symbolTable.getSymbol(this, SymbolType.FIELD, memberValue.toString())).getValue(this, targetValue);
        }
        try {
            return targetValue.getClass().getField(memberValue.toString()).get(targetValue);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Field not found: " + memberValue + " in " + targetValue.getClass().getName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot access field: " + memberValue + " in " + targetValue.getClass().getName(), e);
        }
    }

    public Object staticMemberAccessExpr(ClassSymbol classSymbol, Object memberValue) {
        if (classSymbol == null || memberValue == null) {
            throw new IllegalArgumentException("Class symbol and member value cannot be null");
        }
        if (classSymbol instanceof ClassSymbolJava classSymbolJava) {
            var cls = classSymbolJava.getSrc();
            try {
                return cls.getField(memberValue.toString()).get(null);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Static field not found: " + memberValue + " in " + cls.getName(), e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot access static field: " + memberValue + " in " + cls.getName(), e);
            }
        } else {
            String cls;
            ProgramFile file;
            if (classSymbol instanceof ClassSymbolFile importedClass) {
                file = importedClass.getFile();
                cls = file.fullName();
            }
            else if (classSymbol instanceof ClassSymbolImpl classSymbolImpl) {
                cls = classSymbolImpl.getClassDecl().id().id();
                file = whatsMyFile(cls);
            }
            else {
                throw new RuntimeException("File not found for class symbol: " + classSymbol);
            }
            if (file == null) {
                throw new RuntimeException("File not found for class symbol: " + classSymbol);
            }
            if (file.symbolTable.hasSymbol(this, SymbolType.FIELD, memberValue.toString())) {
                return ((VariableSymbol) file.symbolTable.getSymbol(this, SymbolType.FIELD, memberValue.toString())).getValue(this, null);
            }
            try {
                return Class.forName(cls).getField(memberValue.toString()).get(null);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Static field not found: " + memberValue + " in " + cls, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot access static field: " + memberValue + " in " + cls, e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Class not found: " + cls, e);
            }
        }
    }

    ////////////////////////////////////////////

    public ExecutionSource getExecutionSource() {
        return executionSource;
    }

    public void setExecutionSource(Object obj) {
        if (obj instanceof ExecutionSource) {
            this.executionSource = (ExecutionSource) obj;
        } else {
            this.executionSource = new ExecutionSource(obj);
        }
        while (executionSource.getSource() instanceof ExecutionSource) {
            executionSource = (ExecutionSource) executionSource.getSource();
        }
    }

    public Class<?> coreClassOf(TypeIdentifier type) {
        return getType(type.id());
    }

    public Object transformVars(Interpreter interpreter, Object o) {
        if (o instanceof VariableSymbol variableSymbol) {
            return variableSymbol.getValue(interpreter, executionSource.getSource());
        } else if (o instanceof CallableSymbol callableSymbol) {
            return callableSymbol.getName();
        }else if (o instanceof ClassSymbol classSymbol) {
            return classSymbol.getFullName();
        } else if (o instanceof Class<?> cls) {
            return cls.getName();
        }
        return o;
    }

    public void addInstance(Object obj, String id) {
        instances.put(obj, id);
    }

    private ProgramFile whatsMyFile(Object obj) {
        if (instances.containsKey(obj) && programFiles.containsKey(instances.get(obj))) {
            return programFiles.get(instances.get(obj));
        }
        return null;
    }

    public ProgramFile getCurrentFile() {
        return currentProgramFile;
    }

    public void setCurrentFile(ProgramFile file) {
        this.currentProgramFile = file;
    }

    public SymbolTable getGlobals() {
        return globals;
    }

    public ProgramFile getMain() {
        if (envs.isEmpty()) {
            throw new RuntimeException("No main environment found");
        }
        if (envs.size() > 1) {
            System.out.println("Multiple main environments found:");
            while (true) {
                var i = 1;
                for (var env : envs) {
                    System.out.println(i + ": " + env.name);
                    i++;
                }
                System.out.print("Select one by index: ");
                var input = new java.util.Scanner(System.in).next();
                var n = 0;
                if (input.matches("\\d+")) {
                    n = Integer.parseInt(input);
                    if (n >= 1 && n <= envs.size()) {
                        return envs.get(n - 1);
                    } else {
                        System.out.println("Invalid index, please enter a number between 1 and " + envs.size() + ".");
                    }
                } else {
                    System.out.println("Invalid input, please enter a number between 1 and " + envs.size() + ".");
                }
            }
        }
        return envs.getFirst();
    }

    public DSLArray parseArray(ArrayLiteralExpr arrayLiteralExpr) {
        var list = new LinkedList<>();
        for (var expr : arrayLiteralExpr.elements()) {
            var value = visitExpr(expr);
            if (value instanceof ArrayLiteralExpr subArray) {
                value = parseArray(subArray);
            }
            list.add(value);
        }
        return new DSLArray(list);
    }
}
