package net.ingoh.myagents;


import net.ingoh.myagents.lang.ILConverter;
import net.ingoh.myagents.lang.ILSerializer;
import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.il.ProgramDecl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        var examples = new String[]{
                "examples/environment.yenv",
                "examples/agent1.yage",
                "examples/agent2.yage",
        };
        ILConverter.clean();
        List<ProgramDecl> programDecls = new LinkedList<>();
        try {
            for (String example : examples) {
                var path = ILConverter.string2Il(Files.readString(Path.of(example)));
                var contents = Files.readString(path);
                var program = ILSerializer.deserialize(contents, ProgramDecl.class);
                programDecls.add(program);
            }
            Interpreter interpreter = new Interpreter(programDecls);
            interpreter.run(interpreter.resolveFile("MyEnv"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
