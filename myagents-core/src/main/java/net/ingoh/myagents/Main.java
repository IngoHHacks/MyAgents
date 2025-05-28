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
        var exampleDir = "examples/cars/";
        var examples = new LinkedList<String>();
        for (var file : Path.of(exampleDir).toFile().listFiles()) {
            if (file.isFile() && file.getName().endsWith(".yage")) {
                examples.add(file.getAbsolutePath());
            }
            if (file.getName().endsWith(".yenv")) {
                examples.add(0, file.getAbsolutePath());
            }
        }
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
            interpreter.run(interpreter.resolveFile("CarsEnv"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
