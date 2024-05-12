package io.bit3.jsass.javet.js;

import io.bit3.jsass.Resources;
import java.util.Objects;

public final class JavaScriptModules {
  private JavaScriptModules() {
    throw new UnsupportedOperationException();
  }

  private static final String IMPORT_SASS_RESOURCE_NAME = "io/bit3/jsass/javet/js/import-sass.js";

  private static final String IMPORT_SASS_SCRIPT = Objects.requireNonNull(
      Resources.toString(
        JavaScriptModules.class.getClassLoader(),
        IMPORT_SASS_RESOURCE_NAME
      )
  );

  public static final PlainJavaScriptModule IMPORT_SASS_MODULE = PlainJavaScriptModule
      .builder()
      .script(IMPORT_SASS_SCRIPT)
      .build();

  private static final String IMPORT_POSTCSS_RESOURCE_NAME = "io/bit3/jsass/javet/js/import-postcss.js";

  private static final String IMPORT_POSTCSS_SCRIPT = Objects.requireNonNull(
      Resources.toString(
        JavaScriptModules.class.getClassLoader(),
          IMPORT_POSTCSS_RESOURCE_NAME
      )
  );

  public static final PlainJavaScriptModule IMPORT_POSTCSS_MODULE = PlainJavaScriptModule
      .builder()
      .script(IMPORT_POSTCSS_SCRIPT)
      .build();

  private static final String IMPORT_AUTOPREFIXER_RESOURCE_NAME = "io/bit3/jsass/javet/js/import-autoprefixer.js";

  private static final String IMPORT_AUTOPREFIXER_SCRIPT = Objects.requireNonNull(
      Resources.toString(
        JavaScriptModules.class.getClassLoader(),
          IMPORT_AUTOPREFIXER_RESOURCE_NAME
      )
  );

  public static final PlainJavaScriptModule IMPORT_AUTOPREFIXER_MODULE = PlainJavaScriptModule
      .builder()
      .script(IMPORT_AUTOPREFIXER_SCRIPT)
      .build();

}
