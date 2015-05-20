package io.bit3.jsass.function;

import com.ochafik.lang.jnaerator.runtime.NativeSize;
import io.bit3.jsass.context.Context;
import io.bit3.jsass.function.arguments.ArgumentConverter;
import io.bit3.jsass.function.arguments.ArgumentConverterFactory;
import io.bit3.jsass.function.arguments.BooleanArgumentConverterFactory;
import io.bit3.jsass.function.arguments.ByteArgumentConverterFactory;
import io.bit3.jsass.function.arguments.CharacterArgumentConverterFactory;
import io.bit3.jsass.function.arguments.ContextArgumentConverterFactory;
import io.bit3.jsass.function.arguments.DoubleArgumentConverterFactory;
import io.bit3.jsass.function.arguments.FloatArgumentConverterFactory;
import io.bit3.jsass.function.arguments.IntegerArgumentConverterFactory;
import io.bit3.jsass.function.arguments.LastImportArgumentConverterFactory;
import io.bit3.jsass.function.arguments.LongArgumentConverterFactory;
import io.bit3.jsass.function.arguments.ObjectArgumentConverter;
import io.bit3.jsass.function.arguments.ShortArgumentConverterFactory;
import io.bit3.jsass.function.arguments.StringArgumentConverterFactory;
import io.bit3.jsass.importer.Import;
import io.bit3.jsass.importer.ImportFactory;
import io.bit3.jsass.type.SassString;
import sass.SassLibrary;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Factory that create libsass function callbacks and wrap them into {@link
 * io.bit3.jsass.function.FunctionWrapper}s.
 */
public class FunctionCallbackFactory {

  /**
   * SASS library adapter.
   */
  private SassLibrary sass;

  private final Stack<Import> importStack;

  /**
   * The import factory.
   */
  private final ImportFactory importFactory;

  private final FunctionArgumentSignatureFactory functionArgumentSignatureFactory;

  private final List<ArgumentConverterFactory> argumentConverterFactories;

  public FunctionCallbackFactory(SassLibrary sass, Stack<Import> importStack) {
    this(sass, importStack, new ImportFactory(sass), new FunctionArgumentSignatureFactory());
  }

  public FunctionCallbackFactory(
      SassLibrary sass,
      Stack<Import> importStack,
      ImportFactory importFactory
  ) {
    this(sass, importStack, importFactory, new FunctionArgumentSignatureFactory());
  }

  public FunctionCallbackFactory(
      SassLibrary sass,
      Stack<Import> importStack, FunctionArgumentSignatureFactory functionArgumentSignatureFactory
  ) {
    this(sass, importStack, new ImportFactory(sass), functionArgumentSignatureFactory);
  }

  public FunctionCallbackFactory(
      SassLibrary sass, Stack<Import> importStack, ImportFactory importFactory,
      FunctionArgumentSignatureFactory functionArgumentSignatureFactory
  ) {
    this.sass = sass;
    this.importStack = importStack;
    this.importFactory = importFactory;
    this.functionArgumentSignatureFactory = functionArgumentSignatureFactory;

    // TODO move this into options and make it configurable
    this.argumentConverterFactories = new LinkedList<>();
    this.argumentConverterFactories.add(new BooleanArgumentConverterFactory());
    this.argumentConverterFactories.add(new ByteArgumentConverterFactory());
    this.argumentConverterFactories.add(new CharacterArgumentConverterFactory());
    this.argumentConverterFactories.add(new ContextArgumentConverterFactory());
    this.argumentConverterFactories.add(new DoubleArgumentConverterFactory());
    this.argumentConverterFactories.add(new FloatArgumentConverterFactory());
    this.argumentConverterFactories.add(new IntegerArgumentConverterFactory());
    this.argumentConverterFactories.add(new LastImportArgumentConverterFactory());
    this.argumentConverterFactories.add(new LongArgumentConverterFactory());
    this.argumentConverterFactories.add(new ShortArgumentConverterFactory());
    this.argumentConverterFactories.add(new StringArgumentConverterFactory());
  }

  /**
   * Compile methods from all objects into libsass functions.
   *
   * @param objects A list of "function provider" objects.
   * @return The newly created list of libsass callbacks.
   */
  public List<SassLibrary.Sass_Function_Entry> compileFunctions(Context context, List<?> objects) {
    List<SassLibrary.Sass_Function_Entry> callbacks = new LinkedList<>();

    for (Object object : objects) {
      List<SassLibrary.Sass_Function_Entry> objectCallbacks = compileFunctions(context, object);

      callbacks.addAll(objectCallbacks);
    }

    return callbacks;
  }

  /**
   * Compile methods from an object into libsass functions.
   *
   * @param object The "function provider" object.
   * @return The newly created list of libsass callbacks.
   */
  public List<SassLibrary.Sass_Function_Entry> compileFunctions(Context context, Object object) {
    Class<?>                  functionClass = object.getClass();
    Method[]                  methods       = functionClass.getDeclaredMethods();
    List<FunctionDeclaration> declarations  = new LinkedList<>();

    for (Method method : methods) {
      int modifiers = method.getModifiers();

      if (!Modifier.isPublic(modifiers)) {
        continue;
      }

      FunctionDeclaration declaration = createDeclaration(context, object, method);
      declarations.add(declaration);
    }

    List<SassLibrary.Sass_Function_Entry> callbacks = new LinkedList<>();

    for (FunctionDeclaration declaration : declarations) {
      FunctionWrapper wrapper = new FunctionWrapper(sass, importFactory, declaration);
      SassLibrary.Sass_Function_Entry callback = sass.sass_make_function(
          declaration.signature,
          wrapper,
          null
      );

      callbacks.add(callback);
    }

    return callbacks;
  }

  /**
   * Convert a java list of libsass function callbacks into a libsass function list.
   *
   * @param callbacks The java list of libsass function callbacks.
   * @return The newly created libsass function list.
   */
  public SassLibrary.Sass_Function_List toSassCFunctionList(
      List<SassLibrary.Sass_Function_Entry> callbacks
  ) {
    SassLibrary.Sass_Function_List functionList = sass.sass_make_function_list(
        new NativeSize(
            callbacks.size()
        )
    );

    int index = 0;
    for (SassLibrary.Sass_Function_Entry callback : callbacks) {
      sass.sass_function_set_list_entry(functionList, new NativeSize(index), callback);
      index++;
    }

    return functionList;
  }

  /**
   * Create a function declaration from an object method.
   *
   * @param object The object.
   * @param method The method.
   * @return The newly created function declaration.
   */
  public FunctionDeclaration createDeclaration(Context context, Object object, Method method) {
    StringBuilder signature  = new StringBuilder();
    Parameter[]   parameters = method.getParameters();

    List<ArgumentConverter> argumentConverters = new ArrayList<>(method.getParameterCount());
    signature.append(method.getName()).append("(");

    for (int index = 0; index < parameters.length; index++) {
      Parameter parameter = parameters[index];
      ArgumentConverter argumentConverter = createArgumentConverter(object, method, parameter);

      argumentConverters.add(argumentConverter);

      List<FunctionArgumentSignature> list = argumentConverter.argumentSignatures(
          object,
          method,
          parameter,
          functionArgumentSignatureFactory
      );

      for (FunctionArgumentSignature functionArgumentSignature : list) {
        String name = functionArgumentSignature.getName();
        Object defaultValue = functionArgumentSignature.getDefaultValue();

        if (index > 0) {
          signature.append(", ");
        }

        signature.append("$").append(name);

        if (null != defaultValue) {
          signature.append(": ").append(formatDefaultValue(defaultValue));
        }
      }
    }

    signature.append(")");

    return new FunctionDeclaration(
        importStack, context,
        signature.toString(),
        object,
        method,
        argumentConverters
    );
  }

  /**
   * Format a default value for libsass function signature.
   *
   * @param value The default value.
   * @return The formated default value.
   */
  private String formatDefaultValue(Object value) {
    if (value instanceof Boolean) {
      return ((Boolean) value) ? "true" : "false";
    }

    if (value instanceof Number) {
      return value.toString();
    }

    if (value instanceof Collection) {
      StringBuilder builder = new StringBuilder();

      builder.append("(");
      boolean first = true;
      for (Object item : ((Collection) value)) {
        if (first) {
          first = false;
        } else {
          builder.append(",");
        }
        builder.append(formatDefaultValue(item));
      }
      builder.append(")");

      return builder.toString();
    }

    String string = value.toString();
    string = sass.sass_string_unquote(string).getString(0);

    if (string.startsWith("$")) {
      return string;
    }

    return sass.sass_string_quote(string, (byte) SassString.DEFAULT_QUOTE_CHARACTER).getString(0);
  }

  private ArgumentConverter createArgumentConverter(
      Object object, Method method,
      Parameter parameter
  ) {
    Class<?> type = parameter.getType();

    for (ArgumentConverterFactory factory : argumentConverterFactories) {
      if (factory.canHandle(type)) {
        return factory.create(object, method, parameter);
      }
    }

    return new ObjectArgumentConverter(type);
  }
}
