package io.bit3.jsass.function;

import com.sun.jna.Pointer;

import io.bit3.jsass.CompilationException;
import io.bit3.jsass.importer.Import;
import io.bit3.jsass.importer.ImportFactory;
import io.bit3.jsass.type.SassList;
import io.bit3.jsass.type.TypeUtils;
import sass.SassLibrary;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Wraps a java function as libsass function and pass the arguments through.
 */
public class FunctionWrapper implements SassLibrary.Sass_Function_Fn {

  /**
   * SASS library adapter.
   */
  private final SassLibrary sass;

  /**
   * The import factory.
   */
  private final ImportFactory importFactory;

  /**
   * The original function declaration, that is created by the factory.
   */
  private final FunctionDeclaration declaration;

  /**
   * Create a new wrapper for the given function.
   *
   * @param sass          The libsass adapter.
   * @param importFactory The import factory.
   * @param declaration   The function declaration.
   */
  public FunctionWrapper(SassLibrary sass, ImportFactory importFactory,
                         FunctionDeclaration declaration) {
    this.sass = sass;
    this.importFactory = importFactory;
    this.declaration = declaration;
  }

  @Override
  public SassLibrary.Sass_Value apply(SassLibrary.Sass_Value value, Pointer cb,
                                      SassLibrary.Sass_Compiler compiler) {
    try {
      Import lastImport;
      int importStackSize = sass.sass_compiler_get_import_stack_size(compiler).intValue();

      if (importStackSize > 0) {
        SassLibrary.Sass_Import_Entry lastImportEntry = sass.sass_compiler_get_last_import(
            compiler
        );
        lastImport = importFactory.create(lastImportEntry);
      } else {
        SassLibrary.Sass_Context context = sass.sass_compiler_get_context(compiler);
        SassLibrary.Sass_Options options = sass.sass_context_get_options(context);
        String inputPath = sass.sass_option_get_input_path(options);
        lastImport = new Import(new URI(inputPath), new URI(""));
      }

      Object decodedValue = TypeUtils.decodeValue(sass, value);
      SassList sassList;

      if (decodedValue instanceof SassList) {
        sassList = (SassList) decodedValue;
      } else {
        sassList = new SassList();
        sassList.add(decodedValue);
      }

      Object result = declaration.invoke(sassList, lastImport);

      return TypeUtils.encodeValue(sass, result);
    } catch (CompilationException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
