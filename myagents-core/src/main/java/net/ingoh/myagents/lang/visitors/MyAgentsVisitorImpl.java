package net.ingoh.myagents.lang.visitors;

import net.ingoh.myagents.lang.il.*;
import net.ingoh.myagents.lang.internal.MyAgentsBaseVisitor;
import net.ingoh.myagents.lang.internal.MyAgentsParser;

import java.util.LinkedList;
import java.util.List;

public class MyAgentsVisitorImpl extends MyAgentsBaseVisitor<Object> {

    @Override
    public ProgramDecl visitProgram(MyAgentsParser.ProgramContext ctx) {
        List<TopLevelDecl> topLevelDecls = new LinkedList<>();
        var packageDecl = ctx.packageDecl();
        if (packageDecl != null) {
            topLevelDecls.add(visitPackageDecl(packageDecl));
        }
        if (ctx.importDecl() != null) {
            for (var importDecl : ctx.importDecl()) {
                topLevelDecls.add(visitImportDecl(importDecl));
            }
        }
        if (ctx.overrideBodyDecl() != null) {
            topLevelDecls.add(visitOverrideBodyDecl(ctx.overrideBodyDecl()));
        }
        if (ctx.classDecl() != null) {
            for (var classDecl : ctx.classDecl()) {
                topLevelDecls.add(visitClassDecl(classDecl));
            }
        }
        return new ProgramDecl(topLevelDecls);
    }

    @Override
    public PackageDecl visitPackageDecl(MyAgentsParser.PackageDeclContext ctx) {
        String packageName = ctx.qualifiedName().getText();
        return new PackageDecl(new NamespaceIdentifier(packageName));
    }

    @Override
    public ImportDecl visitImportDecl(MyAgentsParser.ImportDeclContext ctx) {
        String importName = ctx.qualifiedName().getText();
        boolean isStatic = ctx.STATIC() != null;
        return new ImportDecl(isStatic, new NamespaceIdentifier(importName));
    }

    @Override
    public OverrideBodyDecl visitOverrideBodyDecl(MyAgentsParser.OverrideBodyDeclContext ctx) {
        List<ClassBodyDecl> bodyDecls = new LinkedList<>();
        for (var bodyDecl : ctx.classBodyDecl()) {
            bodyDecls.add(visitClassBodyDecl(bodyDecl));
        }
        return new OverrideBodyDecl(bodyDecls);
    }

    @Override
    public ClassDecl visitClassDecl(MyAgentsParser.ClassDeclContext ctx) {
        String className = ctx.id().getText();
        List<ClassBodyDecl> bodyDecls = new LinkedList<>();
        for (var bodyDecl : ctx.classBody().classBodyDecl()) {
            bodyDecls.add(visitClassBodyDecl(bodyDecl));
        }
        return new ClassDecl(className, bodyDecls);
    }

    @Override
    public ClassBodyDecl visitClassBodyDecl(MyAgentsParser.ClassBodyDeclContext ctx) {
        if (ctx.block() != null) {
            return visitBlock(ctx.block());
        } else if (ctx.memberDecl() != null) {
            return visitMemberDecl(ctx.memberDecl());
        }
        throw new IllegalArgumentException("Unknown class body declaration type");
    }

    @Override
    public Block visitBlock(MyAgentsParser.BlockContext ctx) {
        List<BlockStmt> statements = new LinkedList<>();
        for (var statement : ctx.blockStmt()) {
            statements.add(visitBlockStmt(statement));
        }
        return new Block(statements);
    }

    @Override
    public BlockStmt visitBlockStmt(MyAgentsParser.BlockStmtContext ctx) {
        if (ctx.stmt() != null) {
            return visitStmt(ctx.stmt());
        } else if (ctx.localVariableDecl() != null) {
            return visitLocalVariableDecl(ctx.localVariableDecl());
        } else if (ctx.localClassDecl() != null) {
            return visitLocalClassDecl(ctx.localClassDecl());
        }
        throw new IllegalArgumentException("Unknown block statement type");
    }

    @Override
    public Stmt visitStmt(MyAgentsParser.StmtContext ctx) {
        if (ctx.blockLabel != null) {
            return visitBlockLabel(ctx.blockLabel);
        } else if (ctx.ifStmt != null) {
            return visitIfStmt(ctx.ifStmt);
        } else if (ctx.forStmt != null) {
            return visitForStmt(ctx.forStmt);
        } else if (ctx.forEachStmt != null) {
            return visitForEachStmt(ctx.forEachStmt);
        } else if (ctx.whileStmt != null) {
            return visitWhileStmt(ctx.whileStmt);
        } else if (ctx.doStmt != null) {
            return visitDoStmt(ctx.doStmt);
        } else if (ctx.tryStmt != null) {
            return visitTryStmt(ctx.tryStmt);
        } else if (ctx.switchStmt != null) {
            return visitSwitchStmt(ctx.switchStmt);
        } else if (ctx.switchExprStatement != null) {
            return visitSwitchExprStatement(ctx.switchExprStatement);
        } else if (ctx.returnStmt != null) {
            return visitReturnStmt(ctx.returnStmt);
        } else if (ctx.throwStmt != null) {
            return visitThrowStmt(ctx.throwStmt);
        } else if (ctx.breakStmt != null) {
            return visitBreakStmt(ctx.breakStmt);
        } else if (ctx.continueStmt != null) {
            return visitContinueStmt(ctx.continueStmt);
        } else if (ctx.exprStmt != null) {
            return visitExprStmt(ctx.exprStmt);
        } else if (ctx.idLabel != null) {
            return visitIdLabel(ctx.idLabel);
        }
    }
}
