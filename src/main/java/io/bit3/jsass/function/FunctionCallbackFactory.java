package io.bit3.jsass.function;

import com.ochafik.lang.jnaerator.runtime.NativeSize;
import io.bit3.jsass.annotation.DefaultBooleanValue;
import io.bit3.jsass.annotation.DefaultByteValue;
import io.bit3.jsass.annotation.DefaultCharacterValue;
import io.bit3.jsass.annotation.DefaultDoubleValue;
import io.bit3.jsass.annotation.DefaultFloatValue;
import io.bit3.jsass.annotation.DefaultIntegerValue;
import io.bit3.jsass.annotation.DefaultLongValue;
import io.bit3.jsass.annotation.DefaultShortValue;
import io.bit3.jsass.annotation.DefaultStringValue;
import io.bit3.jsass.annotation.Name;
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
import io.bit3.jsass.importer.ImportFactory;
import sass.SassLibrary;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Factory that create libsass function callbacks and wrap them into {@link
 * io.bit3.jsass.function.FunctionWrapper}s.
 */
public class FunctionCallbackFactory {

  /**
   * SASS library adapter.
   */
  private SassLibrary sass;

  /**
   * The import factory.
   */
  private final ImportFactory importFactory;

  private final List<ArgumentConverterFactory> argumentConverterFactories;

  public FunctionCallbackFactory(SassLibrary sass) {
    this(sass, new ImportFactory(sass));
  }

  public FunctionCallbackFactory(SassLibrary sass, ImportFactory importFactory) {
    this.sass = sass;
    this.importFactory = importFactory;

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

    signature.append(method.getName()).append("(");

    for (int index = 0; index < parameters.length; index++) {
      Parameter parameter = parameters[index];
      String name = getParameterName(parameter);

      if (index > 0) {
        signature.append(", ");
      }

      signature.append("$").append(name);

      Object defaultValue = getDefaultValue(parameter);

      if (null != defaultValue) {
        signature.append(": ").append(formatDefaultValue(defaultValue));
      }
    }

    signature.append(")");

    List<ArgumentConverter> argumentConverters = createArgumentConverters(object, method);

    return new FunctionDeclaration(
        context,
        signature.toString(),
        object,
        method,
        argumentConverters
    );
  }

  /**
   * Determine annotated name of a method parameter.
   *
   * @param parameter The method parameter.
   * @return The parameters name.
   */
  private String getParameterName(Parameter parameter) {
    Name annotation = parameter.getAnnotation(Name.class);

    if (null == annotation) {
      return parameter.getName();
    }

    return annotation.value();
  }

  /**
   * Determine annotated default parameter value.
   *
   * @param parameter The method parameter.
   * @return The parameters default value.
   */
  private Object getDefaultValue(Parameter parameter) {
    Class<?> type = parameter.getType();

    if (CharSequence.class.isAssignableFrom(type)) {
      DefaultStringValue defaultStringValue = parameter.getAnnotation(DefaultStringValue.class);

      if (null != defaultStringValue) {
        return defaultStringValue.value();
      }
    }

    if (Byte.class.isAssignableFrom(type) || byte.class.isAssignableFrom(type)) {
      DefaultByteValue defaultByteValue = parameter.getAnnotation(DefaultByteValue.class);

      if (null != defaultByteValue) {
        return defaultByteValue.value();
      }
    }

    if (Short.class.isAssignableFrom(type) || short.class.isAssignableFrom(type)) {
      DefaultShortValue defaultShortValue = parameter.getAnnotation(DefaultShortValue.class);

      if (null != defaultShortValue) {
        return defaultShortValue.value();
      }
    }

    if (Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type)) {
      DefaultIntegerValue defaultIntegerValue = parameter.getAnnotation(DefaultIntegerValue.class);

      if (null != defaultIntegerValue) {
        return defaultIntegerValue.value();
      }
    }

    if (Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type)) {
      DefaultLongValue defaultLongValue = parameter.getAnnotation(DefaultLongValue.class);

      if (null != defaultLongValue) {
        return defaultLongValue.value();
      }
    }

    if (Float.class.isAssignableFrom(type) || float.class.isAssignableFrom(type)) {
      DefaultFloatValue defaultFloatValue = parameter.getAnnotation(DefaultFloatValue.class);

      if (null != defaultFloatValue) {
        return defaultFloatValue.value();
      }
    }

    if (Double.class.isAssignableFrom(type) || double.class.isAssignableFrom(type)) {
      DefaultDoubleValue defaultDoubleValue = parameter.getAnnotation(DefaultDoubleValue.class);

      if (null != defaultDoubleValue) {
        return defaultDoubleValue.value();
      }
    }

    if (Character.class.isAssignableFrom(type) || char.class.isAssignableFrom(type)) {
      DefaultCharacterValue defaultCharacterValue = parameter.getAnnotation(
          DefaultCharacterValue.class
      );

      if (null != defaultCharacterValue) {
        return defaultCharacterValue.value();
      }
    }

    if (Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type)) {
      DefaultBooleanValue defaultBooleanValue = parameter.getAnnotation(DefaultBooleanValue.class);

      if (null != defaultBooleanValue) {
        return defaultBooleanValue.value();
      }
    }

    return null;
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

    // fallback to quoted string
    String string = value.toString();
    string = sass.sass_string_unquote(string).getString(0);
    return sass.sass_string_quote(string, (byte) '\'').getString(0);
  }

  private List<ArgumentConverter> createArgumentConverters(Object object, Method method) {
    List<ArgumentConverter> list = new ArrayList<>(method.getParameterCount());

    for (Parameter parameter : method.getParameters()) {
      ArgumentConverter argumentConverter = createArgumentConverter(object, method, parameter);
      list.add(argumentConverter);
    }

    return list;
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
