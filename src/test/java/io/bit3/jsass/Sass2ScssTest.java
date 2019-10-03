package io.bit3.jsass;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;

public class Sass2ScssTest extends AbstractCompileTest {
  public Sass2ScssTest() {
    super();
  }

  @Test
  public void testConvert () throws Exception {
    URL sourceUrl = getClass().getResource("/sass/input.sass");
    String source = IOUtils.toString(sourceUrl);

    int options = Sass2ScssOptions.PRETTIFY2 | Sass2ScssOptions.KEEP_COMMENT;
    String converted_output = Compiler.sass2scss(source, options);

    URL outputUrl = getClass().getResource("/scss/input.scss");
    String real_output = IOUtils.toString(outputUrl);

    Assertions.assertEquals(real_output, converted_output);
  }
}
