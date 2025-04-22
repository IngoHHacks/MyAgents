package net.ingoh.myagents.lang.generators;

import net.ingoh.myagents.lang.parsers.EnvironmentParser;
import net.ingoh.myagents.lang.parsers.EnvironmentParserBaseVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class EnvironmentGenerator extends EnvironmentParserBaseVisitor<String> {

    private String className = "UnnamedEnvironment";
    private List<String> agentNames = new LinkedList<>();
    private String tickStatement = "";
    private boolean tickBlockSeen = false;

    @Override
    public String visitProgram(EnvironmentParser.ProgramContext ctx) {
        for (ParseTree child : ctx.children) {
            if (child instanceof EnvironmentParser.TypeDeclContext) {
                // The plan is to use file extensions, so this part is not needed
            } else if (child instanceof EnvironmentParser.NameDeclContext nameDecl) {
                className = nameDecl.ID().getText();
            } else if (child instanceof EnvironmentParser.AgentsDeclContext agentsDecl) {
                for (var agent : agentsDecl.ID()) {
                    agentNames.add(agent.getText());
                }
            } else if (child instanceof EnvironmentParser.TickBlockContext tickBlock) {
                if (tickBlockSeen) {
                    throw new RuntimeException("Multiple tick blocks are not allowed.");
                }
                tickBlockSeen = true;
                visitTickBlock(tickBlock);
            }
        }
        return generateEnvironmentClass();
    }

    @Override
    public String visitTickBlock(EnvironmentParser.TickBlockContext ctx) {
        for (var statement : ctx.blockRetBool().statement()) {
            visitStatement(statement);
        }
        if (ctx.blockRetBool().RETURN() != null) {
            String returnValue = ctx.blockRetBool().BOOL_LITERAL().toString().toLowerCase(Locale.ROOT);
            tickStatement += "return " + returnValue + ";\n";
        }
        return null;
    }

    @Override
    public String visitStatement(EnvironmentParser.StatementContext ctx) {
        if (ctx.PRINT() != null) {
            String text = ctx.STRING_LITERAL().getText();
            tickStatement += "System.out.println(" + text + ");\n";
        }
        return null;
    }

    private String generateEnvironmentClass() {
        StringBuilder sb = new StringBuilder();
        sb.append("import net.ingoh.myagents.agents.core.Environment;\n");
        sb.append("import net.ingoh.myagents.agents.core.Agent;\n");
        sb.append("import java.util.*;\n");
        sb.append("public class ").append(className).append(" extends Environment {\n");
        sb.append("    public ").append(className).append("() {\n");
        for (String agentName : agentNames) {
            sb.append("        agents.add(new ").append(agentName).append("());\n");
        }
        sb.append("    }\n");
        sb.append("    @Override\n");
        sb.append("    public void tick(float time) {\n");
        sb.append("        super.tick(time);\n");
        for (String line : tickStatement.split("\n")) {
            sb.append("        ").append(line).append("\n");
        }
        sb.append("    }\n");
        sb.append("}\n");
        return sb.toString();
    }
}
