package net.ingoh.myagents.lang;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import net.ingoh.myagents.core.EnvironmentImpl;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaGenerator {
    public static <T> Path generateJavaCode(T il, Path outPath) {
        assert il != null : "ILNode cannot be null";
        assert outPath != null : "Output path cannot be null";
        String code;
        Path outFile;
        try {
            if (il instanceof EnvironmentIL environmentIL) {
                code = generateEnvironmentCode(environmentIL);
                outFile = outPath.resolve(environmentIL.name + ".java");
            } else {
                throw new UnsupportedOperationException("Unsupported ILNode type: " + il.getClass().getName());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error generating Java code: " + e.getMessage(), e);
        }
        try {
            Files.createDirectories(outPath);
            Files.writeString(outFile, code);
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file: " + e.getMessage(), e);
        }
        assert Files.exists(outFile) : "File was not created";
        return outFile;
    }

    private static String generateEnvironmentCode(EnvironmentIL il) {
        TypeSpec.Builder cls = TypeSpec.classBuilder(il.name)
                .addModifiers(Modifier.PUBLIC)
                .superclass(EnvironmentImpl.class);

        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);

        if (il.tickRate != null) {
            constructor.addStatement("this.tickRate = $Lf", il.tickRate);
        }

        for (String agent : il.agents) {
            constructor.addStatement("agents.add($L.class)", agent);
        }

        cls.addMethod(constructor.build());

        if (il.tickMethod != null) {
            MethodSpec.Builder tickMethod = MethodSpec.methodBuilder("tick")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(boolean.class)
                    .addParameter(float.class, il.tickMethod.parameters.get(0).name);

            tickMethod.addStatement("super.tick($L)", il.tickMethod.parameters.get(0).name);
            for (StatementIL statement : il.tickMethod.statements) {
                tickMethod.addStatement(generateStatementCode(statement));
            }
            tickMethod.addStatement("return true");

            cls.addMethod(tickMethod.build());
        }

        return JavaFile.builder("net.ingoh.myagents.example", cls.build())
                .addFileComment("Generated code, do not edit.")
                .build()
                .toString();
    }

    private static String generateStatementCode(StatementIL il) {
        if (il instanceof PrintStatementIL printStatement) {
            return "System.out.println(" + generateExpressionCode(printStatement.expr) + ")";
        }
        throw new UnsupportedOperationException("Unsupported statement type: " + il.getClass().getName());
    }

    private static String generateExpressionCode(ExpressionIL expr) {
        if (expr instanceof StringLiteralIL stringLiteral) {
            if (stringLiteral.value.startsWith("\"") && stringLiteral.value.endsWith("\"")) {
                return stringLiteral.value;
            } else {
                return "\"" + stringLiteral.value + "\"";
            }
        }
        if (expr instanceof VariableIL variable) {
            return variable.name;
        }
        throw new UnsupportedOperationException("Unsupported expression type: " + expr.getClass().getName());
    }
}
