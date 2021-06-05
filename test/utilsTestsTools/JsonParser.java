package utilsTestsTools;

import java.io.File;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {
    public List<AGG_Test_Entity> readAGGEntitiesFromJson(String filename) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<AGG_Test_Entity>> mapType = new TypeReference<List<AGG_Test_Entity>>() {};
        List<AGG_Test_Entity> jsonToAGG_Test_Entity = objectMapper.readValue(new File(filename), mapType);
        return jsonToAGG_Test_Entity;
    }
}
