package net.ingoh.myagents.lang;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.ingoh.myagents.lang.il.ILNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ILSerializer {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T extends ILNode> String serialize(T node) throws IOException {
        assert node != null : "Node cannot be null";
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
    }

    public static <T extends ILNode> void saveToFile(Path path, T node) throws IOException {
        assert path != null : "Path cannot be null";
        assert node != null : "Node cannot be null";
        String content = serialize(node);
        Files.createDirectories(path.getParent());
        Files.writeString(path, content);
        assert Files.exists(path) : "File was not created";
    }

    public static JsonNode deserialize(String content) throws IOException {
        assert content != null : "Content cannot be null";
        assert !content.isEmpty() : "Content cannot be empty";
        return objectMapper.readTree(content);
    }

    public static <T extends ILNode> T deserialize(String content, Class<T> cls) throws IOException {
        assert content != null : "Content cannot be null";
        assert !content.isEmpty() : "Content cannot be empty";
        assert cls != null : "Class cannot be null";
        return objectMapper.readValue(content, cls);
    }

    public static <T extends ILNode> T deserialize(JsonNode node, Class<T> cls) throws IOException {
        assert node != null : "Node cannot be null";
        assert cls != null : "Class cannot be null";
        return objectMapper.treeToValue(node, cls);
    }

    public static JsonNode loadFromFile(Path path) throws IOException {
        assert path != null : "Path cannot be null";
        assert Files.exists(path) : "File does not exist";
        String content = Files.readString(path);
        assert !content.isEmpty() : "Content cannot be empty";
        return deserialize(content);
    }

    public static <T extends ILNode> T loadFromFile(Path path, Class<T> cls) throws IOException {
        assert path != null : "Path cannot be null";
        assert Files.exists(path) : "File does not exist";
        String content = Files.readString(path);
        assert !content.isEmpty() : "Content cannot be empty";
        return deserialize(content, cls);
    }
}
