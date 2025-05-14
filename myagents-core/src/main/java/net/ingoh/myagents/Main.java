package net.ingoh.myagents;


import net.ingoh.myagents.core.Environment;
import net.ingoh.myagents.lang.DSL2IL;
import net.ingoh.myagents.lang.IL2J;

public class Main {
    public static void main(String[] args) {
        var example = """
name MyEnv;
tickRate 60;
agents MyAgent1 MyAgent2;
tick dt {
    print("Hello, world");
}
""";
        var path = DSL2IL.string2Il(example);
        IL2J.file2J(path);
        DSL2IL.clean();
    }
}
