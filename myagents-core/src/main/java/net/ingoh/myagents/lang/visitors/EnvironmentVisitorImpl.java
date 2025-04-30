package net.ingoh.myagents.lang.visitors;

import net.ingoh.myagents.lang.il.*;
import net.ingoh.myagents.lang.internal.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class EnvironmentVisitorImpl extends EnvironmentBaseVisitor<Object> implements HasRootVisitor<EnvironmentIL, EnvironmentParser.ProgramContext> {

    private Dictionary<String, VariableIL> variables = new Hashtable<>();

    @Override
    public EnvironmentIL visitProgram(EnvironmentParser.ProgramContext ctx) {
        EnvironmentIL il = new EnvironmentIL();
        il.name = ctx.nameDecl().get(0).ID().getText();
        il.tickRate = Float.parseFloat(ctx.tickRateDecl().get(0).number().getText());
        il.agents = ctx.agentsDecl().get(0).ID().stream()
                .map(ParseTree::getText).toList();
        il.tickMethod = visitTickMethodDecl(ctx.tickMethodDecl().get(0));
        return il;
    }

    @Override
    public MethodIL visitTickMethodDecl(EnvironmentParser.TickMethodDeclContext ctx) {
        MethodIL methodIL = new MethodIL();
        methodIL.name = "tick";
        methodIL.returnType = "boolean";
        methodIL.parameters = List.of(ParameterIL.builder()
                .name(ctx.ID().getText())
                .type("float")
                .build());
        methodIL.statements = visitBlock(ctx.block());
        return methodIL;
    }

    @Override
    public List<StatementIL> visitBlock(EnvironmentParser.BlockContext ctx) {
        return ctx.statement().stream()
                .map(this::visitStatement)
                .toList();
    }

    @Override
    public StatementIL visitStatement(EnvironmentParser.StatementContext ctx) {
        if (ctx.printStatement() != null) {
            PrintStatementIL printStatementIL = new PrintStatementIL();
            printStatementIL.expr = visitExpression(ctx.printStatement().expression());
            return printStatementIL;
        }
        throw new UnsupportedOperationException("Unsupported statement type: " + ctx.getClass().getSimpleName());
    }

    @Override
    public ExpressionIL visitExpression(EnvironmentParser.ExpressionContext ctx) {
        if (ctx.STRING() != null) {
            return new StringLiteralIL(ctx.STRING().getText());
        }
        if (ctx.ID() != null) {
            String id = ctx.ID().getText();
            VariableIL variable = variables.get(id);
            if (variable == null) {
                throw new RuntimeException("Variable " + id + " not declared");
            }
            return variable;
        }
        throw new UnsupportedOperationException("Unsupported expression type: " + ctx.getClass().getSimpleName());
    }

    @Override
    public EnvironmentIL root(EnvironmentParser.ProgramContext ctx) {
        return visitProgram(ctx);
    }
}
