package io.bit3.jsass.function.arguments;

import io.bit3.jsass.context.Context;
import io.bit3.jsass.function.FunctionArgumentSignature;
import io.bit3.jsass.function.FunctionArgumentSignatureFactory;
import io.bit3.jsass.importer.Import;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Stack;

public interface ArgumentConverter {
  public Object convert(
      List<?> remainingArguments,
      Stack<Import> importStack, Context context
  );

  public List<FunctionArgumentSignature> argumentSignatures(
      Object object,
      Method method,
      Parameter parameter,
      FunctionArgumentSignatureFactory factory
  );
}
