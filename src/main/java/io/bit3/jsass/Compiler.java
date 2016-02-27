package io.bit3.jsass;

import io.bit3.jsass.adapter.NativeAdapter;
import io.bit3.jsass.context.Context;
import io.bit3.jsass.context.FileContext;
import io.bit3.jsass.context.ImportStack;
import io.bit3.jsass.context.StringContext;
import io.bit3.jsass.function.FunctionArgumentSignatureFactory;
import io.bit3.jsass.function.FunctionWrapperFactory;

import org.apache.commons.io.Charsets;

import java.net.URI;
import java.nio.charset.Charset;

/**
 * The compiler compiles SCSS files, strings and contexts.
 */
public class Compiler {

  /**
   * The default defaultCharset that is used for compiling strings.
   */
  public final Charset defaultCharset = Charsets.UTF_8;

  /**
   * sass library adapter.
   */
  private final NativeAdapter adapter;

  /**
   * Create new compiler.
   */
  public Compiler() {
    FunctionArgumentSignatureFactory functionArgumentSignatureFactory
        = new FunctionArgumentSignatureFactory();
    FunctionWrapperFactory functionWrapperFactory
        = new FunctionWrapperFactory(functionArgumentSignatureFactory);

    adapter = new NativeAdapter(functionWrapperFactory);
  }

  /**
   * Compile string.
   *
   * @param string  The input string.
   * @param options The compile options.
   * @return The compilation output.
   * @throws CompilationException If the compilation failed.
   */
  public SuccessOutput compileString(String string, Options options) throws CompilationException {
    return compileString(string, defaultCharset, null, null, options);
  }

  /**
   * Compile string.
   *
   * @param string  The input string.
   * @param charset The defaultCharset of the input string.
   * @param options The compile options.
   * @return The compilation output.
   * @throws CompilationException If the compilation failed.
   */
  public SuccessOutput compileString(String string, Charset charset, Options options)
      throws CompilationException {
    return compileString(string, charset, null, null, options);
  }

  /**
   * Compile string.
   *
   * @param string     The input string.
   * @param inputPath  The input path.
   * @param outputPath The output path.
   * @param options    The compile options.
   * @return The compilation output.
   * @throws CompilationException If the compilation failed.
   */
  public SuccessOutput compileString(String string, URI inputPath, URI outputPath, Options options)
      throws CompilationException {
    return compileString(string, defaultCharset, inputPath, outputPath, options);
  }

  /**
   * Compile string.
   *
   * @param string     The input string.
   * @param charset    The defaultCharset of the input string.
   * @param inputPath  The input path.
   * @param outputPath The output path.
   * @param options    The compile options.
   * @return The compilation output.
   * @throws CompilationException If the compilation failed.
   */
  public SuccessOutput compileString(
      String string, Charset charset, URI inputPath, URI outputPath,
      Options options
  ) throws CompilationException {
    StringContext context = new StringContext(string, inputPath, outputPath, options);

    return compile(context);
  }

  /**
   * Compile file.
   *
   * @param inputPath  The input path.
   * @param outputPath The output path.
   * @param options    The compile options.
   * @return The compilation output.
   * @throws CompilationException If the compilation failed.
   */
  public SuccessOutput compileFile(URI inputPath, URI outputPath, Options options)
      throws CompilationException {
    FileContext context = new FileContext(inputPath, outputPath, options);
    return compile(context);
  }

  /**
   * Compile context.
   *
   * @param context The context.
   * @return The compilation output.
   * @throws CompilationException If the compilation failed.
   */
  public SuccessOutput compile(Context context) throws CompilationException {
    if (context instanceof FileContext) {
      return compile((FileContext) context);
    }

    if (context instanceof StringContext) {
      return compile((StringContext) context);
    }

    throw new RuntimeException(
        String.format(
            "Context type \"%s\" is not supported",
            null == context ? "null" : context.getClass().getName()
        )
    );
  }

  /**
   * Compile a string context.
   *
   * @param context The string context.
   * @return The compilation output.
   * @throws CompilationException If the compilation failed.
   */
  public SuccessOutput compile(StringContext context) throws CompilationException {
    final ImportStack importStack = new ImportStack();

    return adapter.compile(context, importStack);
  }

  /**
   * Compile file.
   *
   * @param context The file context.
   * @return The compilation output.
   * @throws CompilationException If the compilation failed.
   */
  public SuccessOutput compile(FileContext context) throws CompilationException {
    final ImportStack importStack = new ImportStack();

    return adapter.compile(context, importStack);
  }
}
