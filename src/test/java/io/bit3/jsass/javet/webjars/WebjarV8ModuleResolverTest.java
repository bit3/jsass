package io.bit3.jsass.javet.webjars;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.engine.JavetEnginePool;
import io.bit3.jsass.json.jackson.JacksonJsonDeserializer;
import org.junit.jupiter.api.Test;

class WebjarV8ModuleResolverTest {

  @Test
  void testSingleUseWithSingletonRuntime() throws JavetException {
    final var moduleResolver = WebjarV8ModuleResolver
        .builder()
        .jsonDeserializer(new JacksonJsonDeserializer())
        .build();

    try (final var v8runtime = V8Host.getV8Instance().createV8Runtime()) {
      resolveAndValidateModule(v8runtime, moduleResolver);
    }
  }

  @Test
  void testSingleUseWithPooledRuntime() throws JavetException {
    final var moduleResolver = WebjarV8ModuleResolver
        .builder()
        .jsonDeserializer(new JacksonJsonDeserializer())
        .build();

    try (
        final var enginePool = new JavetEnginePool<>();
        final var v8engine = enginePool.getEngine();
        final var v8runtime = v8engine.getV8Runtime()
    ) {
      resolveAndValidateModule(v8runtime, moduleResolver);
    }
  }

  @Test
  void testReuseWithSingletonRuntime() throws JavetException {
    final var moduleResolver = WebjarV8ModuleResolver
        .builder()
        .jsonDeserializer(new JacksonJsonDeserializer())
        .build();

    try (final var v8runtime = V8Host.getV8Instance().createV8Runtime()) {
      resolveAndValidateModule(v8runtime, moduleResolver);
    }

    try (final var v8runtime = V8Host.getV8Instance().createV8Runtime()) {
      // Reusing with different runtimes is not supported
      assertThrows(JavetException.class, () -> resolveAndValidateModule(v8runtime, moduleResolver));
    }
  }

  @Test
  void testReuseWithPooledRuntime() throws JavetException {
    final var moduleResolver = WebjarV8ModuleResolver
        .builder()
        .jsonDeserializer(new JacksonJsonDeserializer())
        .build();

    try (final var enginePool = new JavetEnginePool<>()) {
      try (final var v8engine1 = enginePool.getEngine()) {
        try (final var v8runtime = v8engine1.getV8Runtime()) {
          resolveAndValidateModule(v8runtime, moduleResolver);
        }

        // request new engine before first one is closed to enforce requesting a new engine
        try (final var v8engine2 = enginePool.getEngine()) {
          try (final var v8runtime = v8engine2.getV8Runtime()) {
            resolveAndValidateModule(v8runtime, moduleResolver);
          }

          // request new engine before first one is closed to enforce requesting a new engine
          try (final var v8engine3 = enginePool.getEngine()) {
            try (final var v8runtime = v8engine3.getV8Runtime()) {
              resolveAndValidateModule(v8runtime, moduleResolver);
            }
          }
        }
      }
    }
  }

  private static void resolveAndValidateModule(V8Runtime v8runtime, WebjarV8ModuleResolver moduleResolver)
      throws JavetException {
    try (final var v8valueObject = v8runtime.createV8ValueObject()) {
      final var scriptModule = v8runtime.createV8Module("script", v8valueObject);

      final var resolved = moduleResolver.resolve(v8runtime, "sass", scriptModule);

      assertEquals("META-INF/resources/webjars/sass/1.71.1/sass.default.js", resolved.getResourceName());
      assertNull(resolved.getException());
    }
  }

}