package net.ingoh.myagents;


import net.ingoh.myagents.lang.ILConverter;
import net.ingoh.myagents.lang.ILSerializer;
import net.ingoh.myagents.lang.il.ProgramDecl;

import java.nio.file.Files;

public class Main {
    public static void main(String[] args) {
        var example = """
file2Il (file, cls) {
    String content;
    try {
        content = Files.readString(file);
    } catch {
        throw new RuntimeException("Error reading file: " + e.getMessage(), e);
    }
    assert content != null && !content.isEmpty();
    return string2Il(content);
}
""";
        ILConverter.clean();
        var path = ILConverter.string2Il(example);
        try {
            var contents = Files.readString(path);
            System.out.println(contents);
            var program = ILSerializer.deserialize(contents, ProgramDecl.class);
            assert contents.equals(ILSerializer.serialize(program));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
