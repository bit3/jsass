package io.bit3.jsass.json.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.bit3.jsass.json.JsonDeserializer;
import lombok.SneakyThrows;
import java.io.InputStream;
import java.util.Map;

public class JacksonJsonDeserializer implements JsonDeserializer {

  private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<>() {
  };

  private final ObjectMapper objectMapper = new ObjectMapper();

  @SneakyThrows
  @Override
  public Map<String, Object> deserialize(InputStream inputStream) {
    return objectMapper.readValue(inputStream, MAP_TYPE_REFERENCE);
  }

}
