package net.ingoh.myagents;


import net.ingoh.myagents.lang.ILConverter;
import net.ingoh.myagents.lang.ILSerializer;
import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.il.ProgramDecl;

import java.nio.file.Files;

public class Main {
    public static void main(String[] args) {
        var example = """
type Environment;

init() {
    a = 10;
    b = 20;
    print(sum(a, b));
    return 0;
}

sum(a, b) {
    return a + b;
}
""";
        ILConverter.clean();
        var path = ILConverter.string2Il(example);
        try {
            var contents = Files.readString(path);
            var program = ILSerializer.deserialize(contents, ProgramDecl.class);
            assert contents.equals(ILSerializer.serialize(program));
            Interpreter interpreter = new Interpreter();
            interpreter.run(program);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
