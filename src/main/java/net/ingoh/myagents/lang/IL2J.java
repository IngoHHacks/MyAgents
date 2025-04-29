package net.ingoh.myagents.lang;

import com.fasterxml.jackson.databind.JsonNode;
import net.ingoh.myagents.lang.il.EnvironmentIL;
import net.ingoh.myagents.lang.il.ILNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class IL2J {

    public static <T extends ILNode> Path json2J(JsonNode content, Class<T> targetClass) {
        assert content != null : "Content cannot be null";
        assert targetClass != null : "Target class cannot be null";
        Path outDir = Path.of("./out/java/");
        T result;
        try {
            result = ILSerializer.deserialize(content, targetClass);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing content: " + e.getMessage(), e);
        }
        assert result != null : "Deserialization failed";
        Path outFile = JavaGenerator.generateJavaCode(result, outDir);
        return outFile;
    }

    public static <T extends ILNode> Path string2J(String content) {
        assert content != null : "Content cannot be null";
        try {
            JsonNode jsonNode = ILSerializer.deserialize(content);
            assert jsonNode != null && jsonNode.has("TYPE");
            var type = jsonNode.get("TYPE").asText();
            Class<? extends ILNode> targetClass = getILClassFromString(type);
            return json2J(jsonNode, targetClass);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing content: " + e.getMessage(), e);
        }
    }

    public static Path file2J(Path file) {
        assert file != null : "File cannot be null";
        assert Files.exists(file) : "File does not exist";
        JsonNode content;
        try {
            content = ILSerializer.loadFromFile(file);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + e.getMessage(), e);
        }
        assert content != null && content.has("TYPE") : "Invalid content";
        var type = content.get("TYPE").asText();
        Class<? extends ILNode> targetClass = getILClassFromString(type);
        return json2J(content, targetClass);
    }

    private static Class<? extends ILNode> getILClassFromString(String type) {
        return switch (type) {
            case "Environment" -> EnvironmentIL.class;
            // Add other cases for different ILNode types
            default -> throw new IllegalArgumentException("Unknown ILNode type: " + type);
        };
    }
}
