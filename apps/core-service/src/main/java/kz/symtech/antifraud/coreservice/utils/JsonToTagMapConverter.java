package kz.symtech.antifraud.coreservice.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JsonToTagMapConverter {
    private final ObjectMapper objectMapper;

    public Map<Integer, String> convertJsonToTagMap(Object obj) {
        Map<Integer, String> resultMap = new HashMap<>();
        objectMapper.valueToTree(obj).forEach(node -> buildTagMap(node, "", resultMap));
        return resultMap;
    }

    private void buildTagMap(JsonNode jsonNode, String parentName, Map<Integer, String> resultMap) {
        Integer id = jsonNode.get("id").asInt();
        String tagName = jsonNode.get("name").asText();
        String tag = parentName.isEmpty() ? tagName : parentName.concat(".").concat(tagName);

        if (jsonNode.get("children").isNull()) {
            resultMap.put(id, tag);
        } else {
            jsonNode.get("children").forEach(node -> buildTagMap(node, tag, resultMap));
        }
    }
}
