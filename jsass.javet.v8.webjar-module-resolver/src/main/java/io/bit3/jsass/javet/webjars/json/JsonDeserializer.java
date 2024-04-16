package io.bit3.jsass.javet.webjars.json;

import java.io.InputStream;
import java.util.Map;

public interface JsonDeserializer {
  Map<String, Object> deserialize(InputStream inputStream);
}
