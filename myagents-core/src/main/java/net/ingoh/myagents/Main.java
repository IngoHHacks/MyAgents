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
        var exampleDir = selectFile("examples");
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
            interpreter.run(interpreter.getMain());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String selectFile(String dir) {
        var folders = Path.of(dir).toFile().listFiles();
        if (folders == null || folders.length == 0) {
            throw new IllegalArgumentException("No files found in directory: " + dir);
        }
        if (folders.length == 1) {
            return folders[0].getAbsolutePath();
        }
        System.out.println("Select a file from the following options:");
        while (true) {
            var i = 0;
            for (var folder : folders) {
                System.out.println(i+1 + ": " + folder.getName());
                i++;
            }
            System.out.print("Select one by index: ");
            var input = new java.util.Scanner(System.in).next();
            var n = 0;
            if (input.matches("\\d+")) {
                n = Integer.parseInt(input);
                if (n >= 1 && n <= folders.length) {
                    return folders[n - 1].getAbsolutePath();
                } else {
                    System.out.println("Invalid index, please enter a number between 1 and " + folders.length + ".");
                }
            } else {
                System.out.println("Invalid input, please enter a number between 1 and " + folders.length + ".");
            }
        }
    }
}
