package net.ingoh.myagents.lang;

import net.ingoh.myagents.lang.il.ILNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.UUID;

public class DSL2IL {

    public static Path string2Il(String content, Class<?> targetClass) {
        assert content != null : "Content cannot be null";
        assert targetClass != null : "Target class cannot be null";
        Path outDir = Paths.get("./out/il/");
        Path outFile = outDir.resolve(UUID.randomUUID() + ".il");
        ILNode result = DSLParser.parse(content, targetClass);
        try {
            ILSerializer.saveToFile(outFile, result);
        } catch (Exception e) {
            throw new RuntimeException("Error saving to file: " + e.getMessage(), e);
        }
        assert result != null : "Deserialization failed";
        return outFile;
    }

    public static Path file2Il(Path file, Class<?> targetClass) {
        assert file != null : "File cannot be null";
        assert Files.exists(file) : "File does not exist";
        assert targetClass != null : "Target class cannot be null";
        String content;
        try {
            content = Files.readString(file);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + e.getMessage(), e);
        }
        assert content != null && !content.isEmpty() : "Invalid content";
        return string2Il(content, targetClass);
    }

    public static void clean() {
        Path outDir = Paths.get("./out/il/");
        if (Files.exists(outDir)) {
            try (var paths = Files.walk(outDir)) {
                paths.sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                throw new RuntimeException("Error deleting file: " + e.getMessage(), e);
                            }
                        });
            } catch (IOException e) {
                throw new RuntimeException("Error walking through directory: " + e.getMessage(), e);
            }
        }
    }
}