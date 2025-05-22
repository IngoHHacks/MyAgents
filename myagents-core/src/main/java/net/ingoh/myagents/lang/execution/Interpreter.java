package net.ingoh.myagents.lang.execution;

import net.ingoh.myagents.core.CustomObject;
import net.ingoh.myagents.lang.il.*;
import net.ingoh.myagents.lang.symbols.*;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class Interpreter {

    private Dictionary<String, ProgramFile> programFiles = new Hashtable<>();
    private ExecutionSource executionSource;
    private ProgramFile currentProgramFile;

    public Interpreter(List<ProgramDecl> sources) {
        for (var source : sources) {
            var file = parseFile(source);
            var name = "";
            for (var decl : source.topLevelDecls()) {
                if (decl instanceof PackageDecl packageDecl) {
                    name = packageDecl.namespace().id();
                } else if (decl instanceof OverrideBodyDecl overrideBodyDecl) {
                    name = overrideBodyDecl.name();
                }
            }
            programFiles.put(name, file);
        }
    }

    public void run(ProgramFile program) {
        runFile(program);
    }

    private void runFile(ProgramFile program) {
        currentProgramFile = program;
        if (currentProgramFile.symbolTable.hasSymbol(this, SymbolType.METHOD, "init")) {
            var m = currentProgramFile.symbolTable.resolveMethod(this, "init");
            var code = invoke(ExecutionSource.STATIC, m);
            System.out.println("Program exited with code: " + code);
        }
    }

    private ProgramFile parseFile(ProgramDecl program) {
        currentProgramFile = new ProgramFile();
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
                return stmt.accept(this);
            }
            stmt.accept(this);
        }
        return null;
    }

    private Object invoke(ExecutionSource src, MethodSymbol method, Object... args) {
        var prevExecutionSource = executionSource;
        executionSource = src;
        if (method == null) {
            throw new IllegalArgumentException("Method cannot be null");
        }
        var result = method.invoke(this, args);
        executionSource = prevExecutionSource;
        return result;
    }

    public CallableSymbol resolveMethod(Object src, IdentifierOrSpecial methodId, ExprList args) {
        var tempFile = currentProgramFile;
        var tempExecutionSource = executionSource;
        String str;
        if (src instanceof ClassSymbol classSymbol) {
            str = classSymbol.getNamepsace();
        } else {
            if (src instanceof VariableSymbol variableSymbol) {
                var obj = variableSymbol.getValue(this);
                if (obj == null) {
                    throw new RuntimeException("Object is null: " + variableSymbol.getName());
                }
                src = obj;
            }
            if (src instanceof CustomObject co) {
                str = co.type.id();
            } else {
                str = src.getClass().getName();
            }
        }
        currentProgramFile = str != null ? resolveFile(str) : currentProgramFile;
        executionSource = new ExecutionSource(src);
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

    public void importFrom(ImportDecl importDecl) {
        var prevFile = currentProgramFile;
        if (importDecl.isStatic()) {
            throw new RuntimeException("Static import not supported yet");
        } else {
            var file = resolveFile(importDecl.namespace().id());
            programFiles.put(file.namespace.id(), file);
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
            var constructor = new ConstructorSymbolImpl(new ConstructorDecl(new TypeIdentifier(overrideBodyDecl.name()), List.of(), null));
            currentProgramFile.symbolTable.addSymbol(this, SymbolType.CONSTRUCTOR, overrideBodyDecl.name(), constructor);
        }
    }

    public void classDecl(ClassDecl classDecl) {
        currentProgramFile.symbolTable.addSymbol(this, SymbolType.NONLOCAL_CLASS, classDecl.id().id(), new ClassSymbolImpl(classDecl));
    }

    public void methodDecl(MethodDecl methodDecl) {
        currentProgramFile.symbolTable.addSymbol(this, SymbolType.METHOD, methodDecl.id().id(), new MethodSymbolImpl(methodDecl));
    }

    public void fieldDecl(FieldDecl fieldDecl) {
        currentProgramFile.symbolTable.addSymbol(this, SymbolType.FIELD, fieldDecl.id().id(), new VariableSymbolImpl(this, fieldDecl.id().id(), visitExpr(fieldDecl.expr())));
    }

    public void constructorDecl(ConstructorDecl constructorDecl) {
        currentProgramFile.symbolTable.addSymbol(this, SymbolType.CONSTRUCTOR, String.join(",", constructorDecl.params().stream()
                .map(ParameterIdentifier::id)
                .toList()), new ConstructorSymbolImpl(constructorDecl));
    }

    public void localVariableDecl(LocalVariableDecl localVariableDecl) {
        currentProgramFile.symbolTable.addSymbol(this, SymbolType.LOCAL_VARIABLE, localVariableDecl.id().id(), new VariableSymbolImpl(this, localVariableDecl.id().id(), visitExpr(localVariableDecl.expr())));
    }

    public void localClassDecl(LocalClassDecl localClassDecl) {
        currentProgramFile.symbolTable.addSymbol(this, SymbolType.LOCAL_CLASS, localClassDecl.classDecl().id().id(), new ClassSymbolImpl(localClassDecl.classDecl()));
    }

    public Symbol getSymbol(SymbolType type, String id) {
        return currentProgramFile.symbolTable.getSymbol(this, type, id);
    }

    public Symbol getSymbol(String id) {
        return currentProgramFile.symbolTable.getSymbol(this, SymbolType.ANY, id);
    }

    public Object invokeMethod(MethodDecl methodDecl, Object[] args) {
        if (args.length != methodDecl.params().size()) {
            throw new IllegalArgumentException("Invalid number of arguments. Expected: " + methodDecl.params().size() + ", got: " + args.length);
        }
        List<ParameterIdentifier> params = methodDecl.params();
        for (int i = 0; i < params.size(); i++) {
            var param = params.get(i);
            currentProgramFile.symbolTable.addSymbol(this, SymbolType.PARAMETER, param.id(), new VariableSymbolImpl(this, param.id(), args[i]));
        }
        return methodDecl.body().accept(this);
    }

    public Object visitExpr(Expr expr) {
        if (expr == null) {
            return null;
        }
        return expr.accept(this);
    }

    public Object memberRefExpr(Object refValue, Object memberValue) {
        // TODO
        throw new UnsupportedOperationException("Member reference expression not implemented yet");
    }

    public Object memberAccessExpr(Object targetValue, Object memberValue) {
        // TODO
        throw new UnsupportedOperationException("Member access expression not implemented yet");
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
    }
}
