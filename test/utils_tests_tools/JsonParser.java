package utils_tests_tools;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;

public class JsonParser {
    public List<AGGTestEntity> readAGGEntitiesFromJson(String filename) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<AGGTestEntity>> mapType = new TypeReference<>() {
        };
        return objectMapper.readValue(new File(filename), mapType);
    }
}
