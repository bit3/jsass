/**
 * jsass is a feature complete java sass compiler,
 * using <a href="https://github.com/sass/libsass">libsass</a>.
 *
 * <p>You should start with the {@link io.bit3.jsass.Compiler} class,
 * here is a short quick start example.</p>
 * <pre>
 * String input = "body { color: red; }";
 * URI inputFile = new File("stylesheet.scss").toURI();
 * URI outputFile = new File("stylesheet.css").toURI();
 *
 * Compiler compiler = new Compiler();
 * Options options = new Options();
 *
 * try {
 *   Output output = compiler.compileString(input, inputFile, outputFile, options);
 *
 *   out.println("Compiled successfully");
 *   out.println(output.getCss());
 * } catch (CompilationException e) {
 *   err.println("Compile failed");
 *   err.println(e.getErrorText());
 * }
 * </pre>
 * <p>You will find more <a href="http://jsass.readthedocs.io/en/latest/examples.html">examples
 * in the documentation</a>.</p>
 */
package io.bit3.jsass;
