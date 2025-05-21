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

class MathHelper {
    sum(a, b) {
        return a + b;
    }
}

class AnObject {
    a;
    b;
    
    sum() {
        return MathHelper.sum(a, b);
    }
}

init() {
    a = 10;
    b = 20;
    print(MathHelper.sum(a, b));
    obj = new AnObject();
    obj.a = 10;
    obj.b = 20;
    print(obj.sum())
    return 0;
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
