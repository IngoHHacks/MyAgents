package net.ingoh.myagents.lang.visitors;

import net.ingoh.myagents.lang.il.*;
import net.ingoh.myagents.lang.internal.MyAgentsBaseVisitor;
import net.ingoh.myagents.lang.internal.MyAgentsLexer;
import net.ingoh.myagents.lang.internal.MyAgentsParser;
import org.antlr.v4.runtime.RuleContext;

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
        var cls = ctx.specialDecl().stream().filter(spcDecl -> spcDecl.overrideTypeDecl() != null).findFirst().orElse(null);
        var className = "";
        if (cls != null) {
             className = cls.overrideTypeDecl().id().getText();
        }
        for (var bodyDecl : ctx.classBodyDecl()) {
            var decls = visitClassBodyDecl(bodyDecl, new TypeIdentifier(className));
            if (decls != null) {
                for (var d : decls) {
                    bodyDecls.add(d);
                }
            }
        }
        var name = "";
        var type = "";
        List<String> agents = new LinkedList<String>();
        for (var spcDecl : ctx.specialDecl()) {
            if (spcDecl.overrideNameDecl() != null) {
                name = spcDecl.overrideNameDecl().id().getText();
            } else if (spcDecl.overrideTypeDecl() != null) {
                type = spcDecl.overrideTypeDecl().id().getText();
            } else if (spcDecl.agentsDecl() != null) {
                agents = spcDecl.agentsDecl().id().stream().map(RuleContext::getText).toList();
            }
        }
        return new OverrideBodyDecl(bodyDecls, type, name, agents);
    }

    @Override
    public ClassDecl visitClassDecl(MyAgentsParser.ClassDeclContext ctx) {
        String className = ctx.id().getText();
        List<ClassBodyDecl> bodyDecls = new LinkedList<>();
        for (var bodyDecl : ctx.classBody().classBodyDecl()) {
            var decl = visitClassBodyDecl(bodyDecl, new TypeIdentifier(className));
            if (decl != null) {
                for (var d : decl) {
                    bodyDecls.add(d);
                }
            }
        }
        return new ClassDecl(new TypeIdentifier(className), bodyDecls);
    }

    public List<ClassBodyDecl> visitClassBodyDecl(MyAgentsParser.ClassBodyDeclContext ctx, TypeIdentifier rootType) {
        if (ctx.block() != null) {
            return List.of(visitBlock(ctx.block()));
        }
        if (ctx.memberDecl() != null) {
            return visitMemberDecl(ctx.memberDecl(), rootType).stream().map(decl -> (ClassBodyDecl) decl).toList();
        }
        return null;
    }

    @Override
    public Block visitBlock(MyAgentsParser.BlockContext ctx) {
        List<BlockStmt> statements = new LinkedList<>();
        for (var statement : ctx.blockStmt()) {
            var stmts = visitBlockStmt(statement);
            for (var stmt : stmts) {
                statements.add(stmt);
            }
        }
        return new Block(statements);
    }

    @Override
    public List<BlockStmt> visitBlockStmt(MyAgentsParser.BlockStmtContext ctx) {
        if (ctx.stmt() != null) {
            return List.of(visitStmt(ctx.stmt()));
        }
		if (ctx.localVariableDecl() != null) {
            return visitLocalVariableDecl(ctx.localVariableDecl()).stream().map(decl -> (BlockStmt) decl).toList();
        }
		if (ctx.localClassDecl() != null) {
            return List.of(new LocalClassDecl(visitClassDecl(ctx.localClassDecl().classDecl())));
        }
        throw new IllegalArgumentException("Unknown block statement type");
    }

    @Override
    public List<LocalVariableDecl> visitLocalVariableDecl(MyAgentsParser.LocalVariableDeclContext ctx) {
        Expr expr;
        if (ctx.expr() != null) {
            expr = visitExpr(ctx.expr());
        } else {
            expr = null;
        }
        List<LocalVariableDecl> vars = new LinkedList<>();
        for (var id : ctx.id()) {
            vars.add(new LocalVariableDecl(new LocalVariableIdentifier(id.getText()), expr));
        }
        return vars;
    }

    public List<MemberDecl> visitMemberDecl(MyAgentsParser.MemberDeclContext ctx, TypeIdentifier rootType) {
        if (ctx.methodDecl() != null) {
            return List.of(visitMethodDecl(ctx.methodDecl()));
        }
		if (ctx.fieldDecl() != null) {
            return visitFieldDecl(ctx.fieldDecl()).stream().map(decl -> (MemberDecl) decl).toList();
        }
		if (ctx.constructorDecl() != null) {
            return List.of(visitConstructorDecl(ctx.constructorDecl(), rootType));
        }
		if (ctx.classDecl() != null) {
            return List.of(visitClassDecl(ctx.classDecl()));
        }
        throw new IllegalArgumentException("Unknown member declaration type");
    }

    @Override
    public MethodDecl visitMethodDecl(MyAgentsParser.MethodDeclContext ctx) {
        return new MethodDecl(new MethodIdentifier(ctx.id().getText()), visitParamList(ctx.paramList()), visitBlock(ctx.methodBody().block()));
    }

    @Override
    public List<FieldDecl> visitFieldDecl(MyAgentsParser.FieldDeclContext ctx) {
        Expr expr;
        if (ctx.expr() != null) {
            expr = visitExpr(ctx.expr());
        } else {
            expr = null;
        }
        List<FieldDecl> fields = new LinkedList<>();
        for (var id : ctx.id()) {
            fields.add(new FieldDecl(new FieldIdentifier(id.getText()), expr));
        }
        return fields;
    }

    public ConstructorDecl visitConstructorDecl(MyAgentsParser.ConstructorDeclContext ctx, TypeIdentifier rootType) {
        return new ConstructorDecl(new TypeIdentifier(ctx.id().getText()), rootType, visitParamList(ctx.paramList()), visitBlock(ctx.constructorBody));
    }

    @Override
    public List<ParameterIdentifier> visitParamList(MyAgentsParser.ParamListContext ctx) {
        if (ctx == null) {
            return new LinkedList<>();
        }
        List<ParameterIdentifier> params = new LinkedList<>();
        for (var param : ctx.id()) {
            params.add(new ParameterIdentifier(param.getText()));
        }
        return params;
    }

    @Override
    public Stmt visitStmt(MyAgentsParser.StmtContext ctx) {
        if (ctx == null) {
            return null;
        }
        if (ctx.blockLabel != null) {
            return visitBlock(ctx.blockLabel);
        }
		if (ctx.ifStmt() != null) {
            return visitIfStmt(ctx.ifStmt());
        }
		if (ctx.forStmt() != null) {
            return visitForStmt(ctx.forStmt());
        }
		if (ctx.forEachStmt() != null) {
            return visitForEachStmt(ctx.forEachStmt());
        }
		if (ctx.whileStmt() != null) {
            return visitWhileStmt(ctx.whileStmt());
        }
		if (ctx.doStmt() != null) {
            return visitDoStmt(ctx.doStmt());
        }
		if (ctx.tryStmt() != null) {
            return visitTryStmt(ctx.tryStmt());
        }
		if (ctx.switchStmt() != null) {
            return visitSwitchStmt(ctx.switchStmt());
        }
		if (ctx.switchExprStatement() != null) {
            return visitSwitchExpr(ctx.switchExprStatement().switchExpr());
        }
		if (ctx.returnStmt() != null) {
            return visitReturnStmt(ctx.returnStmt());
        }
		if (ctx.throwStmt() != null) {
            return visitThrowStmt(ctx.throwStmt());
        }
		if (ctx.breakStmt() != null) {
            return visitBreakStmt(ctx.breakStmt());
        }
		if (ctx.continueStmt() != null) {
            return visitContinueStmt(ctx.continueStmt());
        }
		if (ctx.exprStmt() != null) {
            return visitExprStmt(ctx.exprStmt());
        }
		if (ctx.idLabel() != null) {
            return visitIdLabel(ctx.idLabel());
        }
        throw new IllegalArgumentException("Unknown statement type: " + ctx.getText());
    }

    @Override
    public IfStmt visitIfStmt(MyAgentsParser.IfStmtContext ctx) {
        return new IfStmt(visitExpr(ctx.parExpr().expr()), visitStmt(ctx.thenStmt), visitStmt(ctx.elseStmt));
    }

    @Override
    public ForStmt visitForStmt(MyAgentsParser.ForStmtContext ctx) {
        var ctrl = ctx.forControl();
        return new ForStmt(visitForInit(ctrl.forInit()), visitExpr(ctrl.expr()), visitExprList(ctrl.forUpdate), visitStmt(ctx.stmt()));
    }

    @Override
    public ForInit visitForInit(MyAgentsParser.ForInitContext ctx) {
        if (ctx.localVariableDecl() != null) {
            var localInit = visitLocalVariableDecl(ctx.localVariableDecl());
            if (localInit.size() > 1) {
                throw new IllegalArgumentException("For loop can only have one local variable declaration");
            }
            return localInit.get(0);
        }
		if (ctx.exprList() != null) {
            return visitExprList(ctx.exprList());
        }
        throw new IllegalArgumentException("Unknown for initialization type");
    }

    @Override
    public ExprList visitExprList(MyAgentsParser.ExprListContext ctx) {
        if (ctx == null) {
            return new ExprList(new LinkedList<>());
        }
        List<Expr> exprs = new LinkedList<>();
        for (var expr : ctx.expr()) {
            exprs.add(visitExpr(expr));
        }
        return new ExprList(exprs);
    }

    @Override
    public ForEachStmt visitForEachStmt(MyAgentsParser.ForEachStmtContext ctx) {
        return new ForEachStmt(new AnyVariableIdentifier(ctx.forEachControl().id().getText()), visitExpr(ctx.forEachControl().expr()), visitStmt(ctx.stmt()));
    }

    @Override
    public WhileStmt visitWhileStmt(MyAgentsParser.WhileStmtContext ctx) {
        return new WhileStmt(visitExpr(ctx.parExpr().expr()), visitStmt(ctx.stmt()));
    }

    @Override
    public DoStmt visitDoStmt(MyAgentsParser.DoStmtContext ctx) {
        return new DoStmt(visitExpr(ctx.parExpr().expr()), visitStmt(ctx.stmt()));
    }

    @Override
    public TryStmt visitTryStmt(MyAgentsParser.TryStmtContext ctx) {
        Block catchBlock = null;
        Block finallyBlock = null;
        if (ctx.catchClause() != null && ctx.catchClause().block() != null) {
            catchBlock = visitBlock(ctx.catchClause().block());
        }
        if (ctx.finallyBlock() != null && ctx.finallyBlock().block() != null) {
            finallyBlock = visitBlock(ctx.finallyBlock().block());
        }
        return new TryStmt(visitBlock(ctx.block()), catchBlock, finallyBlock);
    }

    @Override
    public SwitchStmt visitSwitchStmt(MyAgentsParser.SwitchStmtContext ctx) {
        List<Case> cases = new LinkedList<>();
        List<BlockStmt> defaultBody = new LinkedList<>();
        for (var switchCase : ctx.switchBlockStmtGroup()) {
            List<Expr> constExprs = new LinkedList<>();
            List<VariableIdentifier> variableIdentifiers = new LinkedList<>();
            for (var label : switchCase.switchLabel()) {
                if (label.constantExpr != null) {
                    constExprs.add(visitExpr(label.constantExpr));
                }
		        if (label.id() != null) {
                    variableIdentifiers.add(new AnyVariableIdentifier(label.id().getText()));
                }
		        if (label.DEFAULT() != null) {
                    for (var blockStmt : switchCase.blockStmt()) {
                        var stmts = visitBlockStmt(blockStmt);
                        for (var stmt : stmts) {
                            defaultBody.add(stmt);
                        }
                    }
                } else {
                    throw new IllegalArgumentException("Unknown switch label type");
                }
            }
            List<BlockStmt> statements = new LinkedList<>();
            for (var blockStmt : switchCase.blockStmt()) {
                var stmts = visitBlockStmt(blockStmt);
                for (var stmt : stmts) {
                    statements.add(stmt);
                }
            }
            cases.add(new Case(new SwitchLabel(constExprs, variableIdentifiers), statements));
        }
        return new SwitchStmt(visitExpr(ctx.parExpr().expr()), cases, defaultBody);
    }

    @Override
    public SwitchExpr visitSwitchExpr(MyAgentsParser.SwitchExprContext ctx) {
        var expr = visitExpr(ctx.parExpr().expr());
        List<Case> cases = new LinkedList<>();
        List<BlockStmt> defaultBody = new LinkedList<>();
        for (var switchCase : ctx.switchLabeledRule()) {
            List<BlockStmt> statements = new LinkedList<>();
            if (switchCase.switchRuleOutcome().blockStmt() != null) {
                for (var blockStmt : switchCase.switchRuleOutcome().blockStmt()) {
                    var stmts = visitBlockStmt(blockStmt);
                    for (var stmt : stmts) {
                        statements.add(stmt);
                    }
                }
            } else if (switchCase.switchRuleOutcome().block() != null) {
                statements.add(visitBlock(switchCase.switchRuleOutcome().block()));
            }
            List<Expr> exprList = List.of(visitExpr(switchCase.expr()));
            cases.add(new Case(new SwitchLabel(exprList, new LinkedList<>()), statements));
        }
        return new SwitchExpr(expr, cases, defaultBody);
    }

    @Override
    public ReturnStmt visitReturnStmt(MyAgentsParser.ReturnStmtContext ctx) {
        return new ReturnStmt(visitExpr(ctx.expr()));
    }

    @Override
    public ThrowStmt visitThrowStmt(MyAgentsParser.ThrowStmtContext ctx) {
        return new ThrowStmt(visitExpr(ctx.expr()));
    }

    @Override
    public BreakStmt visitBreakStmt(MyAgentsParser.BreakStmtContext ctx) {
        return new BreakStmt(ctx.id() != null ? new LabelIdentifier(ctx.id().getText()) : null);
    }

    @Override
    public ContinueStmt visitContinueStmt(MyAgentsParser.ContinueStmtContext ctx) {
        return new ContinueStmt(ctx.id() != null ? new LabelIdentifier(ctx.id().getText()) : null);
    }

    @Override
    public ExprStmt visitExprStmt(MyAgentsParser.ExprStmtContext ctx) {
        return new ExprStmt(visitExpr(ctx.expr()));
    }

    @Override
    public IdLabel visitIdLabel(MyAgentsParser.IdLabelContext ctx) {
        return new IdLabel(new LabelIdentifier(ctx.id().getText()));
    }

    @Override
    public Expr visitExpr(MyAgentsParser.ExprContext ctx) {
        if (ctx.primary() != null) {
            return visitPrimary(ctx.primary());
        }
		if (ctx.anyRef() != null) {
            return visitAnyRef2(visitExpr(ctx.expr(0)), ctx.anyRef());
        }
		if (ctx.methodCall() != null) {
            return visitMethodCall(ctx.methodCall());
        }
		if (ctx.switchExpr() != null) {
            return visitSwitchExpr(ctx.switchExpr());
        }
		if (ctx.postfix != null) {
            var which = ctx.postfix;
            if (which.getType() == MyAgentsLexer.INC) {
                return new PostfixIncrementExpr(visitExpr(ctx.expr(0)));
            }
		    if (which.getType() == MyAgentsLexer.DEC) {
                return new PostfixDecrementExpr(visitExpr(ctx.expr(0)));
            }
            throw new IllegalArgumentException("Unknown postfix operator: " + which.getText());
        }
        if (ctx.prefix != null) {
            var which = ctx.prefix;
            if (which.getType() == MyAgentsLexer.INC) {
                return new PrefixIncrementExpr(visitExpr(ctx.expr(0)));
            }
			if (which.getType() == MyAgentsLexer.DEC) {
                return new PrefixDecrementExpr(visitExpr(ctx.expr(0)));
            }
            if (which.getType() == MyAgentsLexer.PLUS) {
                return new UnaryPlusExpr(visitExpr(ctx.expr(0)));
            }
            if (which.getType() == MyAgentsLexer.MINUS) {
                return new UnaryMinusExpr(visitExpr(ctx.expr(0)));
            }
            if (which.getType() == MyAgentsLexer.NOT) {
                return new NotExpr(visitExpr(ctx.expr(0)));
            }
            if (which.getType() == MyAgentsLexer.BITNOT) {
                return new BitwiseNotExpr(visitExpr(ctx.expr(0)));
            }
            throw new IllegalArgumentException("Unknown prefix operator: " + which.getText());
        }
		if (ctx.creator() != null) {
            return visitCreator(ctx.creator());
        }
		if (ctx.mop != null) {
            var which = ctx.mop;
            if (which.getType() == MyAgentsLexer.MULT) {
                return new MultExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
            }
		    if (which.getType() == MyAgentsLexer.DIV) {
                return new DivExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
            }
		    if (which.getType() == MyAgentsLexer.MOD) {
                return new ModExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
            }
            throw new IllegalArgumentException("Unknown multiplication operator: " + which.getText());
        }
		if (ctx.aop != null) {
            var which = ctx.aop;
            if (which.getType() == MyAgentsLexer.PLUS) {
                return new AddExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
            }
		    if (which.getType() == MyAgentsLexer.MINUS) {
                return new SubExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
            }
            throw new IllegalArgumentException("Unknown arithmetic operator: " + which.getText());
        }
		if (ctx.sop != null) {
            var which = ctx.sop;
            if (which.getType() == MyAgentsLexer.LSHIFT) {
                return new ShiftLeftExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
            }
		    if (which.getType() == MyAgentsLexer.RSHIFT) {
                return new ShiftRightExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
            }
		    if (which.getType() == MyAgentsLexer.URSHIFT) {
                return new ShiftRightUnsignedExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
            }
            throw new IllegalArgumentException("Unknown shift operator: " + which.getText());
        }
		if (ctx.rop != null) {
            var which = ctx.rop;
            if (which.getType() == MyAgentsLexer.LT) {
                return new LessThanExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
            }
		    if (which.getType() == MyAgentsLexer.GT) {
                return new GreaterThanExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
            }
		    if (which.getType() == MyAgentsLexer.LE) {
                return new LessThanOrEqualExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
            }
		    if (which.getType() == MyAgentsLexer.GE) {
                return new GreaterThanOrEqualExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
            }
            throw new IllegalArgumentException("Unknown relational operator: " + which.getText());
        }
		if (ctx.eop != null) {
            var which = ctx.eop;
            if (which.getType() == MyAgentsLexer.EQ) {
                return new EqualityExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
            }
		    if (which.getType() == MyAgentsLexer.NE) {
                return new InequalityExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
            }
            throw new IllegalArgumentException("Unknown equality operator: " + which.getText());
        }
		if (ctx.baop != null) {
            return new BitAndExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
        }
		if (ctx.bxop != null) {
            return new BitXorExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
        }
		if (ctx.boop != null) {
            return new BitOrExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
        }
		if (ctx.laop != null) {
            return new LogicAndExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
        }
		if (ctx.loop != null) {
            return new LogicOrExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
        }
		if (ctx.ternary != null) {
            return new TernaryExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)), visitExpr(ctx.expr(2)));
        }
		if (ctx.assign != null) {
            return switch (ctx.assign.getType()) {
                case MyAgentsLexer.ASSIGN -> new AssignExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
                case MyAgentsLexer.ADD_ASSIGN -> new PlusAssignExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
                case MyAgentsLexer.SUB_ASSIGN -> new MinusAssignExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
                case MyAgentsLexer.MUL_ASSIGN -> new MultAssignExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
                case MyAgentsLexer.DIV_ASSIGN -> new DivAssignExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
                case MyAgentsLexer.MOD_ASSIGN -> new ModAssignExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
                case MyAgentsLexer.AND_ASSIGN -> new BitAndAssignExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
                case MyAgentsLexer.OR_ASSIGN -> new BitOrAssignExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
                case MyAgentsLexer.XOR_ASSIGN -> new BitXorAssignExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
                case MyAgentsLexer.LSHIFT_ASSIGN -> new ShiftLeftAssignExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
                case MyAgentsLexer.RSHIFT_ASSIGN -> new ShiftRightAssignExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
                case MyAgentsLexer.URSHIFT_ASSIGN -> new ShiftRightUnsignedAssignExpr(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
                default -> throw new IllegalArgumentException("Unknown assignment operator: " + ctx.assign.getText());
            };
        }
        throw new IllegalArgumentException("Unknown expression type: " + ctx.getText());
    }

    @Override
    public PrimaryExpr visitPrimary(MyAgentsParser.PrimaryContext ctx) {
        if (ctx.parenExpr() != null) {
            return new ParenthesizedExpr(visitExpr(ctx.parenExpr().expr()));
        }
        if (ctx.THIS() != null) {
            return new ThisExpr();
        }
        if (ctx.SUPER() != null) {
            return new SuperExpr();
        }
        if (ctx.literal() != null) {
            return visitLiteral(ctx.literal());
        }
        if (ctx.varRef() != null) {
            return new RefExpr(new AnyVariableIdentifier(ctx.varRef().id().getText()));
        }
        if (ctx.clsRef() != null) {
            return new ClassRefExpr(new TypeIdentifier(ctx.clsRef().id().getText()));
        }
        if (ctx.memRef() != null) {
            return new MemberRefExpr(new MemberContainerIdentifier(ctx.memRef().id(0).getText()), new AnyMemberIdentifier(ctx.memRef().id(1).getText()));
        }
        throw new IllegalArgumentException("Unknown primary expression type: " + ctx.getText());
    }

    public Expr visitAnyRef2(Expr target, MyAgentsParser.AnyRefContext ctx) {
        if (ctx.id() != null) {
            return new MemberAccessExpr(target, new AnyMemberIdentifier(ctx.id().getText()));
        }
        if (ctx.methodCall() != null) {
            return visitMethodCall2(target, ctx.methodCall());
        }
        if (ctx.THIS() != null) {
            return new ExprThisRefExpr(target);
        }
        if (ctx.NEW() != null) {
            return new ExprNewRefExpr(target);
        }
        if (ctx.SUPER() != null) {
            return new ExprSuperRefExpr(target, visitExprList(ctx.superSuffix().arguments().exprList()));
        }
        throw new IllegalArgumentException("Unknown reference type: " + ctx.getText());
    }

    @Override
    public GlobalMethodCallExpr visitMethodCall(MyAgentsParser.MethodCallContext ctx) {
        if (ctx.id() != null) {
            return new GlobalMethodCallExpr(new AnyMemberIdentifier(ctx.id().getText()), visitExprList(ctx.arguments().exprList()));
        }
        if (ctx.THIS() != null) {
            return new GlobalMethodCallExpr(new ThisExpr(), visitExprList(ctx.arguments().exprList()));
        }
        if (ctx.SUPER() != null) {
            return new GlobalMethodCallExpr(new SuperExpr(), visitExprList(ctx.arguments().exprList()));
        }
        throw new IllegalArgumentException("Unknown method call type: " + ctx.getText());
    }

    public MethodCallExpr visitMethodCall2(Expr target, MyAgentsParser.MethodCallContext ctx) {
        ExprList args = null;
        if (ctx.arguments() != null && ctx.arguments().exprList() != null) {
            args = visitExprList(ctx.arguments().exprList());
        }
        if (ctx.id() != null) {
            return new MethodCallExpr(target, new AnyMemberIdentifier(ctx.id().getText()), args);
        }
        if (ctx.THIS() != null) {
            return new MethodCallExpr(target, new ThisExpr(), args);
        }
        if (ctx.SUPER() != null) {
            return new MethodCallExpr(target, new SuperExpr(), args);
        }
        throw new IllegalArgumentException("Unknown method call type: " + ctx.getText());
    }

    @Override
    public Expr visitCreator(MyAgentsParser.CreatorContext ctx) {
        var name = ctx.createdName().getText();
        if (ctx.classCreatorRest() != null) {
            var body = new LinkedList<ClassBodyDecl>();
            if (ctx.classCreatorRest().classBody() != null) {
                for (var bodyDecl : ctx.classCreatorRest().classBody().classBodyDecl()) {
                    var decls = visitClassBodyDecl(bodyDecl, new TypeIdentifier(name));
                    if (decls != null) {
                        for (var d : decls) {
                            body.add(d);
                        }
                    }
                }
            }
            ExprList exprs = new ExprList(new LinkedList<>());
            if (ctx.classCreatorRest().arguments() != null && ctx.classCreatorRest().arguments().exprList() != null) {
                exprs = visitExprList(ctx.classCreatorRest().arguments().exprList());
            }
            return new ObjectCreationExpr(new TypeIdentifier(name), exprs, body);
        }
        if (ctx.arrayCreatorRest() != null) {
            var dimensions = new LinkedList<Expr>();
            for (var dim : ctx.arrayCreatorRest().expr()) {
                dimensions.add(visitExpr(dim));
            }
            return new ArrayCreationExpr(new AnyVariableIdentifier(name), dimensions);
        }
        throw new IllegalArgumentException("Unknown creator type: " + ctx.getText());
    }

    @Override
    public LiteralExpr visitLiteral(MyAgentsParser.LiteralContext ctx) {
        if (ctx.INT_LITERAL() != null) {
            return new IntLiteralExpr(Integer.parseInt(ctx.INT_LITERAL().getText()));
        }
        if (ctx.FLOAT_LITERAL() != null) {
            return new FloatLiteralExpr(Float.parseFloat(ctx.FLOAT_LITERAL().getText()));
        }
        if (ctx.STRING_LITERAL() != null) {
            return new StringLiteralExpr(ctx.STRING_LITERAL().getText());
        }
        if (ctx.BOOL_LITERAL() != null) {
            return new BooleanLiteralExpr(Boolean.parseBoolean(ctx.BOOL_LITERAL().getText()));
        }
        if (ctx.CHAR_LITERAL() != null) {
            return new CharLiteralExpr(ctx.CHAR_LITERAL().getText().charAt(0));
        }
        if (ctx.NULL_LITERAL() != null) {
            return new NullLiteralExpr();
        }
        if (ctx.TEXT_BLOCK() != null) {
            return new TextBlockLiteralExpr(ctx.TEXT_BLOCK().getText());
        }
        throw new IllegalArgumentException("Unknown literal type: " + ctx.getText());
    }
}